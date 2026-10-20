package com.figure.figure.service;

import com.figure.figure.dto.CharacterSubscriptionRequest;
import com.figure.figure.exception.CharacterNotFoundException;
import com.figure.figure.exception.MemberNotFoundException;
import com.figure.figure.model.Character;
import com.figure.figure.model.CharacterSubscription;
import com.figure.figure.model.Member;
import com.figure.figure.model.MemberRole;
import com.figure.figure.repository.CharacterRepository;
import com.figure.figure.repository.CharacterSubscriptionRepository;
import com.figure.figure.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CharacterSubscriptionServiceTest {

    @Mock
    private CharacterSubscriptionRepository subscriptionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterSubscriptionService subscriptionService;

    @Test
    void subscribe_existingSubscription_updatesSettings() {
        // given: 회원·캐릭터·기존 구독이 존재한다
        Long memberId = 1L;
        Long characterId = 10L;

        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        Character character = new Character("하츠네 미쿠", "Piapro");

        CharacterSubscription existingSubscription =
                new CharacterSubscription(
                        member,
                        character,
                        true, true, true, true, true
                );

        // 요청 설정: 예약 시작 알림만 켠다
        CharacterSubscriptionRequest request =
                new CharacterSubscriptionRequest();

        ReflectionTestUtils.setField(request, "announcementEnabled", false);
        ReflectionTestUtils.setField(request, "preorderEnabled", true);
        ReflectionTestUtils.setField(request, "deadlineEnabled", false);
        ReflectionTestUtils.setField(request, "rereleaseEnabled", false);
        ReflectionTestUtils.setField(request, "prizeEnabled", false);

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(characterRepository.findById(characterId))
                .thenReturn(Optional.of(character));

        when(subscriptionRepository.findByMember_IdAndCharacter_Id(
                memberId, characterId
        )).thenReturn(Optional.of(existingSubscription));

        // when: 구독 저장 기능을 실행한다
        CharacterSubscriptionService.SubscribeResult result =
                subscriptionService.subscribe(
                        memberId,
                        characterId,
                        request
                );

        // then: 기존 구독 객체의 설정이 변경된다
        assertFalse(result.created());
        assertSame(existingSubscription, result.subscription());

        assertFalse(result.subscription().isAnnouncementEnabled());
        assertTrue(result.subscription().isPreorderEnabled());
        assertFalse(result.subscription().isDeadlineEnabled());
        assertFalse(result.subscription().isRereleaseEnabled());
        assertFalse(result.subscription().isPrizeEnabled());
    }

    @Test
    void subscribe_noExistingSubscription_createsSubscription() {
        // given
        Long memberId = 1L;
        Long characterId = 10L;

        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        Character character = new Character("하츠네 미쿠", "Piapro");
        CharacterSubscriptionRequest request =
                new CharacterSubscriptionRequest();

        ReflectionTestUtils.setField(request, "announcementEnabled", false);
        ReflectionTestUtils.setField(request, "preorderEnabled", true);
        ReflectionTestUtils.setField(request, "deadlineEnabled", false);
        ReflectionTestUtils.setField(request, "rereleaseEnabled", false);
        ReflectionTestUtils.setField(request, "prizeEnabled", false);

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(characterRepository.findById(characterId))
                .thenReturn(Optional.of(character));

        when(subscriptionRepository.findByMember_IdAndCharacter_Id(
                memberId, characterId
        )).thenReturn(Optional.empty());

        when(subscriptionRepository.save(any(CharacterSubscription.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        CharacterSubscriptionService.SubscribeResult result =
                subscriptionService.subscribe(
                        memberId,
                        characterId,
                        request
                );

        // then
        assertTrue(result.created());
        assertSame(member, result.subscription().getMember());
        assertSame(character, result.subscription().getCharacter());

        verify(subscriptionRepository).save(result.subscription());

        assertFalse(result.subscription().isAnnouncementEnabled());
        assertTrue(result.subscription().isPreorderEnabled());
        assertFalse(result.subscription().isDeadlineEnabled());
        assertFalse(result.subscription().isRereleaseEnabled());
        assertFalse(result.subscription().isPrizeEnabled());
    }

    @Test
    void subscribe_memberNotFound_throwsExceptionWithoutSaving() {
        // given
        Long memberId = 999L;
        Long characterId = 10L;

        CharacterSubscriptionRequest request =
                new CharacterSubscriptionRequest();

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                MemberNotFoundException.class,
                () -> subscriptionService.subscribe(
                        memberId,
                        characterId,
                        request
                )
        );

        verify(subscriptionRepository, never())
                .save(any(CharacterSubscription.class));
    }

    @Test
    void subscribe_characterNotFound_throwsExceptionWithoutSaving() {
        // given
        Long memberId = 1L;
        Long characterId = 999L;

        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        CharacterSubscriptionRequest request =
                new CharacterSubscriptionRequest();

        when(memberRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        when(characterRepository.findById(characterId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                CharacterNotFoundException.class,
                () -> subscriptionService.subscribe(
                        memberId,
                        characterId,
                        request
                )
        );

        // then
        verify(subscriptionRepository, never())
                .save(any(CharacterSubscription.class));
    }
}