package com.serverstudy.todolist.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum Priority {

    NONE(null),
    PRIMARY(1),
    SECONDARY(2),
    TERTIARY(3);


    @JsonValue
    private final Integer number;

    Priority (Integer number) {
        this.number = number;
    }

    public static Priority getPriority(Integer inputNumber) {

        return Arrays.stream(Priority.values())
                .filter(priority -> Objects.equals(priority.number, inputNumber))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요."));
    }
    @JsonCreator    // Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    public static Priority from(Integer inputNumber) {

        return Arrays.stream(Priority.values())
                .filter(priority -> Objects.equals(priority.number, inputNumber))
                .findAny()
                .orElse(null);
    }
}
