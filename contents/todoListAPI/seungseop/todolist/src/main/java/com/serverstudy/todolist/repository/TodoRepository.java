package com.serverstudy.todolist.repository;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoCustomRepository {

    int countByFolder(Folder folder);

    List<Todo> findAllByIsDeletedOrderByDeletedTimeAsc (boolean isDeleted);

    List<Todo> findAllByFolder(Folder folder);

    List<Todo> findAllByUserId(long userId);
}
