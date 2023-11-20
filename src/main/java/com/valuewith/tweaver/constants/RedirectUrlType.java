package com.valuewith.tweaver.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RedirectUrlType {
  GROUP_DETAIL("/group/list/"), // 그룹 상세페이지 - groupId를 넣어서 보내줌
  MY_PAGE("/mylounge"); // 마이페이지 - 내 모집 화면 url

  private final String url;
}
