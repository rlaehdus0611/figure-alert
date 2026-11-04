package com.figure.figure.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "official_news", uniqueConstraints = @UniqueConstraint(name = "uk_official_news_event_key", columnNames = "event_key"),
        indexes = @Index(name = "ix_news_pending_deadline", columnList = "type,deadline_processed,deadline"))
public class OfficialNews {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_key", nullable = false, length = 100)
    private String eventKey;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "figure_id", nullable = false)
    private Figure figure;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private NotificationType type;
    @Column(nullable = false, length = 255)
    private String title;
    @Column(nullable = false, length = 2048)
    private String sourceUrl;
    @Column(length = 100)
    private String displayDate;
    private Instant deadline;
    @Column(nullable = false)
    private int revision;
    @Column(nullable = false)
    private boolean deadlineProcessed;
    @Column(nullable = false, updatable = false)
    private Instant verifiedAt;

    public OfficialNews(String eventKey, Figure figure, NotificationType type, String title,
                        String sourceUrl, String displayDate, Instant deadline, Instant now) {
        this.eventKey = eventKey; this.figure = figure; this.type = type; this.title = title;
        this.sourceUrl = sourceUrl; this.displayDate = displayDate; this.deadline = deadline; this.verifiedAt = now;
    }

    public void changeDeadline(Instant deadline) {
        this.deadline = deadline;
        this.revision++;
        this.deadlineProcessed = false;
    }

    public void markDeadlineProcessed() { this.deadlineProcessed = true; }
}
