package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.AddTodoReq;
import com.appcenter.practice.dto.reqeust.UpdateTodoReq;
import com.appcenter.practice.dto.response.ReadTodoRes;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.repository.TodoRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;


    public List<ReadTodoRes> getTodoList(){
        return todoRepository.findAll().stream()
                .map(todo->ReadTodoRes.from(todo))
                .collect(Collectors.toList());
    }


    @Transactional
    public long saveTodo(AddTodoReq reqDto){
        Member member=memberRepository.findByEmail(reqDto.getEmail())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 유저입니다."));
        return todoRepository.save(reqDto.toEntity(member)).getId();
    }

    @Transactional
    public long updateTodo(Long id,UpdateTodoReq reqDto){
        Todo todo=todoRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 할일입니다."));
        todo.changeContent(reqDto.getContent());
        return id;
    }

    @Transactional
    public long completeTodo(Long id){
        Todo todo=todoRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 할일입니다."));
        todo.changeCompleted(true);
        return id;
    }

    @Transactional
    public long deleteTodo(Long id){
        todoRepository.deleteById(id);
        return id;
    }
}
