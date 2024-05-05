package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.domain.Folder;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface FolderReq {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class FolderPost {

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
    class FolderPatch {

        @NotNull
        private String name;
    }
}
