package com.figure.figure.dto;

import com.figure.figure.model.Notification;
import com.figure.figure.model.NotificationType;
import java.time.Instant;

public record NotificationResponse(Long id, Long newsId, Long figureId, NotificationType type,
                                   String title, String figureName, String sourceUrl, String displayDate,
                                   Instant deadline, Instant previousDeadline, int revision,
                                   Instant createdAt, Instant readAt) {
    public static NotificationResponse from(Notification notification) {
        var news = notification.getNews();
        Instant deadline = notification.getType() == NotificationType.DEADLINE_CHANGED
                ? notification.getChangedDeadline() : news.getDeadline();
        return new NotificationResponse(notification.getId(), news.getId(), news.getFigure().getId(), notification.getType(),
                news.getTitle(), news.getFigure().getName(), news.getSourceUrl(), news.getDisplayDate(), deadline,
                notification.getPreviousDeadline(), notification.getRevision(), notification.getCreatedAt(), notification.getReadAt());
    }
}
