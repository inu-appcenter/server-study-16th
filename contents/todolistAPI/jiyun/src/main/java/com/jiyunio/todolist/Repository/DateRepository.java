package com.jiyunio.todolist.Repository;

import com.jiyunio.todolist.Domain.Date;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends JpaRepository<Date, Long> {
}
