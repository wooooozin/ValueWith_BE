package com.valuewith.tweaver.alert.repository;

import static com.valuewith.tweaver.alert.entity.QAlert.*;
import static com.valuewith.tweaver.group.entity.QTripGroup.*;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.valuewith.tweaver.alert.dto.AlertResponseDto;
import com.valuewith.tweaver.alert.entity.QAlert;
import com.valuewith.tweaver.constants.GroupStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlertDslImpl implements AlertDsl {

  private final JPAQueryFactory query;

  @Override
  public List<AlertResponseDto> getAlertsByMemberId(Long memberId) {
    return query.select(
            Projections.fields(AlertResponseDto.class,
                alert.alertId,
                alert.redirectUrl,
                alert.content,
                alert.createdDateTime,
                alert.groupId,
                ExpressionUtils.as(
                    JPAExpressions.select(tripGroup.name)
                        .from(tripGroup)
                        .where(tripGroup.tripGroupId.eq(alert.groupId))
                        .distinct(),
                    "groupName"))
        ).from(alert)
        .where((alert.member.memberId.eq(memberId))
            .and(alert.isDeleted.eq(false))
        )
        .orderBy(alert.createdDateTime.desc())
        .fetch()
        .stream()
        .collect(Collectors.toList());
  }

  @Override
  public Long getAlertCountByMemberId(Long memberId) {
    return query.select(alert.count())
        .from(alert)
        .where((alert.member.memberId.eq(memberId))
            .and(alert.isDeleted.eq(false))
            .and(alert.isChecked.eq(false))
        ).fetchOne();
  }

  @Override
  public void checkAllByMemberId(Long memberId) {
    query
        .update(alert)
        .set(alert.isChecked, true)
        .where(alert.member.memberId.eq(memberId))
        .execute();
  }

}
