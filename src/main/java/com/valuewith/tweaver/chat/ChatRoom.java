package com.valuewith.tweaver.chat;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.group.Group;
import com.valuewith.tweaver.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_CHAT_ROOM")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_CHAT_ROOM SET IS_DELETED = 1 WHERE CHAT_ROOM_ID = ?")
public class ChatRoom extends Period {
    /**
     * ChatRoom PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @NotNull
    private String title;

    /**
     * Group Entity 와 연관 관계 (1 : 1)
     **/
    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

}
