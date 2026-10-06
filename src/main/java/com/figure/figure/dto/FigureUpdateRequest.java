package com.figure.figure.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FigureUpdateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Positive
    private Long manufacturerId;

    @NotEmpty
    private List<@NotNull @Positive Long> characterIds;

    @AssertTrue(message = "캐릭터를 중복 선택할 수 없습니다.")
    public boolean isCharacterSelectionValid() {
        if (characterIds == null) {
            return true; // 누락은 @NotEmpty에서 검사
        }

        return characterIds.stream().distinct().count()
                == characterIds.size();
    }
}