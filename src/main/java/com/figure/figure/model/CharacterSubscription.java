package com.figure.figure.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "character_subscription",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_character_subscription_member_character",
                columnNames = {"member_id", "character_id"}
        )
)
public class CharacterSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean announcementEnabled;

    @Column(nullable = false)
    private boolean preorderEnabled;

    @Column(nullable = false)
    private boolean deadlineEnabled;

    @Column(nullable = false)
    private boolean rereleaseEnabled;

    @Column(nullable = false)
    private boolean prizeEnabled;

    public CharacterSubscription(
            Member member,
            Character character,
            boolean announcementEnabled,
            boolean preorderEnabled,
            boolean deadlineEnabled,
            boolean rereleaseEnabled,
            boolean prizeEnabled
    ) {
        this.member = member;
        this.character = character;
        this.announcementEnabled = announcementEnabled;
        this.preorderEnabled = preorderEnabled;
        this.deadlineEnabled = deadlineEnabled;
        this.rereleaseEnabled = rereleaseEnabled;
        this.prizeEnabled = prizeEnabled;
    }

    public void changeSettings(
            boolean announcementEnabled,
            boolean preorderEnabled,
            boolean deadlineEnabled,
            boolean rereleaseEnabled,
            boolean prizeEnabled
    ) {
        this.announcementEnabled = announcementEnabled;
        this.preorderEnabled = preorderEnabled;
        this.deadlineEnabled = deadlineEnabled;
        this.rereleaseEnabled = rereleaseEnabled;
        this.prizeEnabled = prizeEnabled;
    }

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}