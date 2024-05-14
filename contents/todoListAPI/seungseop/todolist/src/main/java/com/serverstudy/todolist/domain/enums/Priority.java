package com.serverstudy.todolist.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Priority {
    NONE, PRIMARY, SECONDARY, TERTIARY;

    public static Priority getPriority(String inputPriority) {

        return Arrays.stream(Priority.values())
                .filter(priority -> priority.name().equals(inputPriority))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요."));
    }
}
