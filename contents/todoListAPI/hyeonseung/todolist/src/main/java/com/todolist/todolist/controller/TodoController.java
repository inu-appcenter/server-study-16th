package com.todolist.todolist.controller;

import com.todolist.todolist.dto.todo.TodoRequestDto;
import com.todolist.todolist.dto.todo.TodoResponseDto;
import com.todolist.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/todo")
@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    // 회원에 따른 Todo 추가
    @PostMapping("/{memberId}")
    public ResponseEntity<TodoResponseDto> addTodo(@PathVariable Long memberId,@RequestBody TodoRequestDto request){
        TodoResponseDto responseDto = todoService.add(memberId, request);

        return new ResponseEntity<TodoResponseDto>(responseDto, HttpStatus.CREATED);
    }

    // Todo 전체 목록 조회
    @GetMapping
    public List<TodoResponseDto> readAll(){
        List<TodoResponseDto>  responseDto = todoService.searchAll();

        return responseDto;
       // return new ResponseEntity<TodoResponseDto>(responseDto_list,HttpStatus.OK);
    }

    // 회원-> Todo 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<List<TodoResponseDto>> readMemberTodo (@PathVariable Long memberId){
        List<TodoResponseDto> responseDto = todoService.searchById(memberId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // Todo 수정
    @PatchMapping("/{memberId}/{todoId}")
    public ResponseEntity<TodoResponseDto> update(@PathVariable Long memberId, @PathVariable Long todoId, @RequestBody TodoRequestDto request){
        TodoResponseDto responseDto = todoService.update(memberId,todoId,request);

        return ResponseEntity.status(200).body(responseDto);
    }

    // Todo 단일 삭제
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long todoId){
        todoService.delete(todoId);
        return ResponseEntity.noContent().build();
    }
}
