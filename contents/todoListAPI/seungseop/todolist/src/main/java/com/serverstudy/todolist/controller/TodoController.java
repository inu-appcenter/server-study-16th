package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.TodoDto;
import com.serverstudy.todolist.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> postTodo(@Valid @RequestBody TodoDto.PostReq postReq, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long todoId = todoService.create(postReq, userId);

        return ResponseEntity.ok(todoId);
    }

    @GetMapping
    public ResponseEntity<?> getTodosByRequirements(@Valid @ModelAttribute TodoDto.GetReq getReq, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        List<TodoDto.Response> responseList = todoService.findAllByConditions(getReq, userId);

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<?> putTodo(@Valid @RequestBody TodoDto.PutReq putReq, @PathVariable Long todoId, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long updatedTodoId = todoService.update(putReq, todoId, userId);

        return ResponseEntity.ok(updatedTodoId);
    }

    @PutMapping("/switch-progress/{todoId}")
    public ResponseEntity<?> switchTodoProgress(@PathVariable Long todoId, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long switchedTodoId = todoService.switchProgress(todoId, userId);

        return ResponseEntity.ok(switchedTodoId);
    }

    @PutMapping("/{todoId}/folder")
    public ResponseEntity<?> putTodoFolder(@RequestBody Long folderId, @PathVariable Long todoId, Long userId) {    // TODO folderId 받는 방식 수정이 필요할 듯

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long movedTodoId = todoService.moveFolder(folderId, todoId, userId);

        return ResponseEntity.ok(movedTodoId);
    }

    @PutMapping("/move-to-trash/{todoId}")
    public ResponseEntity<?> moveToTrashTodo(@PathVariable Long todoId, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long movedToTrashTodoId = todoService.moveToTrash(todoId, userId);

        return ResponseEntity.ok(movedToTrashTodoId);
    }
}
