package com.appcenter.practice.repository;

import com.appcenter.practice.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<ToDo,Long> {



}
