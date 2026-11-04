package com.figure.figure.model;

public enum NotificationType {
    ANNOUNCEMENT, PREORDER_START, PREORDER_DEADLINE, RERELEASE, PRIZE_APPEARANCE, DEADLINE_CHANGED;

    public boolean enabledFor(CharacterSubscription subscription) {
        return switch (this) {
            case ANNOUNCEMENT -> subscription.isAnnouncementEnabled();
            case PREORDER_START -> subscription.isPreorderEnabled();
            case PREORDER_DEADLINE, DEADLINE_CHANGED -> subscription.isDeadlineEnabled();
            case RERELEASE -> subscription.isRereleaseEnabled();
            case PRIZE_APPEARANCE -> subscription.isPrizeEnabled();
        };
    }
}
