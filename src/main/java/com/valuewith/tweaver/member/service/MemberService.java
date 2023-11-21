package com.valuewith.tweaver.member.service;

import static com.valuewith.tweaver.constants.ErrorCode.*;

import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.constants.Provider;
import com.valuewith.tweaver.defaultImage.entity.DefaultImage;
import com.valuewith.tweaver.defaultImage.repository.DefaultImageRepository;
import com.valuewith.tweaver.exception.CustomException;
import com.valuewith.tweaver.member.dto.MemberRequestDto;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;
  private final DefaultImageRepository defaultImageRepository;

  public Member findMemberByEmail(String memberEmail) {
    return memberRepository.findByEmail(memberEmail)
        .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
  }

  public Member findMemberByMemberId(Long id) {
    return memberRepository.findById(id).orElseThrow(
        () -> new CustomException(MEMBER_NOT_FOUND)
    );
  }

  @Transactional
  public void modifiedMemberProfile(
      Long memberId, MemberRequestDto requestDto, String profileImageUrl
  ) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

    // 패스워드 처리 - 카카오면 그냥 endoedPassword 넣기
    String encodedPassword = member.getPassword();

    if (member.getProvider() != Provider.KAKAO) {
      if (!(StringUtils.isEmpty(requestDto.getFirstPassword())
          && StringUtils.isEmpty(requestDto.getSecondPassword()))
      ) {
        if (!requestDto.getFirstPassword().equals(requestDto.getSecondPassword())) {
          throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }
        encodedPassword = passwordEncoder.encode(requestDto.getFirstPassword());
      }
    }

    // 프로필 이미지 처리 - null 일 때
    if (profileImageUrl == null && StringUtils.isEmpty(requestDto.getProfileUrl())) {
      DefaultImage defaultImage = defaultImageRepository.findByImageName("멤버");
      profileImageUrl = defaultImage.getDefaultImageUrl();
    }

    member.modifiedProfile(
        requestDto.getNickName(), requestDto.getAge(), profileImageUrl, encodedPassword
    );
    memberRepository.save(member);
  }
}
