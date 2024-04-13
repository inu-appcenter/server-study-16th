package com.serverstudy.todolist.domain;

import lombok.Getter;

@Getter
public enum Priority {

    PRIMARY("매우 중요"),
    SECONDARY("중요"),
    TERTIARY("보통");

    private final String name;

    Priority (String name) {
        this.name = name;
    }
}
