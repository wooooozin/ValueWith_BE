package com.valuewith.tweaver.place.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.valuewith.tweaver.group.entity.QTripGroup;
import com.valuewith.tweaver.place.entity.Place;
import com.valuewith.tweaver.place.entity.QPlace;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QPlace place = QPlace.place;

    @Override
    public List<Place> findByTripGroupId(Long tripGroupId) {
        return queryFactory
            .selectFrom(place)
            .join(place.tripGroup, QTripGroup.tripGroup).fetchJoin()
            .where(place.tripGroup.tripGroupId.eq(tripGroupId))
            .orderBy(place.orders.asc())
            .fetch();
    }
}
