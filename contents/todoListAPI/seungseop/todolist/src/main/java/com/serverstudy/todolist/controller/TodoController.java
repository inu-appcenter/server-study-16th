package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.TodoReq.TodoGet;
import com.serverstudy.todolist.dto.request.TodoReq.TodoPost;
import com.serverstudy.todolist.dto.response.TodoRes;
import com.serverstudy.todolist.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.serverstudy.todolist.dto.request.TodoReq.TodoFolderPatch;
import static com.serverstudy.todolist.dto.request.TodoReq.TodoPut;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> postTodo(@Valid @RequestBody TodoPost todoPost, Long userId) {

        long todoId = todoService.create(todoPost, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(todoId);
    }

    @GetMapping
    public ResponseEntity<?> getTodosByRequirements(@Valid @ModelAttribute TodoGet todoGet, Long userId) {

        List<TodoRes> responseList = todoService.findAllByConditions(todoGet, userId);

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<?> putTodo(@Valid @RequestBody TodoPut todoPut, @PathVariable Long todoId, Long userId) {

        long updatedTodoId = todoService.update(todoPut, todoId, userId);

        return ResponseEntity.ok(updatedTodoId);
    }

    @PatchMapping("/{todoId}/progress")
    public ResponseEntity<?> switchTodoProgress(@PathVariable Long todoId, Long userId) {

        long switchedTodoId = todoService.switchProgress(todoId, userId);

        return ResponseEntity.ok(switchedTodoId);
    }

    @PatchMapping("/{todoId}/folder")
    public ResponseEntity<?> patchTodoFolder(@RequestBody TodoFolderPatch todoFolderPatch, @PathVariable Long todoId, Long userId) {

        long movedTodoId = todoService.moveFolder(todoFolderPatch.getFolderId(), todoId, userId);

        return ResponseEntity.ok(movedTodoId);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId, @NotNull Boolean restore, Long userId) {

        Long result = todoService.delete(todoId, restore, userId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
