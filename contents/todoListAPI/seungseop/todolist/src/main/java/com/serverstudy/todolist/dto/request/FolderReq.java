package com.serverstudy.todolist.dto.request;

import com.serverstudy.todolist.domain.Folder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface FolderReq {

    @Schema(description = "폴더 생성 요청 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class FolderPost {

        @Schema(title = "폴더명", description = "-,_가 아닌 특수문자를 제외한, 공백으로 시작하지 않는 문자 입력",
                example = "과제 폴더")
        @NotBlank(message = "공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_][ㄱ-ㅎ가-힣a-zA-Z0-9-_\\s]*$",
                 message = "공백으로 시작해선 안되며, -_을 제외한 특수문자를 사용할 수 없습니다.")
        private String name;

        public Folder toEntity(long userId) {
            return Folder.builder()
                    .name(name)
                    .userId(userId)
                    .build();
        }
    }

    @Schema(description = "폴더 변경 요청 DTO")
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    class FolderPatch {

        @Schema(title = "폴더명", description = "-,_가 아닌 특수문자를 제외한, 공백으로 시작하지 않는 문자 입력",
                example = "스터디 폴더")
        @NotBlank(message = "공백으로 입력할 수 없습니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_][ㄱ-ㅎ가-힣a-zA-Z0-9-_\\s]*$",
                 message = "공백으로 시작해선 안되며, -_을 제외한 특수문자를 사용할 수 없습니다.")
        private String name;
    }
}
