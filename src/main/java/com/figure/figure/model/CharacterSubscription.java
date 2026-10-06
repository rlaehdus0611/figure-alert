
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

    public CharacterSubscription(Member member, Character character) {
        this.member = member;
        this.character = character;
    }

    @PrePersist
    private void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
