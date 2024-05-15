package com.serverstudy.todolist.dto.response;

import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "투두 응답 DTO")
@Getter
public class TodoRes {

    @Schema(title = "투두 Id", description = "투두 Id", example = "1")
    private final Long id;

    @Schema(title = "제목", description = "제목", example = "컴퓨터공학개론 레포트")
    private final String title;

    @Schema(title = "설명", description = "설명", example = "주제: 컴퓨터공학이란 무엇인가?, 분량: 3장 이상")
    private final String description;

    @Schema(title = "마감 기한", description = "마감 기한", example = "2024-05-15T23:59:00Z")
    private final LocalDateTime deadline;

    @Schema(title = "우선 순위", description = "우선 순위", example = "PRIMARY")
    private final Priority priority;

    @Schema(title = "진행 상황", description = "진행 상황", example = "DONE")
    private final Progress progress;

    @Schema(title = "임시 삭제 여부", description = "임시 삭제 여부", example = "true")
    private final boolean isDeleted;

    @Schema(title = "임시 삭제 이후 지난 날짜", description = "임시 삭제로부터 지나간 날짜 (null 또는 0부터 최대 30일)", example = "12")
    private final Integer dateFromDelete;

    @Schema(title = "폴더 Id", description = "폴더 Id", example = "1")
    private final Long folderId;

    @Schema(title = "폴더명", description = "폴더명", example = "과제 폴더")
    private final String folderName;

    @Builder
    private TodoRes(Long id, String title, String description, LocalDateTime deadline, Priority priority, Progress progress, boolean isDeleted, Integer dateFromDelete, Long folderId, String folderName) {
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