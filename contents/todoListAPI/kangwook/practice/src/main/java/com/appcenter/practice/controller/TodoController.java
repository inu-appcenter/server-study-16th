package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.todo.ReadTodoRes;
import com.appcenter.practice.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<ReadTodoRes>>> getTodoList(){
        return ResponseEntity.ok(CommonResponse.of("Ok","TodoList 조회 성공",todoService.getTodoList()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommonResponse<ReadTodoRes>> getTodo(@PathVariable Long id){
        return ResponseEntity.ok(CommonResponse.of("Ok","Todo 조회 성공",todoService.getTodo(id)));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Long>> addTodo(@RequestBody AddTodoReq reqDto){
        return ResponseEntity
                .status(201)
                .body(CommonResponse.of("Created","Todo가 생성 성공.",todoService.saveTodo(reqDto)));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CommonResponse<Long>> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","Todo 수정 성공",todoService.updateTodo(id,reqDto)));
    }

    @PatchMapping(value = "/{id}/complete")
    public ResponseEntity<CommonResponse<Long>> completeTodo(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","Todo 완료 성공",todoService.completeTodo(id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommonResponse<Long>> deleteTodo(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","Todo 삭제 성공",todoService.deleteTodo(id)));
    }

}
