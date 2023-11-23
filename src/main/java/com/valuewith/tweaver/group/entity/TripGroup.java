package com.valuewith.tweaver.group.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.constants.ErrorCode;
import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.exception.CustomException;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import com.valuewith.tweaver.member.entity.Member;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "TRIP_GROUP")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE trip_group SET IS_DELETED = 1 WHERE TRIP_GROUP_ID = ?")
@Where(clause = "IS_DELETED = 0")
public class TripGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    private String name;

    @NotNull
    private String content;

    @NotNull
    private Integer maxMemberNumber;

    @NotNull
    private Integer currentMemberNumber;

    @NotNull
    private String tripArea;

    @NotNull
    private LocalDate tripDate;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private String thumbnailUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupStatus status;

    public void updateTripGroup(TripGroupRequestDto tripGroupRequestDto) {
        this.name = tripGroupRequestDto.getName();
        this.content = tripGroupRequestDto.getContent();
        this.maxMemberNumber = tripGroupRequestDto.getMaxMemberNumber();
        this.tripArea = tripGroupRequestDto.getTripArea();
        this.tripDate = tripGroupRequestDto.getTripDate();
        this.dueDate = tripGroupRequestDto.getDueDate() == null
            ? tripGroupRequestDto.getTripDate().minusDays(1)
            : tripGroupRequestDto.getDueDate();
        this.thumbnailUrl = tripGroupRequestDto.getThumbnailUrl() == null
            ? this.thumbnailUrl
            : tripGroupRequestDto.getThumbnailUrl();
        this.status = setGroupStatus();
    }

    /**
     * 그룹의 최대 인원 변경으로 인한 그룹 상태변경
     * 1.최대 인원이 현재 인원과 같고, 마감 날짜가 현재 날짜보다 빠르다면 -> 마감상태로 변경
     * 2.최대 인원이 현재 인원보다 크고, 마감 날짜가 현재 날짜와 같거나 늦다면 -> 모집상태로 변경
     */
    public GroupStatus setGroupStatus() {
        if (this.currentMemberNumber == this.maxMemberNumber
            || LocalDate.now().isAfter(this.dueDate)) {
            return GroupStatus.CLOSE;
        } else if(this.currentMemberNumber < this.maxMemberNumber
            && !LocalDate.now().isAfter(this.dueDate)){
            return GroupStatus.OPEN;
        }
        return this.status;
    }

    public void incrementCurrentMemberNumber() {
        this.currentMemberNumber = this.currentMemberNumber + 1;
        this.status = setGroupStatus();
    }
    public void decrementCurrentMemberNumber() {
        if (this.currentMemberNumber > 0) {
            this.currentMemberNumber = this.currentMemberNumber - 1;
            this.status = setGroupStatus();
        } else {
            throw new CustomException(ErrorCode.MEMBER_COUNT_CANNOT_BE_NEGATIVE);
        }
    }


}
