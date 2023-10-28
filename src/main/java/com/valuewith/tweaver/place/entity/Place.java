package com.valuewith.tweaver.place.entity;

import com.valuewith.tweaver.auditing.BaseEntity;
import com.valuewith.tweaver.group.entity.TripGroup;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PLACE")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE PLACE SET IS_DELETED = 1 WHERE PLACE_ID = ?")
public class Place extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long placeId;

  @NotNull
  private String name;

  @NotNull
  private Double x;

  @NotNull
  private Double y;

  @NotNull
  private String address;

  @NotNull
  private String placeCode;

  @NotNull
  private Integer orders;

  @NotNull
  private Double distance;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trip_group_id")
  private TripGroup tripGroup;
}
