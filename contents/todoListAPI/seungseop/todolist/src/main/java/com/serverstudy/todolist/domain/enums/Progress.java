package com.serverstudy.todolist.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
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
    @JsonCreator    // Enum Validation 을 위한 코드, enum 에 속하지 않으면 null 리턴
    public static Progress from(String inputProgress) {

        return Arrays.stream(Progress.values())
                .filter(progress -> progress.name().equals(inputProgress))
                .findAny()
                .orElse(null);
    }
}
