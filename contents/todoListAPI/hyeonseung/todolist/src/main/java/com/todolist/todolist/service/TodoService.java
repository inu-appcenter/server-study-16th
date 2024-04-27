package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.domain.Todo;
import com.todolist.todolist.dto.todo.TodoMapper;
import com.todolist.todolist.dto.todo.TodoRequestDto;
import com.todolist.todolist.dto.todo.TodoResponseDto;
import com.todolist.todolist.repository.MemberRepository;
import com.todolist.todolist.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    /*
    1. Todo 추가
    2. Todo 수정
    3. Todo 전체 조회
    4. Todo 리스트 조회 (memberId로 조회)
    5. Todo 삭제
    6. Todo 전체 삭제
     */

    // 1. Todo 추가
    public TodoResponseDto add(Long id,TodoRequestDto requestDto) {

        Todo todo = TodoMapper.INSTANCE.toEntity(requestDto);
        Member member = memberRepository.findById(id)
                        .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        todo.matchMember(member);

        todoRepository.save(todo);

        return TodoMapper.INSTANCE.toDto(todo);
    }

    // 2. Todo 수정
    public TodoResponseDto update(Long id, TodoRequestDto requestDto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND));
        todo.updateTitle(requestDto.getTitle());
        todo.updateContents(requestDto.getContents());
        todo.updateIsCompleted(requestDto.getIsCompleted());
        todo.updateDueAt(requestDto.getDueAt());
        todoRepository.save(todo);

        return TodoMapper.INSTANCE.toDto(todo);
    }

    // 3. Todo 전체 조회
    public List<TodoResponseDto> findAll() {
        List<Todo> todo = todoRepository.findAll();
        List<TodoResponseDto> todoList = new ArrayList<>();
        for (Todo todo_one : todo){
            TodoResponseDto responseDto = TodoMapper.INSTANCE.toDto(todo_one);
            todoList.add(responseDto);
        }
        return todoList;
    }

    // 4. Todo 리스트 조회 (memberId로 조회)
    public List<TodoResponseDto> findById(Long id){
        // 회원찾기
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // 회원으로
        List<Todo> todo  = todoRepository.findByMember(member);
        List<TodoResponseDto> todoList = new ArrayList<>();
        for(Todo todo_one : todo){
            TodoResponseDto responseDto = TodoMapper.INSTANCE.toDto(todo_one);
            todoList.add(responseDto);
        }

        return todoList;
    }
//    //    5. Todo 삭제
//    //    6. Todo 전체 삭제

}