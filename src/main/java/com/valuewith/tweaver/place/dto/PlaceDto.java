package com.valuewith.tweaver.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto {
  private Long placeId;
  private Long tripGroupId;
  private String name;
  private Double x;
  private Double y;
  private String address;
  private String placeCode;
  private Integer orders;
  private Double distance;
}
