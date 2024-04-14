package com.serverstudy.todolist.dto;

import com.serverstudy.todolist.domain.Folder;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface FolderDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class PostReq {

        @NotNull
        private String name;

        public Folder toEntity(long userId) {
            return Folder.builder()
                    .name(name)
                    .userId(userId)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class PutReq {

        @NotNull
        private String name;
    }

    @Getter
    class Response {

        private final long folderId;

        private final String name;

        private final int todoCount;

        @Builder
        private Response(long folderId, String name, int todoCount) {
            this.folderId = folderId;
            this.name = name;
            this.todoCount = todoCount;
        }
    }
}
