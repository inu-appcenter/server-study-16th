package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.todo.ReadTodoRes;
import com.appcenter.practice.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<ReadTodoRes>>> getTodoList(){
        return ResponseEntity.ok(CommonResponse.of(TODO_FOUND.getMessage(), todoService.getTodoList()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommonResponse<ReadTodoRes>> getTodo(@PathVariable Long id){
        return ResponseEntity.ok(CommonResponse.of(TODO_FOUND.getMessage(),todoService.getTodo(id)));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Long>> addTodo(@RequestBody @Valid AddTodoReq reqDto){
        return ResponseEntity
                .status(TODO_CREATE.getStatus())
                .body(CommonResponse.of(TODO_CREATE.getMessage(),todoService.saveTodo(reqDto)));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CommonResponse<Long>> updateTodo(@PathVariable Long id, @RequestBody @Valid UpdateTodoReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.of(TODO_UPDATE.getMessage(),todoService.updateTodo(id,reqDto)));
    }

    @PatchMapping(value = "/{id}/complete")
    public ResponseEntity<CommonResponse<Long>> completeTodo(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of(TODO_UPDATE.getMessage(),todoService.completeTodo(id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommonResponse<Long>> deleteTodo(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of(TODO_DELETE.getMessage(),todoService.deleteTodo(id)));
    }

}
