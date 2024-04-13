package com.todolist.todolist.repository;

import com.todolist.todolist.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository  extends JpaRepository<Todo,Long> {
}
