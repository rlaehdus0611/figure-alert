
package com.figure.figure.repository;

import com.figure.figure.model.CharacterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CharacterSubscriptionRepository
        extends JpaRepository<CharacterSubscription, Long> {

    Optional<CharacterSubscription> findByMember_IdAndCharacter_Id(
            Long memberId,
            Long characterId
    );

    @Query("select s from CharacterSubscription s join fetch s.character "
            + "where s.member.id = :memberId order by s.id")
    List<CharacterSubscription> findAllForMember(@Param("memberId") Long memberId);

    @Query("select distinct s from CharacterSubscription s join fetch s.member "
            + "where s.character.id in (select fc.character.id from FigureCharacter fc where fc.figure.id = :figureId) "
            + "and s.member.role = com.figure.figure.model.MemberRole.USER")
    List<CharacterSubscription> findSubscribersForFigure(@Param("figureId") Long figureId);

    @Modifying
    @Query("delete from CharacterSubscription s "
            + "where s.member.id = :memberId and s.character.id = :characterId")
    int deleteForMember(@Param("memberId") Long memberId,
                        @Param("characterId") Long characterId);
}
