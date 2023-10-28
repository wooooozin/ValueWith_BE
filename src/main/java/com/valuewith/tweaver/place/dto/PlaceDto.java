package com.valuewith.tweaver.place.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class PlaceDto {
  private Long placeId;
  private Long groupId;
  private String name;
  private Double x;
  private Double y;
  private String address;
  private String placeCode;
  private Integer orders;
  private Double distance;
}
