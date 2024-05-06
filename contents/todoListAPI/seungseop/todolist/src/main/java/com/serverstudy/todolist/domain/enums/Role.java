package com.serverstudy.todolist.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @JsonCreator    // Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    public static Role getRole(String inputRole) {
        return Arrays.stream(values())
                .filter(role -> role.getRole().equals(inputRole))
                .findAny()
                .orElse(null);
    }
}
