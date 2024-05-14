package com.serverstudy.todolist.repository;

import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;

import java.util.List;

public interface TodoCustomRepository {

    List<Todo> findAllByConditions(Long folderId, Long userId, Priority priority, Progress progress, Boolean isDeleted);
}
