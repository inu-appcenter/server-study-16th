package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.domain.Folder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface FolderReq {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class FolderPost {

        @NotBlank(message = "폴더명은 공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_][ㄱ-ㅎ가-힣a-z0-9-_\\s]*$",
                message = "폴더명은 공백으로 시작해선 안되며, -_을 제외한 특수문자를 사용할 수 없습니다.")
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

        @NotBlank
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_][ㄱ-ㅎ가-힣a-z0-9-_\\s]*$",
                message = "폴더명은 공백으로 시작해선 안되며, -_을 제외한 특수문자를 사용할 수 없습니다.")
        private String name;
    }
}
