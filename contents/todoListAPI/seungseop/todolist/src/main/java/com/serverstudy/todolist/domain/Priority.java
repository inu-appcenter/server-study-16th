package com.serverstudy.todolist.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Priority {

    PRIMARY("매우 중요"),
    SECONDARY("중요"),
    TERTIARY("보통");

    private final String name;

    Priority (String name) {
        this.name = name;
    }

    public static Priority getPriority(String inputName) {
        return Arrays.stream(Priority.values())
                .filter(priority -> priority.name.equals(inputName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 이름의 Priority를 찾을 수 없습니다"));
    }
}
