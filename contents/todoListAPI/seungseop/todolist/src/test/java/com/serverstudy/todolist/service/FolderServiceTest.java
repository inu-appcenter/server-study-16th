package com.serverstudy.todolist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPost;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.exception.ErrorCode;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FolderServiceTest {

    @InjectMocks
    FolderService folderService;
    @Mock
    FolderRepository folderRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TodoRepository todoRepository;
    @Captor
    private ArgumentCaptor<Folder> folderCaptor;
    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class create_메서드는 {
        FolderPost givenFolderPost() throws JsonProcessingException {
            String folderPostJson = """
                    {
                      "name": "과제 폴더"
                    }
                    """;
            return objectMapper.readValue(folderPostJson, FolderPost.class);
        }

        @Test
        void 폴더_이름과_유저_기본키가_주어지면_새로운_폴더를_생성하고_기본키를_반환한다() throws JsonProcessingException {
            // given
            FolderPost folderPost = givenFolderPost();
            Long userId = 1L;
            Folder folder = Folder.builder()
                    .name(folderPost.getName())
                    .userId(userId)
                    .build();
            Long folderId = 1L;
            ReflectionTestUtils.setField(folder, "id", folderId);
            given(userRepository.existsById(userId)).willReturn(true);
            given(folderRepository.existsByNameAndUserId(folderPost.getName(), userId)).willReturn(false);
            given(folderRepository.save(any(Folder.class))).willReturn(folder);

            // when
            long result = folderService.create(folderPost, userId);

            // then
            assertThat(result).isEqualTo(folderId);
            verify(userRepository, times(1)).existsById(userId);
            verify(folderRepository, times(1)).existsByNameAndUserId(folderPost.getName(), userId);
            verify(folderRepository, times(1)).save(folderCaptor.capture());
            Folder savedFolder = folderCaptor.getValue();
            assertThat(savedFolder.getName()).isEqualTo(folderPost.getName());
            assertThat(savedFolder.getUserId()).isEqualTo(userId);
        }

        @Test
        void 만약_존재하지_않는_유저의_기본키가_주어지면_USER_NOT_FOUND_예외를_던진다() throws JsonProcessingException {
            // given
            FolderPost folderPost = givenFolderPost();
            Long userId = 1L;
            given(userRepository.existsById(userId)).willReturn(false);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> folderService.create(folderPost, userId));

            // then
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, times(1)).existsById(userId);
            verify(folderRepository, times(0)).existsByNameAndUserId(any(String.class), any(Long.class));
            verify(folderRepository, times(0)).save(any(Folder.class));
        }

        @Test
        void 만약_중복된_폴더_이름이_주어지면_DUPLICATE_FOLDER_NAME_예외를_던진다() throws JsonProcessingException {
            // given
            FolderPost folderPost = givenFolderPost();
            Long userId = 1L;
            given(userRepository.existsById(userId)).willReturn(true);
            given(folderRepository.existsByNameAndUserId(folderPost.getName(), userId)).willReturn(true);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> folderService.create(folderPost, userId));

            // then
            assertEquals(ErrorCode.DUPLICATE_FOLDER_NAME, exception.getErrorCode());
            verify(userRepository, times(1)).existsById(userId);
            verify(folderRepository, times(1)).existsByNameAndUserId(folderPost.getName(), userId);
            verify(folderRepository, times(0)).save(any(Folder.class));
        }
    }

    @Nested
    class getAllWithTodoCount_메서드는 {
        @Test
        void 유저_기본키가_주어지면_유저의_모든_폴더의_정보를_반환한다() {
            // given
            Long userId = 1L;
            Folder folder1 = Folder.builder().name("Folder 1").userId(userId).build();
            ReflectionTestUtils.setField(folder1, "id", 1L);
            Folder folder2 = Folder.builder().name("Folder 2").userId(userId).build();
            ReflectionTestUtils.setField(folder2, "id", 2L);

            List<Folder> folderList = List.of(folder1, folder2);

            given(folderRepository.findAllByUserIdOrderByNameAsc(userId)).willReturn(folderList);
            given(todoRepository.countByFolder(folder1)).willReturn(3);
            given(todoRepository.countByFolder(folder2)).willReturn(5);

            // when
            List<FolderRes> result = folderService.getAllWithTodoCount(userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getFolderId()).isEqualTo(folder1.getId());
            assertThat(result.get(0).getName()).isEqualTo(folder1.getName());
            assertThat(result.get(0).getTodoCount()).isEqualTo(3);
            assertThat(result.get(1).getFolderId()).isEqualTo(folder2.getId());
            assertThat(result.get(1).getName()).isEqualTo(folder2.getName());
            assertThat(result.get(1).getTodoCount()).isEqualTo(5);
        }
    }

    @Nested
    class modify_메서드는 {
        @Test
        void 수정할_폴더_이름과_폴더_기본키가_주어지면_수정하고_폴더_기본키를_반환한다() throws JsonProcessingException {
            // given
            Long folderId = 1L;
            Long userId = 1L;
            String folderPatchJson = """
                    {
                      "name": "New Folder Name"
                    }
                    """;
            FolderPatch folderPatch = objectMapper.readValue(folderPatchJson, FolderPatch.class);

            Folder folder = Folder.builder().name("Old Folder Name").userId(userId).build();
            ReflectionTestUtils.setField(folder, "id", folderId);

            given(folderRepository.findById(folderId)).willReturn(Optional.of(folder));
            given(folderRepository.existsByNameAndUserId(folderPatch.getName(), userId)).willReturn(false);

            // when
            long result = folderService.modify(folderPatch, folderId);

            // then
            assertThat(result).isEqualTo(folderId);
            assertThat(folder.getName()).isEqualTo(folderPatch.getName());
            verify(folderRepository, times(1)).findById(folderId);
            verify(folderRepository, times(1)).existsByNameAndUserId(folderPatch.getName(), userId);
        }

        @Test
        void 만약_중복된_폴더_이름이_존재하면_DUPLICATE_FOLDER_NAME_예외를_던진다() throws JsonProcessingException {
            // given
            Long folderId = 1L;
            Long userId = 1L;
            String folderPatchJson = """
                    {
                      "name": "New Folder Name"
                    }
                    """;
            FolderPatch folderPatch = objectMapper.readValue(folderPatchJson, FolderPatch.class);

            Folder folder = Folder.builder().name("Old Folder Name").userId(userId).build();

            given(folderRepository.findById(folderId)).willReturn(Optional.of(folder));
            given(folderRepository.existsByNameAndUserId(folderPatch.getName(), userId)).willReturn(true);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> folderService.modify(folderPatch, folderId));

            // then
            assertEquals(ErrorCode.DUPLICATE_FOLDER_NAME, exception.getErrorCode());
            verify(folderRepository, times(1)).findById(folderId);
            verify(folderRepository, times(1)).existsByNameAndUserId(folderPatch.getName(), userId);
        }

        @Test
        void 만약_존재하지_않는_폴더의_기본키가_주어지면_FOLDER_NOT_FOUND_예외를_던진다() throws JsonProcessingException {
            // given
            Long folderId = 1L;
            String folderPatchJson = """
                    {
                      "name": "New Folder Name"
                    }
                    """;
            FolderPatch folderPatch = objectMapper.readValue(folderPatchJson, FolderPatch.class);

            given(folderRepository.findById(folderId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> folderService.modify(folderPatch, folderId));

            // then
            assertEquals(ErrorCode.FOLDER_NOT_FOUND, exception.getErrorCode());
            verify(folderRepository, times(1)).findById(folderId);
        }
    }
    @Nested
    class delete_메서드는 {
        @Test
        void 폴더_기본키가_주어지면_해당_폴더를_삭제하고_폴더에_속한_투두를_폴더없음으로_바꾼다() {
            // given
            Long folderId = 1L;
            Folder folder = Folder.builder().name("Folder").userId(1L).build();

            List<Todo> todoList = List.of(
                    Todo.builder().title("Todo 1").folder(folder).build(),
                    Todo.builder().title("Todo 2").folder(folder).build()
            );

            given(folderRepository.findById(folderId)).willReturn(Optional.of(folder));
            given(todoRepository.findAllByFolder(any(Folder.class))).willReturn(todoList);

            // when
            folderService.delete(folderId);

            // then
            assertThat(todoList.get(0).getFolder()).isEqualTo(null);
            assertThat(todoList.get(1).getFolder()).isEqualTo(null);
            verify(folderRepository, times(1)).findById(folderId);
            verify(todoRepository, times(1)).findAllByFolder(folder);
            verify(folderRepository, times(1)).delete(folder);
        }
    }
}