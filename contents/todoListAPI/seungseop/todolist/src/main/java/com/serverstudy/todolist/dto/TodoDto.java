package com.serverstudy.todolist.dto;

import com.serverstudy.todolist.domain.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

public interface TodoDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class PostReq {

        private String title;

        private String description;

        private LocalDateTime deadline;

        private Integer priority;

        @NotNull
        private Progress progress;

        private Long folderId;

        public Todo toEntity(long userId, Folder folder) {
            return Todo.builder()
                    .title(title)
                    .description(description)
                    .deadline(deadline)
                    .priority(priority == null ? null : Priority.getPriority(priority))
                    .progress(Progress.getProgress(progress.name()))
                    .userId(userId)
                    .folder(folder)
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class GetReq {

        private Integer priority;

        private Progress progress;

        private Boolean isDeleted;

        private Long folderId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class PutReq {

        private String title;

        private String description;

        private LocalDateTime deadline;

        private Integer priority;

        @NotNull
        private Progress progress;

        private Long folderId;

    }

    @Getter
    class Response {

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
        private Response(Long id, String title, String description, LocalDateTime deadline, Integer priority, String progress, boolean isDeleted, Integer dateFromDelete, Long folderId, String folderName) {
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
}
