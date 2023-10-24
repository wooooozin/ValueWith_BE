package com.valuewith.tweaver.member;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.chat.ChatRoom;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.MemberRole;
import com.valuewith.tweaver.group.Group;
import com.valuewith.tweaver.message.Message;
import com.valuewith.tweaver.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_MEMBER")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_MEMBER SET IS_DELETED = 1 WHERE MEMBER_ID = ?")
public class Member extends Period {
    /**
     * Member PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    /**
     * Member Role(멤버 역할)
     **/
    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    /**
     * Is Banned(퇴출 여부)
     **/
    @NotNull
    private Boolean isBanned;

    /**
     * Approved Status(승인 상태)
     **/
    @NotNull
    @Enumerated(EnumType.STRING)
    private ApprovedStatus approvedStatus;

    /**
     * Approved DateTime(승인된 날짜 시간)
     **/
    @NotNull
    private LocalDateTime approvedDateTime;

    /**
     * Message Entity 와 연관 관계 (1 : N)
     **/
    @OneToMany(mappedBy = "member")
    private List<Message> messages = new ArrayList<>();

    /**
     * User Entity 와 연관 관계 (N : 1)
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Group Entity 와 연관 관계 (N : 1)
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    /**
     * ChatRoom Entity 와 연관 관계 (N : 1)
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
}
