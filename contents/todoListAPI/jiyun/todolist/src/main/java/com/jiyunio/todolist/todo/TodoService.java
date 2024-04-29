package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import com.jiyunio.todolist.todo.dto.CreateTodoDto;
import com.jiyunio.todolist.todo.dto.TodoDto;
import com.jiyunio.todolist.todo.dto.UpdateTodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    public void createTodo(Long memberId, CreateTodoDto createTodo) {
        Member member = memberRepository.findById(memberId).get();

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

    public List<TodoDto> getTodo(Long memberId) {
        List<Todo> todoList = todoRepository.findByMemberId(memberId);
        List<TodoDto> returnTodoList = new ArrayList<>();

        for (Todo todo :
                todoList) {
            TodoDto todoDto = TodoDto.builder()
                    .content(todo.getContent())
                    .category(todo.getCategory())
                    .writeDate(todo.getWriteDate())
                    .setDate(todo.getSetDate())
                    .checked(todo.getChecked())
                    .build();

            returnTodoList.add(todoDto);
        }
        return returnTodoList;
    }

    public void updateTodo(Long todoId, UpdateTodoDto updateTodo) { //Put
        Todo todo = todoRepository.findById(todoId).get();
        todo.updateTodo(updateTodo);
        todoRepository.save(todo);
    }

    public boolean deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
        return true;
    }
}
