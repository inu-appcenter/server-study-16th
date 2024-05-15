package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.common.ExampleData;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPost;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.exception.ErrorResponse;
import com.serverstudy.todolist.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Folder", description = "Folder API 입니다.")
@Validated
@RestController
@RequestMapping("/api/folder")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FolderController implements ExampleData {

    private final FolderService folderService;

    @Operation(summary = "폴더 생성", description = "새로운 폴더를 생성합니다.", parameters = {
            @Parameter(name = "userId", description = "유저 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "201", description = "폴더 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "USER_NOT_FOUND", value = USER_NOT_FOUND_DATA),
            })),
            @ApiResponse(responseCode = "409", description = "중복된 폴더 이름", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Long> postFolder(@Valid @RequestBody FolderPost folderPost, @NotNull Long userId) {  // @RequestParam만 붙여도 null 값 입력 시 예외 발생

        Long folderId = folderService.create(folderPost, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(folderId);
    }

    @Operation(summary = "폴더 목록 조회", description = "폴더 목록을 가져옵니다.", parameters = {
            @Parameter(name = "userId", description = "유저 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "폴더 목록 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            }))
    })
    @GetMapping
    public ResponseEntity<List<FolderRes>> getFoldersByUser(@NotNull Long userId) {

        List<FolderRes> responseList = folderService.getAllWithTodoCount(userId);

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "폴더 정보 수정", description = "해당 폴더의 정보를 수정합니다.", parameters = {
            @Parameter(name = "folderId", description = "폴더 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "폴더 정보 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "폴더가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "FOLDER_NOT_FOUND", value = FOLDER_NOT_FOUND_DATA),
            })),
            @ApiResponse(responseCode = "409", description = "중복된 폴더 이름", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{folderId}")
    public ResponseEntity<Long> patchFolder(@Valid @RequestBody FolderPatch folderPatch, @PathVariable Long folderId) {

        Long modifiedFolderId = folderService.modify(folderPatch, folderId);

        return ResponseEntity.ok(modifiedFolderId);
    }

    @Operation(summary = "폴더 삭제", description = "해당 폴더를 삭제합니다.", parameters = {
            @Parameter(name = "folderId", description = "폴더 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "204", description = "폴더 삭제 성공")
    })
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {

        folderService.delete(folderId);

        return ResponseEntity.noContent().build();
    }
}
