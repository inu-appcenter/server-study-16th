package com.serverstudy.todolist.repository;

import com.serverstudy.todolist.domain.Priority;
import com.serverstudy.todolist.domain.Progress;
import com.serverstudy.todolist.domain.Todo;

import java.util.List;

public interface TodoCustomRepository {

    List<Todo> findAllByConditions(Long folderId, Long userId, Priority priority, Progress progress, boolean isDeleted);
}
