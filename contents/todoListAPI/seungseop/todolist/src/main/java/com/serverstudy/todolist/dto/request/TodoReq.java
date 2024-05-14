package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.common.Enum;
import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;
import jakarta.validation.constraints.NotEmpty;
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

        @NotEmpty(message = "제목을 작성해주세요.")
        private String title;

        private String description;

        private LocalDateTime deadline;

        @NotNull(message = "Priority 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        @Enum(message = "Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요.", enumClass = Priority.class, ignoreCase = true)
        private String priority;

        @NotNull(message = "Progress 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        @Enum(message = "Progress 값이 잘못 되었습니다. 올바른 값을 입력해주세요.", enumClass = Progress.class, ignoreCase = true)
        private String progress;

        private Long folderId;

        public Todo toEntity(long userId, Folder folder) {
            return Todo.builder()
                    .title(title == null ? "" : title)
                    .description(description == null ? "" : description)
                    .deadline(deadline)
                    .priority(Priority.valueOf(priority))
                    .progress(Progress.valueOf(progress))
                    .userId(userId)
                    .folder(folder)
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoGet {

        @Enum(message = "Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요.", enumClass = Priority.class, ignoreCase = true)
        private String priority;

        @Enum(message = "Progress 값이 잘못 되었습니다. 올바른 값을 입력해주세요.", enumClass = Progress.class, ignoreCase = true)
        private String progress;

        @NotNull(message = "idDeleted 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        private Boolean isDeleted;

        private Long folderId;

        public Priority getPriority() {
            return (priority == null) ? null : Priority.valueOf(priority);
        }

        public Progress getProgress() {
            return (progress == null) ? null : Progress.valueOf(progress);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoPut {

        @NotEmpty(message = "제목을 작성해주세요.")
        private String title;

        private String description;

        private LocalDateTime deadline;

        @NotNull(message = "Priority 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        @Enum(message = "Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요.", enumClass = Priority.class, ignoreCase = true)
        private String priority;

        @NotNull(message = "Progress 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        @Enum(message = "Progress 값이 잘못 되었습니다. 올바른 값을 입력해주세요.", enumClass = Progress.class, ignoreCase = true)
        private String progress;

        private Long folderId;

        public String getTitle() {
            return title == null ? "" : title;
        }

        public String getDescription() {
            return description == null ? "" : description;
        }

        public Priority getPriority() {
            return Priority.valueOf(priority);
        }

        public Progress getProgress() {
            return Progress.valueOf(progress);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoFolderPatch {

        private Long folderId;
    }

}
