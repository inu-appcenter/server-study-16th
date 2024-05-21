package com.jiyunio.todolist.todoTest;

import com.jiyunio.todolist.todo.TodoRepository;
import com.jiyunio.todolist.todo.TodoService;
import com.jiyunio.todolist.todo.dto.CreateTodoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @Test
    @DisplayName("Todo 생성 테스트")
    void CreateTodoTest() {
        //given
        Long memberId = 2L;
        CreateTodoDTO createTodoDTO = CreateTodoDTO.builder()
                .content("코딩")
                .category("시험")
                .writeDate(LocalDate.now())
                .setDate(LocalDate.of(2024, 5, 28))
                .build();
        //when
        String userId = todoService.createTodo(memberId, createTodoDTO);

        //then
        assertThat(userId).isEqualTo("mxmdi");
    }
}
