package com.figure.figure.service;

import com.figure.figure.dto.*;
import com.figure.figure.exception.NotificationNotFoundException;
import com.figure.figure.model.*;
import com.figure.figure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notifications;
    private final CharacterSubscriptionRepository subscriptions;
    private final Clock clock;

    @Transactional
    public void deliver(OfficialNews news, NotificationType type, Instant now, Instant previousDeadline) {
        // Callers hold the event's database lock, serializing duplicate processing.
        var sent = new HashSet<Long>();
        var previousRecipients = new HashSet<Long>();
        if (type == NotificationType.DEADLINE_CHANGED) {
            notifications.findByNews_IdAndType(news.getId(), NotificationType.PREORDER_DEADLINE)
                    .forEach(n -> previousRecipients.add(n.getMember().getId()));
        }
        for (var subscription : subscriptions.findSubscribersForFigure(news.getFigure().getId())) {
            Long memberId = subscription.getMember().getId();
            if (!type.enabledFor(subscription) || !sent.add(memberId)) continue;
            if (type == NotificationType.DEADLINE_CHANGED && !previousRecipients.contains(memberId)) continue;
            if (!notifications.existsByMember_IdAndNews_IdAndTypeAndRevision(memberId, news.getId(), type, news.getRevision())) {
                notifications.save(new Notification(subscription.getMember(), news, type, now, previousDeadline));
            }
        }
    }

    public NotificationPageResponse list(Long memberId, int page, int size) {
        var result = notifications.findByMember_Id(memberId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt", "id")));
        return new NotificationPageResponse(result.getContent().stream().map(NotificationResponse::from).toList(),
                page, size, result.getTotalElements(), result.getTotalPages());
    }

    @Transactional
    public NotificationResponse read(Long memberId, Long notificationId) {
        var notification = notifications.findByIdAndMember_Id(notificationId, memberId)
                .orElseThrow(NotificationNotFoundException::new);
        notification.markRead(clock.instant());
        return NotificationResponse.from(notification);
    }
}
