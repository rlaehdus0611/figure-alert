package com.figure.figure.integration;

import com.figure.figure.model.Character;
import com.figure.figure.model.Member;
import com.figure.figure.model.MemberRole;
import com.figure.figure.repository.CharacterRepository;
import com.figure.figure.repository.CharacterSubscriptionRepository;
import com.figure.figure.repository.MemberRepository;
import com.figure.figure.security.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:subscription_flow;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.show-sql=false",
        "jwt.secret=local-test-only-signing-key-0123456789-abcdefghijklmnopqrstuvwxyz",
        "jwt.expiration=1800000"
})
@AutoConfigureMockMvc
class SubscriptionFlowTest {
    static final String SECRET = "local-test-only-signing-key-0123456789-abcdefghijklmnopqrstuvwxyz";
    static final String SETTINGS = """
            {"announcementEnabled":true,"preorderEnabled":false,"deadlineEnabled":true,
             "rereleaseEnabled":false,"prizeEnabled":false}
            """;
    @Autowired MockMvc mvc;
    @Autowired JsonMapper json;
    @Autowired MemberRepository members;
    @Autowired CharacterRepository characters;
    @Autowired CharacterSubscriptionRepository subscriptions;
    @Autowired JwtTokenProvider tokens;
    Member owner;
    Character character;
    String token;

    @BeforeEach
    void prepare() {
        subscriptions.deleteAll();
        members.deleteAll();
        characters.deleteAll();
        owner = members.save(new Member("owner@example.com", "unused-hash", "owner", MemberRole.USER));
        character = characters.save(new Character("미쿠", "테스트"));
        token = tokens.createToken(owner);
    }

    String path() { return "/characters/" + character.getId() + "/subscription"; }

