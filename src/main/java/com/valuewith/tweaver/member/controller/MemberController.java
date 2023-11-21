package com.valuewith.tweaver.member.controller;

import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.constants.ImageType;
import com.valuewith.tweaver.defaultImage.service.ImageService;
import com.valuewith.tweaver.exception.CustomException;
import com.valuewith.tweaver.member.dto.MemberRequestDto;
import com.valuewith.tweaver.member.dto.MemberResponseDto;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import io.swagger.annotations.Authorization;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final TokenService tokenService;
    private final MemberService memberService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMemberProfile(
        @RequestHeader("Authorization") String token
    ) {
        String memberEmail = tokenService.getMemberEmail(token);
        Member member = memberService.findMemberByEmail(memberEmail);
        MemberResponseDto result = MemberResponseDto.from(member);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<String> modifiedMemberProfile(
        @RequestHeader("Authorization") String token,
        @PathVariable Long memberId,
        @Valid @RequestPart MemberRequestDto requestDto,
        @RequestPart(required = false) MultipartFile file
    ) {
        String memberEmail = tokenService.getMemberEmail(token);
        Member member = memberService.findMemberByEmail(memberEmail);

        if (member.getMemberId() != memberId) {
            throw new CustomException(ErrorCode.INVALID_PROFILE_MODIFIED_MEMBER);
        }

        /**
        * 새로 사진을 올릴 때 -> file 값 있고 profileUrl 원래 값 -> 새로운 이미지 생성 및 교체
        * 프로필 사진 안바꾸면 -> file 값 없고 profileUrl 원래 값
        * 프로필 사진 삭제하면 -> file 값 없고 profileUrl 빈 값
         */
        String profileImageUrl;
        if (file != null && !file.isEmpty() && requestDto.getProfileUrl() != null &&!requestDto.getProfileUrl().isEmpty()) {
            profileImageUrl = imageService.uploadImageAndGetUrl(file, ImageType.PROFILE);
        } else if (file == null && requestDto.getProfileUrl() != null && !requestDto.getProfileUrl().isEmpty()) {
            profileImageUrl = requestDto.getProfileUrl();
        } else {
            profileImageUrl = null;
        }

        memberService.modifiedMemberProfile(member.getMemberId(), requestDto, profileImageUrl);
        return ResponseEntity.ok().body(member.getNickName() + "의 프로필이 변경되었습니다.");
    }
}
