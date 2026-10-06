package com.figure.figure.dto;

import com.figure.figure.model.ReleaseStatus;
import com.figure.figure.model.ReleaseType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReleaseCreateRequest {

    @NotNull
    private Long figureId;

    @Min(value = 1, message = "발매 연도는 1 이상이어야 합니다.")
    @Max(value = 9999, message = "발매 연도는 9999 이하여야 합니다.")
    private Integer releaseYear;

    @Min(value = 1, message = "발매 월은 1~12여야 합니다.")
    @Max(value = 12, message = "발매 월은 1~12여야 합니다.")
    private Integer releaseMonth;

    @Min(value = 1, message = "발매 일은 1~31이어야 합니다.")
    @Max(value = 31, message = "발매 일은 1~31이어야 합니다.")
    private Integer releaseDay;

    @Positive
    private int price;

    @NotNull
    private ReleaseType type;

    @NotNull
    private ReleaseStatus status;

    @AssertTrue(message = "발매일을 확인해 주세요. 월·일을 입력하려면 연도부터 입력해야 하며, 실제 존재하는 날짜여야 합니다.")
    public boolean isReleaseDateValid() {

        if (releaseMonth != null && releaseYear == null) {
            return false;
        }

        if (releaseDay != null && releaseMonth == null) {
            return false;
        }

        if (releaseYear != null
                && releaseMonth != null
                && releaseDay != null) {
            try {
                LocalDate.of(releaseYear, releaseMonth, releaseDay);
            } catch (DateTimeException e) {
                return false;
            }
        }

        return true;
    }
}