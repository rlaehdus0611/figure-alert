package com.figure.figure.dto;

import com.figure.figure.model.OfficialNews;
import com.figure.figure.model.NotificationType;
import java.time.Instant;

public record OfficialNewsResponse(Long id, String eventKey, Long figureId, NotificationType type,
                                   String title, String sourceUrl, String displayDate,
                                   Instant deadline, int revision, Instant verifiedAt) {
    public static OfficialNewsResponse from(OfficialNews news) {
        return new OfficialNewsResponse(news.getId(), news.getEventKey(), news.getFigure().getId(), news.getType(),
                news.getTitle(), news.getSourceUrl(), news.getDisplayDate(), news.getDeadline(), news.getRevision(), news.getVerifiedAt());
    }
}
