package com.valuewith.tweaver.alert;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Table(name = "TBL_ALERT")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_ALERT SET IS_DELETED = 1 WHERE ALERT_ID = ?")
public class Alert extends Period {
    /**
     * Alert PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alertId;

    /**
     * Is Checked(확인 여부)
     **/
    private Boolean isChecked;

    /**
     * User Token(유저 토큰)
     **/
    private String userToken;

    /**
     * Group Id (그룹 아이디)
     **/
    private Long groupId;

    /**
     * User Entity 와 연관 관계 (N : 1)
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
