package com.serverstudy.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;
import com.serverstudy.todolist.dto.request.TodoReq.TodoGet;
import com.serverstudy.todolist.dto.request.TodoReq.TodoPost;
import com.serverstudy.todolist.dto.request.TodoReq.TodoPut;
import com.serverstudy.todolist.dto.response.TodoRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.exception.ErrorCode;
import com.serverstudy.todolist.service.TodoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TodoController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TodoService todoService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class postTodo_메서드는 {

        @Test
        void 투두를_생성하고_created_응답과_투두_기본키를_반환한다() throws Exception {
            // given
            Long userId = 1L;
            String todoPostJson = """
                    {
                       "title": "컴퓨터공학개론 레포트",
                       "description": "주제: 컴퓨터공학이란 무엇인가?, 분량: 3장 이상",
                       "deadline": "2024-05-15T23:59:00Z",
                       "priority": "NONE",
                       "progress": "TODO",
                       "folderId": 1
                     }
                    """;
            Long todoId = 1L;
            given(todoService.create(any(TodoPost.class), eq(userId))).willReturn(todoId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/todo?userId={userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoPostJson))
                    // then
                    .andExpect(status().isCreated())
                    .andExpect(content().string(todoId.toString()));
        }
        @Test
        void 만약_존재하지_않는_폴더_기본키가_주어지면_FOLDER_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long userId = 1L;
            String todoPostJson = """
                    {
                       "title": "컴퓨터공학개론 레포트",
                       "description": "주제: 컴퓨터공학이란 무엇인가?, 분량: 3장 이상",
                       "deadline": "2024-05-15T23:59:00Z",
                       "priority": "NONE",
                       "progress": "TODO",
                       "folderId": 404
                     }
                    """;
            given(todoService.create(any(TodoPost.class), eq(userId))).willThrow(new CustomException(ErrorCode.FOLDER_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/todo?userId={userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoPostJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.FOLDER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.FOLDER_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class getTodosByRequirements_메서드는 {

        @Test
        void 조건에_맞는_투두를_조회하고_성공_응답과_투두_응답_객체_리스트를_반환한다() throws Exception {
            // given
            Long userId = 1L;
            String todoGetJson = """
                    {
                      "priority": "NONE",
                      "progress": "TODO",
                      "isDeleted": false,
                      "folderId": 1
                    }
                    """;
            List<TodoRes> todoList = List.of(
                    TodoRes.builder().id(1L).title("Todo1").priority(Priority.NONE).progress(Progress.TODO).folderId(1L).build(),
                    TodoRes.builder().id(2L).title("Todo2").priority(Priority.NONE).progress(Progress.TODO).folderId(1L).build()
            );
            given(todoService.findAllByConditions(any(TodoGet.class), eq(userId))).willReturn(todoList);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/todo?userId={userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("priority", "NONE")
                            .queryParam("progress", "TODO")
                            .queryParam("isDeleted", "false")
                            .queryParam("folderId", "1"))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(todoList.get(0).getId()))
                    .andExpect(jsonPath("$[0].title").value(todoList.get(0).getTitle()))
                    .andExpect(jsonPath("$[0].priority").value(todoList.get(0).getPriority().toString()))
                    .andExpect(jsonPath("$[0].progress").value(todoList.get(0).getProgress().toString()))
                    .andExpect(jsonPath("$[0].folderId").value(todoList.get(0).getFolderId()))
                    .andExpect(jsonPath("$[1].id").value(todoList.get(1).getId()))
                    .andExpect(jsonPath("$[1].title").value(todoList.get(1).getTitle()))
                    .andExpect(jsonPath("$[1].priority").value(todoList.get(1).getPriority().toString()))
                    .andExpect(jsonPath("$[1].progress").value(todoList.get(1).getProgress().toString()))
                    .andExpect(jsonPath("$[1].folderId").value(todoList.get(1).getFolderId()));
        }
    }

    @Nested
    class putTodo_메서드는 {
        @Test
        void 투두를_수정하고_성공_응답과_투두_기본키를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            String todoPutJson = """
                    {
                      "title": "앱센터 발표",
                      "description": "주제: 스프링이란?, 비고: GitHub에 업로드 ",
                      "deadline": "2024-05-20T12:00:00Z",
                      "priority": "PRIMARY",
                      "progress": "DOING",
                      "folderId": 1
                    }
                    """;
            given(todoService.update(any(TodoPut.class), eq(todoId))).willReturn(todoId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/todo/{todoId}", todoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoPutJson))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().string(todoId.toString()));
        }
        @Test
        void 만약_존재하지_않는_투두_기본키가_주어지면_TODO_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long nonExistingTodoId = 404L;
            String todoPutJson = """
                    {
                      "title": "앱센터 발표",
                      "description": "주제: 스프링이란?, 비고: GitHub에 업로드 ",
                      "deadline": "2024-05-20T12:00:00Z",
                      "priority": "PRIMARY",
                      "progress": "DOING",
                      "folderId": 1
                    }
                    """;
            given(todoService.update(any(TodoPut.class), eq(nonExistingTodoId))).willThrow(new CustomException(ErrorCode.TODO_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/todo/{nonExistingTodoId}", nonExistingTodoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoPutJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.TODO_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.TODO_NOT_FOUND.getMessage()));
        }
        @Test
        void 만약_존재하지_않는_폴더_기본키가_주어지면_FOLDER_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            String todoPutJson = """
                    {
                      "title": "앱센터 발표",
                      "description": "주제: 스프링이란?, 비고: GitHub에 업로드 ",
                      "deadline": "2024-05-20T12:00:00Z",
                      "priority": "PRIMARY",
                      "progress": "DOING",
                      "folderId": 404
                    }
                    """;
            given(todoService.update(any(TodoPut.class), eq(todoId))).willThrow(new CustomException(ErrorCode.FOLDER_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/todo/{todoId}", todoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoPutJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.FOLDER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.FOLDER_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class switchTodoProgress_메서드는 {
        @Test
        void 투두_진행_상황을_변경하고_성공_응답과_투두_기본키를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            given(todoService.switchProgress(eq(todoId))).willReturn(todoId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/todo/{todoId}/progress", todoId))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().string(todoId.toString()));
        }
        @Test
        void 만약_존재하지_않는_투두_기본키가_주어지면_TODO_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            given(todoService.switchProgress(eq(todoId))).willThrow(new CustomException(ErrorCode.TODO_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/todo/{todoId}/progress", todoId))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.TODO_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.TODO_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class patchTodoFolder_메서드는 {
        @Test
        void 투두_폴더를_변경하고_성공_응답과_투두_기본키를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            String todoFolderPatchJson = """
                    {
                      "folderId": 2
                    }
                    """;
            given(todoService.moveFolder(anyLong(), eq(todoId))).willReturn(todoId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/todo/{todoId}/folder", todoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoFolderPatchJson))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().string(todoId.toString()));
        }
        @Test
        void 만약_존재하지_않는_투두_기본키가_주어지면_TODO_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            String todoFolderPatchJson = """
                    {
                      "folderId": 2
                    }
                    """;
            given(todoService.moveFolder(anyLong(), eq(todoId))).willThrow(new CustomException(ErrorCode.TODO_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/todo/{todoId}/folder", todoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoFolderPatchJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.TODO_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.TODO_NOT_FOUND.getMessage()));
        }
        @Test
        void 만약_존재하지_않는_폴더_기본키가_주어지면_FOLDER_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            String todoFolderPatchJson = """
                    {
                      "folderId": 2
                    }
                    """;
            given(todoService.moveFolder(anyLong(), eq(todoId))).willThrow(new CustomException(ErrorCode.FOLDER_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/todo/{todoId}/folder", todoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(todoFolderPatchJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.FOLDER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.FOLDER_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class deleteTodo_메서드는 {
        @Test
        void 투두_임시_삭제_후_성공_응답과_투두_기본키를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            Boolean restore = true;
            given(todoService.delete(eq(todoId), eq(restore))).willReturn(todoId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/todo/{todoId}?restore={restore}", todoId, restore))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().string(todoId.toString()));
        }

        @Test
        void 투두_영구_삭제_성공_후_noContent_응답을_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            Boolean restore = false;
            given(todoService.delete(eq(todoId), eq(restore))).willReturn(null);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/todo/{todoId}?restore={restore}", todoId, restore))
                    // then
                    .andExpect(status().isNoContent());
        }

        @Test
        void 만약_존재하지_않는_투두_기본키가_주어지면_TODO_NOT_FOUND_예외를_반환한다() throws Exception {
            // given
            Long todoId = 1L;
            Boolean restore = true;
            given(todoService.delete(eq(todoId), eq(restore))).willThrow(new CustomException(ErrorCode.TODO_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/todo/{todoId}?restore={restore}", todoId, restore))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.TODO_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.TODO_NOT_FOUND.getMessage()));
        }
    }

}