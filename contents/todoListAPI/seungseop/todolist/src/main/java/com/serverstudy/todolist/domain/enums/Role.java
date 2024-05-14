package com.serverstudy.todolist.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role getRole(String inputRole) {
        return Arrays.stream(values())
                .filter(role -> role.getRole().equals(inputRole))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Role 값이 잘못 되었습니다. 올바른 값을 입력해주세요."));
    }
}
