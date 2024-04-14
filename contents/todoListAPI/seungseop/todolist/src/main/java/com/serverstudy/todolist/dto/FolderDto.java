package com.serverstudy.todolist.dto;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.User;
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

        public Folder toEntity(User user) {
            return Folder.builder()
                    .name(name)
                    .user(user)
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

        private final String name;

        private final int todoCount;

        @Builder
        private Response(String name, int todoCount) {
            this.name = name;
            this.todoCount = todoCount;
        }
    }
}
