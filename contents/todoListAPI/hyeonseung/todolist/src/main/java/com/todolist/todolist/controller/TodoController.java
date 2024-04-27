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

    @PostMapping("/{memberId}")
    public ResponseEntity<TodoResponseDto> addTodo(@PathVariable(value="memberId") Long id,@RequestBody TodoRequestDto request){
        TodoResponseDto responseDto = todoService.add(id, request);

        return new ResponseEntity<TodoResponseDto>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public List<TodoResponseDto> readAll(){
        List<TodoResponseDto>  responseDto_list = todoService.findAll();

        return responseDto_list;
       // return new ResponseEntity<TodoResponseDto>(responseDto_list,HttpStatus.OK);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<TodoResponseDto>> readMemberTodo (@PathVariable(value = "memberId") Long id){
        List<TodoResponseDto> responseDto = todoService.findById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
