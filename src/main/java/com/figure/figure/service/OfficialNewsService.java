package com.figure.figure.service;

import com.figure.figure.dto.*;
import com.figure.figure.exception.*;
import com.figure.figure.model.*;
import com.figure.figure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OfficialNewsService {
    private final OfficialNewsRepository newsRepository;
    private final FigureRepository figures;
    private final NotificationService notifications;
    private final Clock clock;

    @Transactional
    public PublishResult publish(OfficialNewsRequest request) {
        var figure = figures.findLocked(request.figureId()).orElseThrow(FigureNotFoundException::new);
        var existing = newsRepository.findByEventKey(request.eventKey());
        if (existing.isPresent()) {
            var n = existing.get();
            if (!n.getFigure().getId().equals(request.figureId()) || n.getType() != request.type()
                    || !n.getTitle().equals(request.title()) || !n.getSourceUrl().equals(request.sourceUrl())
                    || !Objects.equals(n.getDisplayDate(), request.displayDate()) || !Objects.equals(n.getDeadline(), request.deadline())) {
                throw new NewsConflictException();
            }
            return new PublishResult(OfficialNewsResponse.from(n), false);
        }
        var now = clock.instant();
        var news = newsRepository.saveAndFlush(new OfficialNews(request.eventKey(), figure, request.type(), request.title(),
                request.sourceUrl(), request.displayDate(), request.deadline(), now));
        if (news.getType() != NotificationType.PREORDER_DEADLINE) {
            notifications.deliver(news, news.getType(), now, null);
        } else if (news.getDeadline() != null && news.getDeadline().isAfter(now)
                && !news.getDeadline().isAfter(now.plus(Duration.ofHours(72)))) {
            notifications.deliver(news, news.getType(), now, null);
            news.markDeadlineProcessed();
        }
        return new PublishResult(OfficialNewsResponse.from(news), true);
    }

    public record PublishResult(OfficialNewsResponse news, boolean created) {}
}
