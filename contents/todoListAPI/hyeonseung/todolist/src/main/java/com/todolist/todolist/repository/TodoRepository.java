package com.todolist.todolist.repository;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository  extends JpaRepository<Todo,Long> {

    List<Todo> findAll ();
    Optional<Todo> findById(Long id);

    List<Todo> findAllByMember(Member member);


}
