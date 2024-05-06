package com.jiyunio.todolist.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long todoId);

    List<Category> findByMemberId(Long memberId);
}
