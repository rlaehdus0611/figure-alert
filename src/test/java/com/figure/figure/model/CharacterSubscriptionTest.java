package com.figure.figure.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSubscriptionTest {

    @Test
    void changeSettings()
    {
        // given
        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        Character character = new Character("하츠네 미쿠", "Piapro");

        CharacterSubscription subscription = new CharacterSubscription(
                member,
                character,
                true, true, true, true, true
        );
        // when
        subscription.changeSettings(
                false, false, false, false, false
        );

        // then
        assertFalse(subscription.isAnnouncementEnabled());
        assertFalse(subscription.isPreorderEnabled());
        assertFalse(subscription.isDeadlineEnabled());
        assertFalse(subscription.isRereleaseEnabled());
        assertFalse(subscription.isPrizeEnabled());
    }

    @Test
    void changeSettings_enablesOnlyPreorder() {
        // given: 알림이 모두 꺼진 구독 준비
        Member member = new Member(
                "test@example.com",
                "test-password",
                "테스트회원",
                MemberRole.USER
        );

        Character character = new Character("하츠네 미쿠", "Piapro");

        CharacterSubscription subscription = new CharacterSubscription(
                member,
                character,
                false, false, false, false, false
        );

        // when: 예약 시작 알림만 켠다
        subscription.changeSettings(
                false, true, false, false, false
        );

        // then: 예약 시작만 켜지고 나머지는 꺼져 있다
        assertFalse(subscription.isAnnouncementEnabled());
        assertTrue(subscription.isPreorderEnabled());
        assertFalse(subscription.isDeadlineEnabled());
        assertFalse(subscription.isRereleaseEnabled());
        assertFalse(subscription.isPrizeEnabled());
    }

}