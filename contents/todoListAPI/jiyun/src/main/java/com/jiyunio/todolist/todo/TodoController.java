package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.todo.dto.CreateTodoDto;
import com.jiyunio.todolist.todo.dto.TodoDto;
import com.jiyunio.todolist.todo.dto.UpdateTodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/{memberId}")
    public ResponseEntity<String> createTodo(@PathVariable Long memberId, @RequestBody CreateTodoDto createTodo) {
        todoService.createTodo(memberId, createTodo);
        return ResponseEntity.ok("Todo 생성 성공");
    }

    @GetMapping("/{memberId}")
    public List<TodoDto> getTodo(@PathVariable Long memberId) {
        List<TodoDto> todoList = todoService.getTodo(memberId);
        return todoList;
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<String> updateTodo(@PathVariable Long todoId, @RequestBody UpdateTodoDto updateTodo) {
        todoService.updateTodo(todoId, updateTodo);
        return ResponseEntity.ok("Todo 수정 성공");
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        return ResponseEntity.ok("Todo 삭제 성공");
    }
}
