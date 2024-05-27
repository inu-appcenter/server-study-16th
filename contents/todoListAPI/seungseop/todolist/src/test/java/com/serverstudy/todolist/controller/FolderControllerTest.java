package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPost;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.exception.ErrorCode;
import com.serverstudy.todolist.service.FolderService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = FolderController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class FolderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FolderService folderService;

    @Nested
    class postFolder_메서드는 {
        @Test
        void 폴더_이름과_유저_기본키가_주어지면_폴더를_생성하고_created_응답과_폴더_기본키를_반환한다() throws Exception {
            // given
            Long userId = 1L;
            String folderPostJson = """
                    {
                      "name": "새로운 폴더"
                    }
                    """;
            Long folderId = 1L;
            given(folderService.create(any(FolderPost.class), eq(userId))).willReturn(folderId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/folder?userId={userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(folderPostJson))
                    // then
                    .andExpect(status().isCreated())
                    .andExpect(content().string(folderId.toString()));
        }
        @Test
        void 만약_없는_유저의_기본키가_주어지면_USER_NOT_FOUND_예외가_반환된다() throws Exception {
            // given
            Long nonExistingUserId = 404L;
            String folderPostJson = """
                    {
                      "name": "새로운 폴더"
                    }
                    """;

            given(folderService.create(any(FolderPost.class), eq(nonExistingUserId))).willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/folder?userId={nonExistingUserId}", nonExistingUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(folderPostJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
        }
        @Test
        void 만약_중복된_폴더_이름이_주어지면_DUPLICATE_FOLDER_NAME_예외가_반환된다() throws Exception {
            // given
            Long userId = 1L;
            String folderPostJson = """
                    {
                      "name": "중복된 폴더"
                    }
                    """;

            given(folderService.create(any(FolderPost.class), eq(userId))).willThrow(new CustomException(ErrorCode.DUPLICATE_FOLDER_NAME));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/folder?userId={userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(folderPostJson))
                    // then
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_FOLDER_NAME.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_FOLDER_NAME.getMessage()));
        }
    }

    @Nested
    class getFoldersByUser_메서드는 {

        // 폴더 목록 조회 성공
        @Test
        void 유저_기본키가_주어지면_성공_응답과_폴더_응답_객체_리스트를_반환한다() throws Exception {
            // given
            Long userId = 1L;
            List<FolderRes> folderList = List.of(
                    FolderRes.builder().folderId(1L).name("폴더1").todoCount(3).build(),
                    FolderRes.builder().folderId(2L).name("폴더2").todoCount(5).build()
            );

            given(folderService.getAllWithTodoCount(userId)).willReturn(folderList);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/folder?userId={userId}", userId))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].folderId").value(folderList.get(0).getFolderId()))
                    .andExpect(jsonPath("$[0].name").value(folderList.get(0).getName()))
                    .andExpect(jsonPath("$[0].todoCount").value(folderList.get(0).getTodoCount()))
                    .andExpect(jsonPath("$[1].folderId").value(folderList.get(1).getFolderId()))
                    .andExpect(jsonPath("$[1].name").value(folderList.get(1).getName()))
                    .andExpect(jsonPath("$[1].todoCount").value(folderList.get(1).getTodoCount()));
        }
    }

    @Nested
    class patchFolder_메서드는 {
        @Test
        void 수정할_폴더_이름과_유저_기본키가_주어지면_수정_후_성공_응답과_폴더_기본키를_반환한다() throws Exception {
            // given
            Long folderId = 1L;
            String folderPatchJson = """
                    {
                      "name": "수정된 폴더"
                    }
                    """;

            Long modifiedFolderId = 1L;
            given(folderService.modify(any(FolderPatch.class), eq(folderId))).willReturn(modifiedFolderId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/folder/{folderId}", folderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(folderPatchJson))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().string(modifiedFolderId.toString()));
        }
        @Test
        void 만약_없는_폴더의_기본키가_주어지면_FOLDER_NOT_FOUND_예외가_반환된다() throws Exception {
            // given
            Long nonExistingFolderId = 1L;
            String folderPatchJson = """
                    {
                      "name": "수정된 폴더"
                    }
                    """;

            given(folderService.modify(any(FolderPatch.class), eq(nonExistingFolderId))).willThrow(new CustomException(ErrorCode.FOLDER_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/folder/{nonExistingFolderId}", nonExistingFolderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(folderPatchJson))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.FOLDER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.FOLDER_NOT_FOUND.getMessage()));
        }
        @Test
        void 만약_중복된_폴더_이름이_주어지면_DUPLICATE_FOLDER_NAME_예외가_반환된다() throws Exception {
            // given
            Long folderId = 1L;
            String folderPatchJson = """
                    {
                      "name": "중복된 폴더"
                    }
                    """;

            given(folderService.modify(any(FolderPatch.class), eq(folderId))).willThrow(new CustomException(ErrorCode.DUPLICATE_FOLDER_NAME));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/folder/{folderId}", folderId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(folderPatchJson))
                    // then
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_FOLDER_NAME.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_FOLDER_NAME.getMessage()));
        }
    }

    @Nested
    class deleteFolder_메서드는 {
        @Test
        void 폴더_기본키가_주어지면_해당_폴더를_삭제한다() throws Exception {
            // given
            Long folderId = 1L;

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/folder/{folderId}", folderId))
                    // then
                    .andExpect(status().isNoContent());
        }
    }
}