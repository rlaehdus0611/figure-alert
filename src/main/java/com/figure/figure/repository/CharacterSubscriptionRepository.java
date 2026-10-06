
package com.figure.figure.repository;

import com.figure.figure.model.CharacterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterSubscriptionRepository
        extends JpaRepository<CharacterSubscription, Long> {

    Optional<CharacterSubscription> findByMember_IdAndCharacter_Id(
            Long memberId,
            Long characterId
    );
}