    @Test
    void saveUpdateReadListAndCancelCommitToDatabase() throws Exception {
        mvc.perform(put(path()).header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(SETTINGS))
                .andExpect(status().isCreated()).andExpect(header().string("Location", path()));
        var original = subscriptions.findByMember_IdAndCharacter_Id(owner.getId(), character.getId()).orElseThrow();
        var originalId = original.getId();
        var createdAt = original.getCreatedAt();
        mvc.perform(get(path()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.announcementEnabled").value(true))
                .andExpect(jsonPath("$.deadlineEnabled").value(true));
        mvc.perform(get("/members/me/subscriptions").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        String disabled = SETTINGS.replace("true", "false");
        mvc.perform(put(path()).header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(disabled))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(originalId));
        var updated = subscriptions.findByMember_IdAndCharacter_Id(owner.getId(), character.getId()).orElseThrow();
        assertThat(updated.getCreatedAt()).isEqualTo(createdAt);
        assertThat(updated.isAnnouncementEnabled()).isFalse();
        assertThat(updated.isDeadlineEnabled()).isFalse();
        assertThat(subscriptions.count()).isEqualTo(1);
        mvc.perform(delete(path()).header("Authorization", "Bearer " + token)).andExpect(status().isNoContent());
        mvc.perform(delete(path()).header("Authorization", "Bearer " + token)).andExpect(status().isNoContent());
        assertThat(subscriptions.count()).isZero();
    }

    @Test
    void anotherMemberCannotReadOrCancelOwnersSubscription() throws Exception {
        mvc.perform(put(path()).header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(SETTINGS)).andExpect(status().isCreated());
        var other = members.save(new Member("other@example.com", "unused", "other", MemberRole.USER));
        String otherToken = tokens.createToken(other);
        mvc.perform(get(path()).header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("SUBSCRIPTION_NOT_FOUND"));
        mvc.perform(get("/members/me/subscriptions").header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(delete(path()).header("Authorization", "Bearer " + otherToken)).andExpect(status().isNoContent());
        assertThat(subscriptions.count()).isEqualTo(1);
    }

    @Test
    void missingCharacterIsDifferentFromMissingSubscription() throws Exception {
        mvc.perform(get(path()).header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("SUBSCRIPTION_NOT_FOUND"));
        mvc.perform(get("/characters/999999/subscription").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.code").value("CHARACTER_NOT_FOUND"));
        assertThat(subscriptions.count()).isZero();
    }

    @Test
    void adminCannotUseAnyPersonalSubscriptionEndpoint() throws Exception {
        var admin = members.save(new Member("admin@example.com", "unused", "admin", MemberRole.ADMIN));
        String auth = "Bearer " + tokens.createToken(admin);
        mvc.perform(put(path()).header("Authorization", auth).contentType(MediaType.APPLICATION_JSON).content(SETTINGS))
                .andExpect(status().isForbidden());
        mvc.perform(get(path()).header("Authorization", auth)).andExpect(status().isForbidden());
        mvc.perform(delete(path()).header("Authorization", auth)).andExpect(status().isForbidden());
        mvc.perform(get("/members/me/subscriptions").header("Authorization", auth)).andExpect(status().isForbidden());
        assertThat(subscriptions.count()).isZero();
    }

    @Test
    void absentTamperedExpiredAndMalformedClaimsAreUnauthorized() throws Exception {
        mvc.perform(put(path()).contentType(MediaType.APPLICATION_JSON).content(SETTINGS)).andExpect(status().isUnauthorized());
        mvc.perform(get(path()).header("Authorization", "Bearer invalid.token.value")).andExpect(status().isUnauthorized());
        String expired = new JwtTokenProvider(SECRET, -1000).createToken(owner);
        mvc.perform(get(path()).header("Authorization", "Bearer " + expired)).andExpect(status().isUnauthorized());
        String invalidSubject = Jwts.builder().subject("not-a-member-id").claim("role", "USER")
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8))).compact();
        mvc.perform(get(path()).header("Authorization", "Bearer " + invalidSubject)).andExpect(status().isUnauthorized());
        assertThat(subscriptions.count()).isZero();
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"false\"", "0", "1", "\"true\"", "[]", "{}"})
    void nonBooleanSettingsAreRejectedWithoutSaving(String value) throws Exception {
        String body = SETTINGS.replace("\"announcementEnabled\":true", "\"announcementEnabled\":" + value);
        mvc.perform(put(path()).header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        assertThat(subscriptions.count()).isZero();
    }

    @Test
    void reportsAllMissingSettingsInScreenOrder() throws Exception {
        mvc.perform(put(path()).header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_SUBSCRIPTION_SETTINGS"))
                .andExpect(jsonPath("$.message", containsString("신규 발표")))
                .andExpect(jsonPath("$.message", containsString("예약 시작")))
                .andExpect(jsonPath("$.message", containsString("예약 마감")))
                .andExpect(jsonPath("$.message", containsString("재발매")))
                .andExpect(jsonPath("$.message", containsString("프라이즈")));
        assertThat(subscriptions.count()).isZero();
    }

    @Test
    void invalidPathIdIsBadRequest() throws Exception {
        mvc.perform(get("/characters/0/subscription").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signupLoginAndSubscribeUseRealPasswordHashAndSignature() throws Exception {
        String body = """
                {"email":"new@example.com","password":"Local-Test-2026!","nickname":"new"}
                """;
        mvc.perform(post("/members/signup").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        var login = mvc.perform(post("/members/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn();
        String issued = json.readTree(login.getResponse().getContentAsString()).get("accessToken").asString();
        var member = members.findByEmail("new@example.com").orElseThrow();
        assertThat(member.getPassword()).isNotEqualTo("Local-Test-2026!");
        mvc.perform(put(path()).header("Authorization", "Bearer " + issued)
                        .contentType(MediaType.APPLICATION_JSON).content(SETTINGS)).andExpect(status().isCreated());
        assertThat(subscriptions.findByMember_IdAndCharacter_Id(member.getId(), character.getId())).isPresent();
    }
}
