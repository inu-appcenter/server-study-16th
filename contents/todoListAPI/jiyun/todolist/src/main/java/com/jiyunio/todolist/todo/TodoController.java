package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.ResponseDTO;
import com.jiyunio.todolist.todo.dto.CreateTodoDTO;
import com.jiyunio.todolist.todo.dto.GetTodoDTO;
import com.jiyunio.todolist.todo.dto.UpdateTodoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Tag(name = "Todo", description = "todo API")
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/{memberId}")
    @Operation(summary = "todo 생성", description = "todo checked 기본 값 = False")
    public ResponseEntity<?> createTodo(@Parameter(description = "member의 id") @PathVariable Long memberId, @Valid @RequestBody CreateTodoDTO createTodo) {
        todoService.createTodo(memberId, createTodo);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("Todo 생성 완료")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "todo 조회")
    public List<GetTodoDTO> getTodo(@Parameter(description = "member의 id") @PathVariable Long memberId) {
        return todoService.getTodo(memberId);
    }

    @PutMapping("/{todoId}")
    @Operation(summary = "todo 수정")
    public ResponseEntity<?> updateTodo(@Parameter(description = "todo의 id") @PathVariable Long todoId, @Valid @RequestBody UpdateTodoDTO updateTodo) {
        todoService.updateTodo(todoId, updateTodo);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("Todo 수정 완료")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{todoId}")
    @Operation(summary = "todo 삭제")
    public ResponseEntity<ResponseDTO> deleteTodo(@Parameter(description = "todo의 id") @PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("Todo 삭제 완료")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }
}
