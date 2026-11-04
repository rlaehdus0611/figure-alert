package com.figure.figure.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification", uniqueConstraints = @UniqueConstraint(name = "uk_notification_delivery",
        columnNames = {"member_id", "news_id", "type", "revision"}),
        indexes = @Index(name = "ix_notification_member_created", columnList = "member_id,created_at"))
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "news_id", nullable = false)
    private OfficialNews news;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private NotificationType type;
    @Column(nullable = false)
    private int revision;
    private Instant previousDeadline;
    private Instant changedDeadline;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    private Instant readAt;

    public Notification(Member member, OfficialNews news, NotificationType type, Instant now, Instant previousDeadline) {
        this.member = member; this.news = news; this.type = type; this.revision = news.getRevision();
        this.createdAt = now; this.previousDeadline = previousDeadline;
        this.changedDeadline = type == NotificationType.DEADLINE_CHANGED ? news.getDeadline() : null;
    }

    public void markRead(Instant now) { if (readAt == null) readAt = now; }
}
