package com.valuewith.tweaver.group.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.dto.TripGroupRequestDto;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "TRIP_GROUP")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TRIP_GROUP SET IS_DELETED = 1 WHERE TRIP_GROUP_ID = ?")
public class TripGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripGroupId;

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
     * 1.최대 인원이 현재 인원과 같은 경우 -> 마감상태로 변경
     * 2.최대 인원이 현재 인원보다 큰 경우 -> 모집상태로 변경
     */
    public GroupStatus setGroupStatus() {
        GroupStatus resultStatus;
        if (this.currentMemberNumber.equals(this.maxMemberNumber)) {
            resultStatus = GroupStatus.CLOSE;
        } else {
            resultStatus = GroupStatus.OPEN;
        }

        if (this.status.equals(resultStatus)) {
            return this.status;
        } else {
            return resultStatus;
        }
    }
}
