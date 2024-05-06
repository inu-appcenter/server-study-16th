package com.serverstudy.todolist.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Priority {

    PRIMARY(1),
    SECONDARY(2),
    TERTIARY(3);

    private final int number;

    Priority (int number) {
        this.number = number;
    }

    public static Priority getPriority(int inputNumber) {
        return Arrays.stream(Priority.values())
                .filter(priority -> priority.number == inputNumber)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 이름의 Priority를 찾을 수 없습니다"));
    }
}
