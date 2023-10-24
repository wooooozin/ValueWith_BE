package com.valuewith.tweaver.group;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.chat.ChatRoom;
import com.valuewith.tweaver.constants.GroupStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "TBL_GROUP")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_GROUP SET IS_DELETED = 1 WHERE GROUP_ID = ?")
public class Group extends Period {
    /**
     * Group PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    /**
     * Group Name(그룹명)
     **/
    @NotNull
    private String name;

    /**
     * Group Content(그룹 소개)
     **/
    @NotNull
    private String content;

    /**
     * Group Max User Number(모집 최대 인원)
     **/
    @NotNull
    private Integer maxUserNumber;

    /**
     * Group Trip Area(여행 지역)
     **/
    @NotNull
    private String tripArea;

    /**
     * Group Trip Date(여행 날짜)
     **/
    @NotNull
    private LocalDate tripDate;

    /**
     * Group Due Date(모집 마감 날짜)
     **/
    @NotNull
    private LocalDate dueDate;

    /**
     * Group Snapshot Url(여행 지도 스냅샷)
     **/
    @NotNull
    private String snapshotUrl;

    /**
     * Group Status(그룹 상태)
     **/
    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupStatus status;

    /**
     * ChatRoom Entity 와 연관 관계 (1 : 1)
     **/
    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

}
