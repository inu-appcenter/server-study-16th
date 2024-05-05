package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.ResponseDTO;
import com.jiyunio.todolist.todo.dto.CreateTodoDTO;
import com.jiyunio.todolist.todo.dto.GetTodoDTO;
import com.jiyunio.todolist.todo.dto.UpdateTodoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping("/{memberId}")
    public ResponseEntity<?> createTodo(@PathVariable Long memberId, @Valid @RequestBody CreateTodoDTO createTodo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ResponseDTO> responseDTOList = returnBindingResult(bindingResult);
            return new ResponseEntity<>(responseDTOList, HttpStatus.BAD_REQUEST);
        }
        todoService.createTodo(memberId, createTodo);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("Todo 생성 완료")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public List<GetTodoDTO> getTodo(@PathVariable Long memberId) {
        return todoService.getTodo(memberId);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<?> updateTodo(@PathVariable Long todoId, @Valid @RequestBody UpdateTodoDTO updateTodo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ResponseDTO> responseDTOList = returnBindingResult(bindingResult);
            return new ResponseEntity<>(responseDTOList, HttpStatus.BAD_REQUEST);
        }

        todoService.updateTodo(todoId, updateTodo);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("Todo 수정 완료")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<ResponseDTO> deleteTodo(@PathVariable Long todoId) {
        todoService.deleteTodo(todoId);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("Todo 삭제 완료")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }

    public List<ResponseDTO> returnBindingResult(BindingResult bindingResult) {
        List<FieldError> list = bindingResult.getFieldErrors();
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        for (FieldError error : list) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .msg(error.getDefaultMessage())
                    .build();
            responseDTOList.add(responseDTO);
        }
        return responseDTOList;
    }
}
