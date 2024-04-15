package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.AddTodoReq;
import com.appcenter.practice.dto.reqeust.UpdateTodoReq;
import com.appcenter.practice.dto.response.ReadTodoRes;
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
    public ResponseEntity<List<ReadTodoRes>> getTodoList(){
        return ResponseEntity.ok(todoService.getTodoList());
    }

    @PostMapping
    public ResponseEntity<?> addTodo(@RequestBody AddTodoReq reqDto){
        return ResponseEntity
                .status(201)
                .body(todoService.saveTodo(reqDto));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, UpdateTodoReq reqDto){
        return ResponseEntity.ok(todoService.updateTodo(id,reqDto));
    }

    @PatchMapping(value = "/{id}/complete")
    public ResponseEntity<?> completeTodo(@PathVariable Long id){
        return ResponseEntity.ok(todoService.completeTodo(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id){
        return ResponseEntity.ok(todoService.deleteTodo(id));
    }

}
