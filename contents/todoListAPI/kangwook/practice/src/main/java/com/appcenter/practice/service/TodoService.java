package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.todo.ReadTodoRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.repository.TodoRepository;
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


    public List<ReadTodoRes> getTodoList(){
        return todoRepository.findAll().stream()
                .map(ReadTodoRes::from)
                .collect(Collectors.toList());
    }

    public ReadTodoRes getTodo(Long id){
        Todo todo=findByTodoId(id);
        return ReadTodoRes.from(todo);
    }


    @Transactional
    public Long saveTodo(AddTodoReq reqDto){
        Member member=memberRepository.findByEmail(reqDto.getEmail())
                .orElseThrow(()->new CustomException(StatusCode.MEMBER_NOT_EXIST));
        return todoRepository.save(reqDto.toEntity(member)).getId();
    }

    @Transactional
    public Long updateTodo(Long id,UpdateTodoReq reqDto){
        Todo todo=findByTodoId(id);
        todo.changeContent(reqDto.getContent());
        return id;
    }

    @Transactional
    public Long completeTodo(Long id){
        Todo todo=findByTodoId(id);
        todo.changeCompleted();
        return id;
    }

    @Transactional
    public Long deleteTodo(Long id){
        todoRepository.deleteById(id);
        return id;
    }

    private Todo findByTodoId(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(()->new CustomException(StatusCode.TODO_NOT_EXIST));
    }
}
