package com.serverstudy.todolist.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Progress {
    TODO, DOING, DONE;

    public static Progress getProgress(String inputProgress) {

        return Arrays.stream(Progress.values())
                .filter(progress -> progress.name().equals(inputProgress))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Progress 값이 잘못 되었습니다. 올바른 값을 입력해주세요."));
    }
}
