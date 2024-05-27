package com.serverstudy.todolist.repository;

import com.serverstudy.todolist.domain.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByUserIdOrderByNameAsc(long userId);
    List<Folder> findAllByUserId(long userId);
    Boolean existsByNameAndUserId(String name, long userId);
}
