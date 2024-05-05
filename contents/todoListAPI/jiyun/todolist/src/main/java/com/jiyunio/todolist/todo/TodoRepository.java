package com.jiyunio.todolist.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByMemberId(Long memberId);

    Optional<Todo> findById(Long todoId);
}
