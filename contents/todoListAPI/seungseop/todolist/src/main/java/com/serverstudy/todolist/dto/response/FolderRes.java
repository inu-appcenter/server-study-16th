package com.serverstudy.todolist.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FolderRes {

    private final long folderId;

    private final String name;

    private final int todoCount;

    @Builder
    private FolderRes(long folderId, String name, int todoCount) {
        this.folderId = folderId;
        this.name = name;
        this.todoCount = todoCount;
    }
}
