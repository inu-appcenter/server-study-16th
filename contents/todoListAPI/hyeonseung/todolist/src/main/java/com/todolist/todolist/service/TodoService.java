package com.todolist.todolist.service;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.domain.Todo;
import com.todolist.todolist.dto.todo.TodoMapper;
import com.todolist.todolist.dto.todo.TodoRequestDto;
import com.todolist.todolist.dto.todo.TodoResponseDto;
import com.todolist.todolist.repository.MemberRepository;
import com.todolist.todolist.repository.TodoRepository;
import com.todolist.todolist.validators.BaseException;
import com.todolist.todolist.validators.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public TodoResponseDto add(Long id, TodoRequestDto requestDto) {
        Todo todo = TodoMapper.INSTANCE.toEntity(requestDto);
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        todo.matchMember(member);

        todoRepository.save(todo);

        return TodoMapper.INSTANCE.toDto(todo);
    }

    // 2. Todo 수정
    public TodoResponseDto update(Long memberId, Long todoId, TodoRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ID));
        // 회원과 할일 간에 -> 관계가 매핑되어 있지 않음
        List<Todo> todos = todoRepository.findAllByMember(member);
        if (todos.isEmpty()) {
            throw new BaseException(ErrorCode.NOT_MATCH_TODO_ID);
        }


        Todo targetTodo = todos.stream()
                .filter(todo -> todo.getId().equals(todoId))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_MATCH_TODO_ID));

        targetTodo.updateTitle(requestDto.getTitle());
        targetTodo.updateContents(requestDto.getContents());
        targetTodo.updateIsCompleted(requestDto.getIsCompleted());
        targetTodo.updateDueAt(requestDto.getDueAt());
        todoRepository.save(targetTodo);

        return TodoMapper.INSTANCE.toDto(targetTodo);
    }

    // 3. Todo 전체 조회
    public List<TodoResponseDto> searchAll() {
        List<Todo> todos = todoRepository.findAll();

        return todos.stream()
                .map(TodoMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    // 4. Todo 리스트 조회 (memberId로 조회)
    public List<TodoResponseDto> searchById(Long id) {
        // 회원찾기
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ID));
        // 회원으로
        List<Todo> todos = todoRepository.findAllByMember(member);
        if (todos.isEmpty()) {
            throw new BaseException(ErrorCode.NOT_MATCH_TODO_ID);
        }

        return todos.stream()
                .map(TodoMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    // 5. Todo 삭제
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

}