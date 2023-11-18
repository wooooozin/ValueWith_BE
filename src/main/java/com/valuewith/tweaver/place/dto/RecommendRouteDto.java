package com.valuewith.tweaver.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecommendRouteDto {
  private String placeCode;
  private Double x;
  private Double y;
  private int index;
}
