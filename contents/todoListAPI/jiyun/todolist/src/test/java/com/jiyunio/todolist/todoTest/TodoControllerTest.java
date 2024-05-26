package com.jiyunio.todolist.todoTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiyunio.todolist.todo.TodoRepository;
import com.jiyunio.todolist.todo.TodoService;
import com.jiyunio.todolist.todo.dto.CreateTodoDTO;
import com.jiyunio.todolist.todo.dto.GetTodoDTO;
import com.jiyunio.todolist.todo.dto.UpdateTodoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @Test
    @DisplayName("Todo 생성 테스트")
    void CreateTodoTest() throws Exception {
        //given
        CreateTodoDTO createTodoDTO = CreateTodoDTO.builder()
                .content("안녕")
                .category("약속")
                .writeDate(LocalDate.now())
                .setDate(LocalDate.of(2024, 5, 21))
                .build();
        String body = mapper.writeValueAsString(createTodoDTO);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/todo/{memberId}", 1L)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Todo 조회 테스트")
    void getTodoTest() throws Exception {
        //given
        List<GetTodoDTO> getTodo = new ArrayList<>();

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/todo/{todoId}", 1L))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Todo 수정 테스트")
    void UpdateTodoTest() throws Exception {
        //given
        UpdateTodoDTO updateTodoDTO = UpdateTodoDTO.builder()
                .content("하이")
                .category("약속")
                .checked(true)
                .writeDate(LocalDate.now())
                .setDate(LocalDate.now())
                .build();

        String body = mapper.writeValueAsString(updateTodoDTO);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/todo/{todoId}", 1L)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Todo 삭제 테스트")
    void DeleteTodoTest() throws Exception {
        //given
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/todo/{todoId}", 1L))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }
}
