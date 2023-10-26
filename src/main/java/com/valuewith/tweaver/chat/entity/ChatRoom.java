package com.valuewith.tweaver.chat.entity;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.group.entity.Group;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

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
