package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Priority;
import com.serverstudy.todolist.domain.Progress;
import com.serverstudy.todolist.domain.Todo;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public interface TodoReq {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoPost {

        private String title;

        private String description;

        private LocalDateTime deadline;

        private Integer priority;

        @NotNull
        private Progress progress;

        private Long folderId;

        public Todo toEntity(long userId, Folder folder) {
            return Todo.builder()
                    .title(title == null ? "" : title)
                    .description(description == null ? "" : description)
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
    class TodoGet {

        private Integer priority;

        private Progress progress;

        private Boolean isDeleted;

        private Long folderId;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoPut {

        private String title;

        private String description;

        private LocalDateTime deadline;

        private Integer priority;

        @NotNull
        private Progress progress;

        private Long folderId;

        public String getTitle() {
            return title == null ? "" : title;
        }

        public String getDescription() {
            return description == null ? "" : description;
        }

        public Priority getPriority() {
            return priority == null ? null : Priority.getPriority(priority);
        }

        public Progress getProgress() {
            return Progress.getProgress(progress.name());
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoFolderPatch {

        private Long folderId;
    }

}
