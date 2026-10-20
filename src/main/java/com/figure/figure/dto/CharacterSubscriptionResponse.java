package com.figure.figure.dto;

import com.figure.figure.model.CharacterSubscription;

public record CharacterSubscriptionResponse(
        Long id,
        Long characterId,
        boolean announcementEnabled,
        boolean preorderEnabled,
        boolean deadlineEnabled,
        boolean rereleaseEnabled,
        boolean prizeEnabled
) {
    public static CharacterSubscriptionResponse from(CharacterSubscription subscription) {
        return new CharacterSubscriptionResponse(
                subscription.getId(), subscription.getCharacter().getId(),
                subscription.isAnnouncementEnabled(), subscription.isPreorderEnabled(),
                subscription.isDeadlineEnabled(), subscription.isRereleaseEnabled(),
                subscription.isPrizeEnabled()
        );
    }
}
