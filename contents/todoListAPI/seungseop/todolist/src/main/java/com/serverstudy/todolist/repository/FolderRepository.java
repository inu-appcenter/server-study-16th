package com.serverstudy.todolist.repository;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByUserOrderByNameAsc(User user);
    boolean existsByUserAndName(User user, String name);
}
