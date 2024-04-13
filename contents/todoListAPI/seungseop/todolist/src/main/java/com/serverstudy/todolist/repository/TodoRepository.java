package com.serverstudy.todolist.repository;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Progress;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findAllByUserAndProgressAndIsDeletedOrderByDeadlineAscPriorityAsc(User user, Progress progress, Boolean isDeleted);
    List<Todo> findAllByFolderAndProgressAndIsDeletedOrderByDeadlineAscPriorityAsc(Folder folder, Progress progress, Boolean isDeleted);
}
