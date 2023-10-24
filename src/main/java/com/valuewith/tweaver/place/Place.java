package com.valuewith.tweaver.place;

import com.valuewith.tweaver.auditing.Period;
import com.valuewith.tweaver.group.Group;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TBL_PLACE")
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE TBL_PLACE SET IS_DELETED = 1 WHERE PLACE_ID = ?")
public class Place extends Period {
    /**
     * Place PK(고유 번호)
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    /**
     * Place Name(여행 장소 이름)
     **/
    @NotNull
    private String name;

    /**
     * Place Longitude(여행 장소 경도)
     **/
    @NotNull
    private Double x;

    /**
     * Place Latitude(여행 장소 위도)
     **/
    @NotNull
    private Double y;


    /**
     * Place Address(여행 장소 주소)
     **/
    @NotNull
    private String address;

    /**
     * Place Code(카카오 맵 여행 장소 id)
     **/
    @NotNull
    private String placeCode;

    /**
     * Place Order(여행 장소 순서)
     **/
    @NotNull
    private Integer orders;

    /**
     * Place Distance(여행 장소 사이 거리)
     **/
    @NotNull
    private Double distance;

    /**
     * Group Entity 와 연관 관계 (N : 1)
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}
