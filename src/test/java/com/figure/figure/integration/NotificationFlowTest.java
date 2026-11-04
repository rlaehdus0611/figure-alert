package com.figure.figure.integration;

import com.figure.figure.model.*;
import com.figure.figure.model.Character;
import com.figure.figure.repository.*;
import com.figure.figure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:notification_flow;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
        "spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false", "jwt.secret=" + SubscriptionFlowTest.SECRET,
        "jwt.expiration=1800000", "figure.notifications.scheduling-enabled=false"
})
@AutoConfigureMockMvc
class NotificationFlowTest {
    @Autowired MockMvc mvc;
    @Autowired JsonMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired MemberRepository members;
    @Autowired CharacterRepository characters;
    @Autowired CharacterSubscriptionRepository subscriptions;
    @Autowired ManufacturerRepository manufacturers;
    @Autowired FigureRepository figures;
    @Autowired FigureCharacterRepository links;
    @Autowired JwtTokenProvider tokens;
    Member owner;
    Character character;
    Figure figure;
    String userAuth, adminAuth;

    @BeforeEach
    void prepare() {
        // The database is an isolated in-memory test database, never the local MySQL.
        if (jdbc.queryForObject("select count(*) from information_schema.tables where table_name='NOTIFICATION'", Long.class) > 0) {
            jdbc.execute("DELETE FROM notification");
            jdbc.execute("DELETE FROM official_news");
        }
        subscriptions.deleteAll(); links.deleteAll(); figures.deleteAll();
        manufacturers.deleteAll(); characters.deleteAll(); members.deleteAll();
        owner = members.save(new Member("owner@example.com", "unused", "owner", MemberRole.USER));
        var admin = members.save(new Member("admin@example.com", "unused", "admin", MemberRole.ADMIN));
        character = characters.save(new Character("미쿠", "test"));
        var maker = manufacturers.save(new Manufacturer("제조사"));
        figure = figures.save(new Figure("테스트 피규어", maker));
        links.save(new FigureCharacter(figure, character));
        subscriptions.save(new CharacterSubscription(owner, character, true, false, true, false, true));
        userAuth = "Bearer " + tokens.createToken(owner);
        adminAuth = "Bearer " + tokens.createToken(admin);
    }

    String news(String key, String type, String deadline) {
        return """
                {"eventKey":"%s","figureId":%d,"type":"%s","title":"공식 소식",
                 "sourceUrl":"https://example.com/official/news","displayDate":"2027년 11월 예정","deadline":%s}
                """.formatted(key, figure.getId(), type, deadline == null ? "null" : "\"" + deadline + "\"");
    }

    long publish(String key, String type, String deadline) throws Exception {
        var result = mvc.perform(post("/admin/news").header("Authorization", adminAuth)
                        .contentType(MediaType.APPLICATION_JSON).content(news(key, type, deadline)))
                .andExpect(status().isCreated()).andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void publishOnceListAndReadOnlyOwnNotification() throws Exception {
        publish("announcement-1", "ANNOUNCEMENT", null);
        mvc.perform(post("/admin/news").header("Authorization", adminAuth)
                        .contentType(MediaType.APPLICATION_JSON).content(news("announcement-1", "ANNOUNCEMENT", null)))
                .andExpect(status().isOk());
        assertThat(jdbc.queryForObject("select count(*) from notification", Long.class)).isEqualTo(1);
        var list = mvc.perform(get("/notifications").header("Authorization", userAuth))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.items[0].title").value("공식 소식"))
                .andExpect(jsonPath("$.items[0].readAt").isEmpty()).andReturn();
        long id = json.readTree(list.getResponse().getContentAsString()).get("items").get(0).get("id").asLong();
        var other = members.save(new Member("other@example.com", "unused", "other", MemberRole.USER));
        mvc.perform(patch("/notifications/" + id + "/read").header("Authorization", "Bearer " + tokens.createToken(other)))
                .andExpect(status().isNotFound());
        mvc.perform(patch("/notifications/" + id + "/read").header("Authorization", userAuth))
                .andExpect(status().isOk()).andExpect(jsonPath("$.readAt").isNotEmpty());
        var firstRead = jdbc.queryForObject("select read_at from notification where id=?", Object.class, id);
        mvc.perform(patch("/notifications/" + id + "/read").header("Authorization", userAuth)).andExpect(status().isOk());
        assertThat(jdbc.queryForObject("select read_at from notification where id=?", Object.class, id)).isEqualTo(firstRead);
    }

    @Test
    void disabledTypeAndMultipleCharacterSubscriptionsDoNotDuplicate() throws Exception {
        var another = characters.save(new Character("テト", "test"));
        links.save(new FigureCharacter(figure, another));
        subscriptions.save(new CharacterSubscription(owner, another, true, false, false, false, false));
        publish("preorder-1", "PREORDER_START", null);
        assertThat(jdbc.queryForObject("select count(*) from notification", Long.class)).isZero();
        publish("announcement-2", "ANNOUNCEMENT", null);
        assertThat(jdbc.queryForObject("select count(*) from notification", Long.class)).isEqualTo(1);
        publish("prize-1", "PRIZE_APPEARANCE", null);
        mvc.perform(get("/notifications").header("Authorization", userAuth))
                .andExpect(status().isOk()).andExpect(jsonPath("$.items[0].displayDate").value("2027년 11월 예정"));
    }

    @Test
    void userCannotPublishAndAdminCannotReadPersonalInbox() throws Exception {
        mvc.perform(post("/admin/news").header("Authorization", userAuth)
                .contentType(MediaType.APPLICATION_JSON).content(news("blocked", "ANNOUNCEMENT", null)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/notifications").header("Authorization", adminAuth)).andExpect(status().isForbidden());
        mvc.perform(get("/notifications?size=1000").header("Authorization", userAuth)).andExpect(status().isBadRequest());
        assertThat(jdbc.queryForObject("select count(*) from notification", Long.class)).isZero();
    }

    @Test
    void conflictingEventKeyDoesNotPublishAnotherEvent() throws Exception {
        publish("same-key", "ANNOUNCEMENT", null);
        mvc.perform(post("/admin/news").header("Authorization", adminAuth)
                        .contentType(MediaType.APPLICATION_JSON).content(news("same-key", "PRIZE_APPEARANCE", null)))
                .andExpect(status().isConflict());
        assertThat(jdbc.queryForObject("select count(*) from official_news", Long.class)).isEqualTo(1);
        assertThat(jdbc.queryForObject("select count(*) from notification", Long.class)).isEqualTo(1);
    }

    @Test
    void futureAndUnknownDeadlinesDoNotNotifyImmediately() throws Exception {
        publish("unknown-deadline", "PREORDER_DEADLINE", null);
        publish("future-deadline", "PREORDER_DEADLINE", Instant.now().plusSeconds(5 * 86400).toString());
        assertThat(jdbc.queryForObject("select count(*) from notification", Long.class)).isZero();
    }
}
