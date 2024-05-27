package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.common.ExampleData;
import com.serverstudy.todolist.dto.request.TodoReq.TodoGet;
import com.serverstudy.todolist.dto.request.TodoReq.TodoPost;
import com.serverstudy.todolist.dto.response.TodoRes;
import com.serverstudy.todolist.exception.ErrorResponse;
import com.serverstudy.todolist.service.TodoService;
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

import static com.serverstudy.todolist.dto.request.TodoReq.TodoFolderPatch;
import static com.serverstudy.todolist.dto.request.TodoReq.TodoPut;

@Tag(name = "Todo", description = "Todo API 입니다.")
@Validated
@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TodoController implements ExampleData {

    private final TodoService todoService;

    @Operation(summary = "투두 생성", description = "새로운 투두를 생성합니다.", parameters = {
            @Parameter(name = "userId", description = "유저 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "201", description = "투두 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음 / 폴더가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "USER_NOT_FOUND", value = USER_NOT_FOUND_DATA),
                    @ExampleObject(name = "FOLDER_NOT_FOUND", value = FOLDER_NOT_FOUND_DATA),
            }))
    })
    @PostMapping
    public ResponseEntity<Long> postTodo(@Valid @RequestBody TodoPost todoPost, @NotNull Long userId) {

        Long todoId = todoService.create(todoPost, userId);


        return ResponseEntity.status(HttpStatus.CREATED).body(todoId);
    }

    @Operation(summary = "투두 목록 조회", description = "조건에 맞는 투두 목록을 가져옵니다.", parameters = {
            @Parameter(name = "userId", description = "유저 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "투두 목록 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            }))
    })
    @GetMapping
    public ResponseEntity<List<TodoRes>> getTodosByRequirements(@Valid @ModelAttribute TodoGet todoGet, @NotNull Long userId) {

        List<TodoRes> responseList = todoService.findAllByConditions(todoGet, userId);

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "투두 수정", description = "해당 투두를 수정합니다. 수정을 원치 않는 값은 조회한 값을 그대로 넣어주세요.", parameters = {
            @Parameter(name = "todoId", description = "투두 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "투두 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "투두가 존재하지 않음 / 폴더가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "TODO_NOT_FOUND", value = TODO_NOT_FOUND_DATA),
                    @ExampleObject(name = "FOLDER_NOT_FOUND", value = FOLDER_NOT_FOUND_DATA),
            }))
    })
    @PutMapping("/{todoId}")
    public ResponseEntity<Long> putTodo(@Valid @RequestBody TodoPut todoPut, @PathVariable Long todoId) {

        Long updatedTodoId = todoService.update(todoPut, todoId);

        return ResponseEntity.ok(updatedTodoId);
    }

    @Operation(summary = "투두 진행 상황 수정", description = "해당 투두의 진행 상황을 변경합니다. TODO -> DOING -> DONE -> TODO 순서로 바뀝니다. ", parameters = {
            @Parameter(name = "todoId", description = "투두 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "투두 진행 상황 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "투두가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "TODO_NOT_FOUND", value = TODO_NOT_FOUND_DATA),
            }))
    })
    @PatchMapping("/{todoId}/progress")
    public ResponseEntity<Long> switchTodoProgress(@PathVariable Long todoId) {

        Long switchedTodoId = todoService.switchProgress(todoId);

        return ResponseEntity.ok(switchedTodoId);
    }

    @Operation(summary = "투두 폴더 변경", description = "해당 투두의 폴더를 변경합니다. 미기입 시 폴더 없음으로 변경합니다.", parameters = {
            @Parameter(name = "todoId", description = "투두 id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "투두 폴더 변경 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "투두가 존재하지 않음 / 폴더가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "TODO_NOT_FOUND", value = TODO_NOT_FOUND_DATA),
                    @ExampleObject(name = "FOLDER_NOT_FOUND", value = FOLDER_NOT_FOUND_DATA),
            }))
    })
    @PatchMapping("/{todoId}/folder")
    public ResponseEntity<Long> patchTodoFolder(@RequestBody TodoFolderPatch todoFolderPatch, @PathVariable Long todoId) {

        Long movedTodoId = todoService.moveFolder(todoFolderPatch.getFolderId(), todoId);

        return ResponseEntity.ok(movedTodoId);
    }

    @Operation(summary = "투두 삭제", description = "해당 투두를 임시/영구 삭제합니다.", parameters = {
            @Parameter(name = "todoId", description = "투두 id", example = "1"),
            @Parameter(name = "restore", description = "임시 삭제 여부, true 시 임시로 삭제하고 30일 뒤 영구 삭제, false 시 즉시 영구 삭제", example = "true")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "투두 임시 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "204", description = "투두 영구 삭제 성공", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "투두가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "TODO_NOT_FOUND", value = TODO_NOT_FOUND_DATA),
            }))
    })
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Long> deleteTodo(@PathVariable Long todoId, @NotNull Boolean restore) {

        Long result = todoService.delete(todoId, restore);

        return (result != null)
                ? ResponseEntity.status(HttpStatus.OK).body(result)
                : ResponseEntity.noContent().build();
    }
}
