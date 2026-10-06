package com.figure.figure.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReleaseScheduleUpdateRequest {

    @Min(1)
    @Max(9999)
    private Integer releaseYear;

    @Min(1)
    @Max(12)
    private Integer releaseMonth;

    @Min(1)
    @Max(31)
    private Integer releaseDay;
    private String note;

    @AssertTrue(
            message = "발매일을 확인해 주세요. 월·일을 입력하려면 연도부터 입력해야 하며, 실제 존재하는 날짜여야 합니다."
    )
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