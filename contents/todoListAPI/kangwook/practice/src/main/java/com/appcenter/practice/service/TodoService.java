package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.dto.reqeust.AddTodoReq;
import com.appcenter.practice.dto.reqeust.UpdateTodoReq;
import com.appcenter.practice.dto.response.ReadTodoRes;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.repository.TodoRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.appcenter.practice.domain.QTodo.todo;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;
    @Transactional(readOnly = true)
    public List<ReadTodoRes> getTodoList(){
        List<ReadTodoRes> todoList= queryFactory
                .select(Projections.constructor(ReadTodoRes.class,todo.id,todo.content,todo.completed))
                .from(todo)
                .fetch();
        return todoList;
    }

    public void saveTodo(String email, AddTodoReq reqDto){
        Member member=memberRepository.findByEmail(email).get();
        todoRepository.save(reqDto.toEntity(member));
    }

    public Long updateTodo(UpdateTodoReq reqDto){
        todoRepository.findById(reqDto.getId()).get().changeContent(reqDto.getContent());
        return reqDto.getId();
    }

    public Long completeTodo(Long id){
        todoRepository.findById(id).get().changeCompleted(true);
        return id;
    }
    public Long deleteTodo(Long id){
        todoRepository.deleteById(id);
        return id;
    }
}
