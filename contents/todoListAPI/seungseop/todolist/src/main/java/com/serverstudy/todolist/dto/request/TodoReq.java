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

        @Enum(message = "Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요.")
        private Priority priority;

        @NotNull(message = "Progress 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        @Enum(message = "Progress 값이 잘못 되었습니다. 올바른 값을 입력해주세요.")
        private Progress progress;

        private Long folderId;

        public Todo toEntity(long userId, Folder folder) {
            return Todo.builder()
                    .title(title == null ? "" : title)
                    .description(description == null ? "" : description)
                    .deadline(deadline)
                    .priority(priority)
                    .progress(progress)
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

        @NotNull(message = "Progress 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        private String progress;

        private Boolean isDeleted;

        private Long folderId;

        public Priority getPriority() {
            return Priority.getPriority(priority);
        }

        public Progress getProgress() {
            return Progress.getProgress(progress);
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoPut {

        @NotEmpty(message = "제목을 작성해주세요.")
        private String title;

        private String description;

        private LocalDateTime deadline;

        @Enum(message = "Priority 값이 잘못 되었습니다. 올바른 값을 입력해주세요.")
        private Priority priority;

        @NotNull(message = "Progress 값은 비어있을 수 없습니다. 값을 입력해주세요.")
        @Enum(message = "Progress 값이 잘못 되었습니다. 올바른 값을 입력해주세요.")
        private Progress progress;

        private Long folderId;

        public String getTitle() {
            return title == null ? "" : title;
        }

        public String getDescription() {
            return description == null ? "" : description;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class TodoFolderPatch {

        private Long folderId;
    }

}
