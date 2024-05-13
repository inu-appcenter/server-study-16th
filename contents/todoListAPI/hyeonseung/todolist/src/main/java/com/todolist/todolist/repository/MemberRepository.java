package com.todolist.todolist.repository;

import com.todolist.todolist.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findById(Long id);

    boolean existsByLoginId(String LoginId);


}
