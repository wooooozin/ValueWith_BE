package com.valuewith.tweaver.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto {
  private Long placeId;
  private Long tripGroupId;
  private String category;
  private String name;
  private Double x;
  private Double y;
  private String address;
  private String placeCode;
  private Integer orders;
  private Double distance;
}
