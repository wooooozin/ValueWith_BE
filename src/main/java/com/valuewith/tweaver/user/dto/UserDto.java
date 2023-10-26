package com.valuewith.tweaver.user.dto;

import com.valuewith.tweaver.alert.dto.AlertDto;
import com.valuewith.tweaver.member.dto.MemberDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class UserDto {
  private Long userId;
  private String email;
  private String password;
  private String nickName;
  private Integer age;
  private String gender;
  private String profileUrl;
  private Boolean isSocial;
  private List<MemberDto> members = new ArrayList<>();
  private List<AlertDto> alerts = new ArrayList<>();
}
