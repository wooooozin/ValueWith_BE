package com.valuewith.tweaver.member.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 30, message = "닉네임은 2~30자 사이여야 합니다.")
    private String nickName;

    @NotNull(message = "나이는 필수 항목입니다.")
    @Min(value = 1, message = "나이는 1보다 크거나 같아야 합니다.")
    @Max(value = 150, message = "나이는 150보다 작거나 같아야 합니다.")
    private Integer age;

    private String profileUrl;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
        message = "비밀번호는 영문과 숫자를 포함한 6자 이상이어야 합니다."
    )
    private String firstPassword;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
        message = "비밀번호는 영문과 숫자를 포함한 6자 이상이어야 합니다."
    )
    private String secondPassword;
}
