package com.serverstudy.todolist.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoRes {

    private final Long id;

    private final String title;

    private final String description;

    private final LocalDateTime deadline;

    private final Integer priority;

    private final String progress;

    private final boolean isDeleted;

    private final Integer dateFromDelete;

    private final Long folderId;

    private final String folderName;

    @Builder
    private TodoRes(Long id, String title, String description, LocalDateTime deadline, Integer priority, String progress, boolean isDeleted, Integer dateFromDelete, Long folderId, String folderName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.progress = progress;
        this.isDeleted = isDeleted;
        this.dateFromDelete = dateFromDelete;
        this.folderId = folderId;
        this.folderName = folderName;
    }
}