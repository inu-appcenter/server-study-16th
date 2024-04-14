package com.serverstudy.todolist.domain;

import lombok.Getter;

@Getter
public enum Progress {
    Todo, Doing, Done;

    public static Progress getProgress(String progress) {
        return Progress.valueOf(progress);  // 해당하는 enum이 없으면 IllegalArgumentException 예외 발생
    }
}
