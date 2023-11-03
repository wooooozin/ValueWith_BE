package com.valuewith.tweaver.group.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.valuewith.tweaver.constants.ApprovedStatus;
import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.dto.TripGroupListResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupResponseDto;
import com.valuewith.tweaver.group.entity.QTripGroup;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import com.valuewith.tweaver.groupMember.entity.QGroupMember;
import com.valuewith.tweaver.groupMember.repository.GroupMemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripGroupListService {

    private final GroupMemberRepository groupMemberRepository;

    private static final QTripGroup qTripGroup = QTripGroup.tripGroup;
    private static final QGroupMember qGroupMember = QGroupMember.groupMember;
    private final JPAQueryFactory queryFactory;

    public TripGroupListResponseDto getFilteredTripGroups(
        String status, String area, String title, Pageable pageable
    ) {
        /*
        BooleanBuilder는 QueryDSL의 조건을 구성하는 데 사용되며,
        여러 필터링 조건을 동적으로 결합할 수 있음.
         */
        BooleanBuilder predicate = new BooleanBuilder();
        if (!"all".equalsIgnoreCase(status)) {
            predicate.and(qTripGroup.status.eq(GroupStatus.valueOf(status.toUpperCase())));
        }
        if (!"all".equalsIgnoreCase(area)) {
            predicate.and(qTripGroup.tripArea.eq(area));
        }
        if (title != null && !title.trim().isEmpty()) {
            predicate.and(qTripGroup.name.containsIgnoreCase(title.trim()));
        }

        List<TripGroup> tripGroups = queryFactory
            .selectFrom(qTripGroup)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(sortOrder(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
            .fetch();

        List<TripGroupResponseDto> tripGroupResponseDtoList = tripGroups.stream()
            .map(tripGroup -> {
                int currentMembersCount =
                    groupMemberRepository.countApprovedMembers(tripGroup, ApprovedStatus.APPROVED)
                        + 1;
                return TripGroupResponseDto.from(tripGroup, currentMembersCount);
            })
            .collect(Collectors.toList());

        long total = queryFactory
            .select(qTripGroup.count())
            .from(qTripGroup)
            .where(predicate)
            .fetchOne();

        int totalPages = (int) Math.ceil((double) total / pageable.getPageSize());
        boolean isLast = pageable.getOffset() + pageable.getPageSize() >= total;
        return TripGroupListResponseDto.from(
            tripGroupResponseDtoList,
            pageable.getPageNumber(),
            totalPages,
            total,
            isLast
        );
    }

    // 스프링의 Sort 객체를 QueryDSL OrderSpecifier로 변환
    private List<OrderSpecifier> sortOrder(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();
        //sort
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder<>(TripGroup.class, "tripGroup");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        return orders;
    }

}
}

