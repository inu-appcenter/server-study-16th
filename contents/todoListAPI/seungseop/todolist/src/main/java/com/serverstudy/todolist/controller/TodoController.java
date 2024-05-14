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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.serverstudy.todolist.dto.request.TodoReq.TodoFolderPatch;
import static com.serverstudy.todolist.dto.request.TodoReq.TodoPut;

@Validated
@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<Long> postTodo(@Valid @RequestBody TodoPost todoPost, @NotNull Long userId) {

        Long todoId = todoService.create(todoPost, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(todoId);
    }

    @GetMapping
    public ResponseEntity<List<TodoRes>> getTodosByRequirements(@Valid @ModelAttribute TodoGet todoGet, @NotNull Long userId) {

        List<TodoRes> responseList = todoService.findAllByConditions(todoGet, userId);

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<Long> putTodo(@Valid @RequestBody TodoPut todoPut, @PathVariable Long todoId) {

        Long updatedTodoId = todoService.update(todoPut, todoId);

        return ResponseEntity.ok(updatedTodoId);
    }

    @PatchMapping("/{todoId}/progress")
    public ResponseEntity<Long> switchTodoProgress(@PathVariable Long todoId) {

        Long switchedTodoId = todoService.switchProgress(todoId);

        return ResponseEntity.ok(switchedTodoId);
    }

    @PatchMapping("/{todoId}/folder")
    public ResponseEntity<Long> patchTodoFolder(@RequestBody TodoFolderPatch todoFolderPatch, @PathVariable Long todoId) {

        Long movedTodoId = todoService.moveFolder(todoFolderPatch.getFolderId(), todoId);

        return ResponseEntity.ok(movedTodoId);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Long> deleteTodo(@PathVariable Long todoId, @NotNull Boolean restore) {

        Long result = todoService.delete(todoId, restore);

        return (result != null)
                ? ResponseEntity.status(HttpStatus.OK).body(result)
                : ResponseEntity.noContent().build();
    }
}
