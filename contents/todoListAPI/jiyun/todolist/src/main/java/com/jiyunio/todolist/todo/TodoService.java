package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import com.jiyunio.todolist.todo.dto.CreateTodoDTO;
import com.jiyunio.todolist.todo.dto.GetTodoDTO;
import com.jiyunio.todolist.todo.dto.UpdateTodoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    public void createTodo(Long memberId, CreateTodoDTO createTodo) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                // 회원 존재 안함
                () -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_EXIST_MEMBER)
        );

        Todo todo = Todo.builder()
                .member(member)
                .content(createTodo.getContent())
                .category(createTodo.getCategory())
                .writeDate(createTodo.getWriteDate())
                .setDate(createTodo.getSetDate())
                .checked(false)
                .build();
        todoRepository.save(todo);
    }

    public List<GetTodoDTO> getTodo(Long memberId) {
        List<Todo> todoList = todoRepository.findByMemberId(memberId);
        List<GetTodoDTO> getTodoList = new ArrayList<>();

        for (Todo todo : todoList) {
            getTodoList.add(GetTodoDTO.builder()
                    .content(todo.getContent())
                    .category(todo.getCategory())
                    .writeDate(todo.getWriteDate())
                    .setDate(todo.getSetDate())
                    .checked(todo.getChecked())
                    .build());
        }
        return getTodoList;
    }

    public void updateTodo(Long todoId, UpdateTodoDTO updateTodo) {
        Todo todo = todoRepository.findById(todoId).get();
        todo.updateTodo(updateTodo);
        todoRepository.save(todo);
    }

    public boolean deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
        return true;
    }
}
