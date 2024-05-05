package com.serverstudy.todolist.domain;

import lombok.Getter;

@Getter
public enum Progress {
    Todo, Doing, Done;

    public static Progress getProgress(String progress) {

        if (progress == null) {
            throw new IllegalArgumentException("progrss가 null 값이면 안됩니다.");
        }

        try {
            return Progress.valueOf(progress);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("부적절한 progress 값: " + progress);
        }
    }
}
