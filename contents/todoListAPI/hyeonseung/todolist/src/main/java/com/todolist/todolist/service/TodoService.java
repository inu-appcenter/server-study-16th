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
import java.util.stream.Collectors;

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
    public TodoResponseDto update(Long memberId,Long todoId, TodoRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하는 회원이 없습니다."));
        // 회원과 할일 간에 -> 관계가 매핑되어 있지 않음
        List<Todo> todos = todoRepository.findByMember(member);

        Todo targetTodo = todos.stream()
                        .filter(todo-> todo.getId().equals(todoId))
                        .findFirst()
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"회원과 일치하는 TodoList가 없습니다."));

        targetTodo.updateTitle(requestDto.getTitle());
        targetTodo.updateContents(requestDto.getContents());
        targetTodo.updateIsCompleted(requestDto.isCompleted());
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

//        List<TodoResponseDto> todoList = new ArrayList<>();
//        for (Todo todo : todos){
//            TodoResponseDto responseDto = TodoMapper.INSTANCE.toDto(todo);
//            todoList.add(responseDto);
//        }
//        return todoList;
    }

    // 4. Todo 리스트 조회 (memberId로 조회)
    public List<TodoResponseDto> searchById(Long id){
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

    // 5. Todo 삭제
    public void delete(Long id){
        todoRepository.deleteById(id);
    }

//    // 6. Todo 전체 삭제
//    public void deleteAll(){
//        todoRepository.deleteAll();
//    }
}