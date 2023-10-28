package com.valuewith.tweaver.constants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GroupStatus {
    OPEN("모집중"),
    CLOSE("마감");

    private final String description;
}
