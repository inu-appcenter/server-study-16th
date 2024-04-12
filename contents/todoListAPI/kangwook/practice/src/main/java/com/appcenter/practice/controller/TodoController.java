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

    @GetMapping(value = "/")
    public ResponseEntity<List<ReadTodoRes>> getTodoList(){
        return ResponseEntity.ok(todoService.getTodoList());
    }

    @PostMapping(value = "/")
    public ResponseEntity<AddTodoReq> addTodo(String email, @RequestBody AddTodoReq reqDto){
        todoService.saveTodo(email,reqDto);
        return ResponseEntity.ok(reqDto);
    }

    @PatchMapping(value = "/")
    public ResponseEntity<?> updateTodo(UpdateTodoReq reqDto){
        return ResponseEntity.ok(todoService.updateTodo(reqDto));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> completeTodo(@PathVariable Long id){
        return ResponseEntity.ok(todoService.completeTodo(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id){
        return ResponseEntity.ok(todoService.deleteTodo(id));
    }

}
