package com.figure.figure.dto;

import com.figure.figure.model.NotificationType;
import jakarta.validation.constraints.*;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public record OfficialNewsRequest(
        @NotBlank @Size(max = 100) @Pattern(regexp = "[A-Za-z0-9._:-]+") String eventKey,
        @NotNull @Positive Long figureId,
        @NotNull NotificationType type,
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 2048) String sourceUrl,
        @Size(max = 100) String displayDate,
        Instant deadline
) {
    public OfficialNewsRequest { if (deadline != null) deadline = deadline.truncatedTo(ChronoUnit.MICROS); }

    @AssertTrue(message = "변경 알림은 마감 수정 API에서만 생성할 수 있습니다.")
    public boolean isPublishableType() { return type != NotificationType.DEADLINE_CHANGED; }

    @AssertTrue(message = "공식 출처는 유효한 HTTPS 주소여야 합니다.")
    public boolean isOfficialSourceValid() {
        if (sourceUrl == null) return true;
        try {
            URI uri = URI.create(sourceUrl);
            return "https".equalsIgnoreCase(uri.getScheme()) && uri.getHost() != null && uri.getUserInfo() == null;
        } catch (IllegalArgumentException e) { return false; }
    }

    @AssertTrue(message = "예약 마감 시각은 예약 마감 이벤트에만 지정할 수 있습니다.")
    public boolean isDeadlineTypeValid() { return deadline == null || type == NotificationType.PREORDER_DEADLINE; }
}
