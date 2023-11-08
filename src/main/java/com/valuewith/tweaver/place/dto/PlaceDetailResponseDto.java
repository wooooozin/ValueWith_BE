package com.valuewith.tweaver.place.dto;

import com.valuewith.tweaver.place.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PlaceDetailResponseDto {

    private String name;
    private Double x;
    private Double y;
    private String address;
    private String placeCode;
    private Integer orders;
    private Double distance;

    public static PlaceDetailResponseDto from(Place place) {
        return PlaceDetailResponseDto.builder()
            .name(place.getName())
            .x(place.getX())
            .y(place.getY())
            .address(place.getAddress())
            .placeCode(place.getPlaceCode())
            .orders(place.getOrders())
            .distance(place.getDistance())
            .build();
    }
}
