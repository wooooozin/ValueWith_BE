package com.valuewith.tweaver.group.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.constants.GroupStatus;
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
    private Integer maxUserNumber;

    @NotNull
    private Integer currentUserNumber;

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
}
