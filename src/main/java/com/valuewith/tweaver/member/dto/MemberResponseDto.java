package com.valuewith.tweaver.member.dto;

import com.valuewith.tweaver.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String nickName;
    private String email;
    private Integer age;
    private String gender;
    private String profileUrl;
    private String provider;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
            .memberId(member.getMemberId())
            .nickName(member.getNickName())
            .email(member.getEmail())
            .age(member.getAge())
            .gender(member.getGender())
            .profileUrl(member.getProfileUrl())
            .provider(member.getProvider().getValue().toLowerCase())
            .build();
    }
}
