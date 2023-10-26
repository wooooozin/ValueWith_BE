package com.valuewith.tweaver.user.entity;

import com.valuewith.tweaver.auditing.Period;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TBL_USER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_USER SET IS_DELETED = 1 WHERE USER_ID = ?")
public class User extends Period {
    /**
     * User PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /**
     * User Email(사용자 이메일)
     **/
    @NotNull
    @Column(unique = true)
    private String email;

    /**
     * User Password(사용자 비밀번호)
     **/
    @NotNull
    private String password;

    /**
     * User NickName(사용자 닉네임)
     **/
    @NotNull
    private String nickName;

    /**
     * User Age(사용자 연령대)
     **/
    @NotNull
    private Integer age;

    /**
     * User Gender(사용자 성별)
     **/
    @NotNull
    private String gender;

    /**
     * User Profile Url(사용자 SNS 프로필 사진 경로)
     **/
    @NotNull
    private String profileUrl;

    /**
     * Is Social(소셜 로그인 여부)
     **/
    @NotNull
    private Boolean isSocial;

}
