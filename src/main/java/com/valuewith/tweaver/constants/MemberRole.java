package com.valuewith.tweaver.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberRole {

    /**
     * LEADER : LEADER(그룹장)
     * MEMBER : MEMBER(그룹원)
     **/
    LEADER("LEADER", "그룹장"),
    MEMBER("MEMBER", "그룹원");

    private final String code;
    private final String name;

}
