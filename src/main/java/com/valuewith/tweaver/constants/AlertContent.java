package com.valuewith.tweaver.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AlertContent {
  DELETED_GROUP(" 그룹이 삭제되었습니다."),
  NEW_APPLICATION("에 새로운 신청자가 있습니다."),
  APPLICATION_APPLY("에 신청이 승인되었습니다."),
  APPLICATION_REJECT("에 신청이 거부되었습니다."),
  ADD_MEMBER("에 새 멤버가 추가되었습니다."),
  UPDATE_GROUP_PLAN("에 그룹 일정이 수정되었습니다.");

  private final String content;

}
