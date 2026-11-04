package com.figure.figure.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tools.jackson.databind.annotation.JsonDeserialize;

@Getter
@NoArgsConstructor
public class CharacterSubscriptionRequest {

    @NotNull(message = "신규 발표 알림 설정 값이 누락되었거나 null입니다.")
    @JsonDeserialize(using = StrictBooleanDeserializer.class)
    private Boolean announcementEnabled;

    @NotNull(message = "예약 시작 알림 설정 값이 누락되었거나 null입니다.")
    @JsonDeserialize(using = StrictBooleanDeserializer.class)
    private Boolean preorderEnabled;

    @NotNull(message = "예약 마감 알림 설정 값이 누락되었거나 null입니다.")
    @JsonDeserialize(using = StrictBooleanDeserializer.class)
    private Boolean deadlineEnabled;

    @NotNull(message = "재발매 알림 설정 값이 누락되었거나 null입니다.")
    @JsonDeserialize(using = StrictBooleanDeserializer.class)
    private Boolean rereleaseEnabled;

    @NotNull(message = "프라이즈 등장 예정 알림 설정 값이 누락되었거나 null입니다.")
    @JsonDeserialize(using = StrictBooleanDeserializer.class)
    private Boolean prizeEnabled;
}
