package com.todolist.todolist.controller;

import com.todolist.todolist.dto.todo.TodoRequestDto;
import com.todolist.todolist.dto.todo.TodoResponseDto;
import com.todolist.todolist.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("api/todo")
@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
  //  private final ErrorMessageHandler messageHandler;


    @Operation(summary = "Todo 추가")
    @PostMapping("/{memberId}")
    public ResponseEntity<TodoResponseDto> addTodo(@PathVariable Long memberId, @RequestBody @Valid TodoRequestDto request){

        TodoResponseDto responseDto = todoService.add(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Todo 전체 목록 조회")
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> readAll(){
        List<TodoResponseDto>  responseDto = todoService.searchAll();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원별 Todo 목록 조회 ")
    @GetMapping("/{memberId}")
    public ResponseEntity<List<TodoResponseDto>> readMemberTodo (@PathVariable Long memberId){
        List<TodoResponseDto> responseDto = todoService.searchById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "Todo 수정")
    @PutMapping("/{memberId}/{todoId}")
    public ResponseEntity<TodoResponseDto> update(@PathVariable Long memberId, @PathVariable Long todoId, @RequestBody @Valid TodoRequestDto request){

        TodoResponseDto responseDto = todoService.update(memberId,todoId,request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "Todo 삭제")
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long todoId){

        todoService.delete(todoId);
        return ResponseEntity.noContent().build();
    }

}