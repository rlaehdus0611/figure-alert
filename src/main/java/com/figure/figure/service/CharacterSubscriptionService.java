package com.figure.figure.service;

import com.figure.figure.exception.CharacterNotFoundException;
import com.figure.figure.exception.MemberNotFoundException;
import com.figure.figure.model.Character;
import com.figure.figure.model.CharacterSubscription;
import com.figure.figure.model.Member;
import com.figure.figure.repository.CharacterRepository;
import com.figure.figure.repository.CharacterSubscriptionRepository;
import com.figure.figure.repository.MemberRepository;
import com.figure.figure.dto.CharacterSubscriptionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterSubscriptionService {

    private final CharacterSubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;

    @Transactional
    public SubscribeResult subscribe(
            Long memberId,
            Long characterId,
            CharacterSubscriptionRequest request
    ) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Character character = characterRepository.findById(characterId)
                .orElseThrow(CharacterNotFoundException::new);

        Optional<CharacterSubscription> existingSubscription =
                subscriptionRepository.findByMember_IdAndCharacter_Id(
                        memberId,
                        characterId
                );

        if (existingSubscription.isPresent()) {
            CharacterSubscription subscription = existingSubscription.get();

            subscription.changeSettings(
                    request.getAnnouncementEnabled(),
                    request.getPreorderEnabled(),
                    request.getDeadlineEnabled(),
                    request.getRereleaseEnabled(),
                    request.getPrizeEnabled()
            );

            return new SubscribeResult(subscription, false);
        }

        CharacterSubscription subscription =
                new CharacterSubscription(
                        member,
                        character,
                        request.getAnnouncementEnabled(),
                        request.getPreorderEnabled(),
                        request.getDeadlineEnabled(),
                        request.getRereleaseEnabled(),
                        request.getPrizeEnabled()
                );

        CharacterSubscription savedSubscription =
                subscriptionRepository.save(subscription);

        return new SubscribeResult(
                savedSubscription,
                true
        );
    }

    public record SubscribeResult(
            CharacterSubscription subscription,
            boolean created
    ) {
    }
}