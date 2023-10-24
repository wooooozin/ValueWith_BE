package com.valuewith.tweaver.message;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TBL_MESSAGE")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_MESSAGE SET IS_DELETED = 1 WHERE MESSAGE_ID = ?")
public class Message extends Period {
    /**
     * Message PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    /**
     * Message Content(메시시 내용)
     **/
    @NotNull
    private String content;

    /**
     * Member Entity 와 연관 관계 (N : 1)
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
