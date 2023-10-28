package com.valuewith.tweaver.user.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MEMBER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE MEMBER SET IS_DELETED = 1 WHERE MEMBER_ID = ?")
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @NotNull
  @Column(unique = true)
  private String email;

  @NotNull
  private String password;

  @NotNull
  private String nickName;

  @NotNull
  private Integer age;

  @NotNull
  private String gender;

  @NotNull
  private String profileUrl;

  @NotNull
  private Boolean isSocial;
}
