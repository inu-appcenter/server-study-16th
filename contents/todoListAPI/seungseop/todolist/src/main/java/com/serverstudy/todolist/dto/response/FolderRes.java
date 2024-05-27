package com.serverstudy.todolist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "폴더 응답 DTO")
@Getter
public class FolderRes {

    @Schema(title = "폴더 Id", description = "폴더 Id", example = "1")
    private final long folderId;

    @Schema(title = "폴더명", description = "폴더명", example = "과제 폴더")
    private final String name;

    @Schema(title = "투두 개수", description = "폴더에 포함된 투두 개수", example = "3")
    private final int todoCount;

    @Builder
    private FolderRes(long folderId, String name, int todoCount) {
        this.folderId = folderId;
        this.name = name;
        this.todoCount = todoCount;
    }
}
