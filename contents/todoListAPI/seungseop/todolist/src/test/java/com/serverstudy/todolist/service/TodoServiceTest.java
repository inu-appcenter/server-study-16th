package com.serverstudy.todolist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;
import com.serverstudy.todolist.dto.response.TodoRes;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.serverstudy.todolist.dto.request.TodoReq.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @InjectMocks
    TodoService todoService;
    @Mock
    TodoRepository todoRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    FolderRepository folderRepository;
    @Captor
    private ArgumentCaptor<Todo> todoCaptor;
    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class create_메서드는 {
        TodoPost givenTodoPost() throws JsonProcessingException {
            String todoPostJson = """
                    {
                      "title": "컴퓨터공학개론 레포트",
                      "description": "주제: 컴퓨터공학이란 무엇인가?, 분량: 3장 이상",
                      "priority": "NONE",
                      "progress": "TODO",
                      "folderId": 1
                    }
                    """;
            return objectMapper.readValue(todoPostJson, TodoPost.class);
        }

        @Test
        void 주어진_투두_정보와_유저_기본키에_대해_투두를_생성_및_저장하고_기본키를_반환한다() throws JsonProcessingException {
            // given
            Long userId = 1L;
            Long folderId = 1L;
            TodoPost todoPost = givenTodoPost();

            Folder folder = Folder.builder().name("Folder").userId(userId).build();
            ReflectionTestUtils.setField(folder, "id", 1L);

            Todo todo = todoPost.toEntity(userId, folder);
            ReflectionTestUtils.setField(todo, "id", 1L);

            given(userRepository.existsById(userId)).willReturn(true);
            given(folderRepository.findById(folderId)).willReturn(Optional.of(folder));
            given(todoRepository.save(any(Todo.class))).willReturn(todo);

            // when
            long result = todoService.create(todoPost, userId);

            // then
            assertThat(result).isEqualTo(1L);
            verify(userRepository, times(1)).existsById(userId);
            verify(folderRepository, times(1)).findById(folderId);
            verify(todoRepository, times(1)).save(todoCaptor.capture());
            Todo savedTodo = todoCaptor.getValue();
            assertThat(savedTodo.getTitle()).isEqualTo(todoPost.getTitle());
            assertThat(savedTodo.getDescription()).isEqualTo(todoPost.getDescription());
            assertThat(savedTodo.getFolder().getId()).isEqualTo(folderId);
        }
        @Test
        void 만약_존재하지_않는_유저_기본키가_주어지면_USER_NOT_FOUND_예외를_던진다() throws JsonProcessingException {
            // given
            Long userId = 1L;
            TodoPost todoPost = givenTodoPost();
            given(userRepository.existsById(userId)).willReturn(false);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> todoService.create(todoPost, userId));

            // then
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, times(1)).existsById(userId);
            verify(todoRepository, times(0)).save(any(Todo.class));
        }
    }

    @Nested
    class findAllByConditions_메서드는 {
        @Test
        void 조건에_맞는_투두들을_검색한_후_투두_응답_객체_리스트로_반환한다() throws JsonProcessingException {
            // given
            Long userId = 1L;
            String todoGetJson = """
                    {
                      "priority": "NONE",
                      "progress": "TODO",
                      "isDeleted": false
                    }
                    """;
            TodoGet todoGet = objectMapper.readValue(todoGetJson, TodoGet.class);
            List<Todo> todoList = List.of(
                    Todo.builder().title("Todo 1").priority(Priority.NONE).progress(Progress.TODO).userId(userId).build(),
                    Todo.builder().title("Todo 2").priority(Priority.NONE).progress(Progress.TODO).userId(userId).build()
            );
            ReflectionTestUtils.setField(todoList.get(0),"id", 1L);
            todoList.get(0).moveToTrash();
            ReflectionTestUtils.setField(todoList.get(1),"id", 2L);

            given(todoRepository.findAllByConditions(any(), eq(userId), any(Priority.class), any(Progress.class), any(Boolean.class))).willReturn(todoList);

            // when
            List<TodoRes> result = todoService.findAllByConditions(todoGet, userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(0).getTitle()).isEqualTo(todoList.get(0).getTitle());
            assertThat(result.get(0).getDateFromDelete()).isEqualTo(0);
            assertThat(result.get(1).getId()).isEqualTo(2L);
            assertThat(result.get(1).getTitle()).isEqualTo(todoList.get(1).getTitle());
        }
    }

    @Nested
    class update_메서드는 {
        @Test
        void 주어진_투두_정보와_유저_기본키에_대해_투두_정보를_수정하고_기본키를_반환한다() throws JsonProcessingException {
            // given
            Long todoId = 1L;
            String todoPutJson = """
                    {
                      "title": "앱센터 발표",
                      "description": "주제: 스프링이란?, 비고: GitHub에 업로드 ",
                      "priority": "PRIMARY",
                      "progress": "DOING",
                      "folderId": 1
                    }
                    """;
            TodoPut todoPut = objectMapper.readValue(todoPutJson, TodoPut.class);

            Todo todo = Todo.builder().title("Todo").build();
            ReflectionTestUtils.setField(todo, "id", todoId);
            Folder folder = Folder.builder().name("Folder").build();

            given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
            given(folderRepository.findById(todoPut.getFolderId())).willReturn(Optional.of(folder));

            // when
            long result = todoService.update(todoPut, todoId);

            // then
            assertThat(result).isEqualTo(todoId);
            verify(todoRepository, times(1)).findById(todoId);
            verify(folderRepository, times(1)).findById(todoPut.getFolderId());
        }
        @Test
        void 존재하지_않는_투두_기본키가_주어지면_TODO_NOT_FOUND_예외를_던진다() throws JsonProcessingException {
            // given
            Long todoId = 1L;
            String todoPutJson = """
                    {
                      "title": "앱센터 발표",
                      "description": "주제: 스프링이란?, 비고: GitHub에 업로드 ",
                      "priority": "PRIMARY",
                      "progress": "DOING"
                    }
                    """;
            TodoPut todoPut = objectMapper.readValue(todoPutJson, TodoPut.class);
            given(todoRepository.findById(todoId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> todoService.update(todoPut, todoId));

            // then
            assertEquals(ErrorCode.TODO_NOT_FOUND, exception.getErrorCode());
            verify(todoRepository, times(1)).findById(todoId);
            verify(folderRepository, times(0)).findById(todoPut.getFolderId());
        }
    }

    @Nested
    class switchProgress_메서드는 {
        @Test
        void 주어진_투두_기본키에_해당하는_투두_상태를_변경하고_기본키를_반환한다() {
            // given
            Long todoId = 1L;
            Todo todo = Todo.builder().title("Todo").progress(Progress.TODO).build();
            ReflectionTestUtils.setField(todo, "id", todoId);
            given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
            Todo switchedTodo =Todo.builder().title("Todo").progress(Progress.TODO).build();
            switchedTodo.switchProgress();

            // when
            long result = todoService.switchProgress(todoId);

            // then
            assertThat(result).isEqualTo(todoId);
            assertThat(todo.getProgress()).isEqualTo(switchedTodo.getProgress());
            verify(todoRepository, times(1)).findById(todoId);
        }
        @Test
        void 만약_주어진_투두_기본키에_해당하는_투두가_존재하지_않으면_TODO_NOT_FOUND_예외를_던진다() {
            // given
            Long todoId = 1L;
            given(todoRepository.findById(todoId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> todoService.switchProgress(todoId));

            // then
            assertEquals(ErrorCode.TODO_NOT_FOUND, exception.getErrorCode());
            verify(todoRepository, times(1)).findById(todoId);
        }
    }

    @Nested
    class moveFolder_메서드는 {
        @Test
        void 투두를_해당_폴더로_이동하고_기본키를_반환한다() {
            // given
            Long folderId = 1L;
            Long todoId = 1L;
            Folder folder = Folder.builder().name("Folder").build();
            ReflectionTestUtils.setField(folder, "id", folderId);
            Todo todo = Todo.builder().title("Todo").build();
            ReflectionTestUtils.setField(todo, "id", todoId);

            given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
            given(folderRepository.findById(folderId)).willReturn(Optional.of(folder));

            // when
            long result = todoService.moveFolder(folderId, todoId);

            // then
            assertThat(result).isEqualTo(todoId);
            assertThat(todo.getFolder()).isEqualTo(folder);
            verify(todoRepository, times(1)).findById(todoId);
            verify(folderRepository, times(1)).findById(folderId);
        }
        @Test
        void 만약_투두_기본키에_해당하는_투두가_존재하지_않으면_TODO_NOT_FOUND_예외를_던진다() {
            // given
            Long folderId = 1L;
            Long todoId = 1L;
            given(todoRepository.findById(todoId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> todoService.moveFolder(folderId, todoId));

            // then
            assertEquals(ErrorCode.TODO_NOT_FOUND, exception.getErrorCode());
            verify(todoRepository, times(1)).findById(todoId);
            verify(folderRepository, times(0)).findById(folderId);
        }
        @Test
        void 만약_폴더_기본키에_해당하는_폴더가_존재하지_않으면_FOLDER_NOT_FOUND_예외를_던진다() {
            // given
            Long folderId = 1L;
            Long todoId = 1L;
            Todo todo = Todo.builder().title("Todo").build();
            ReflectionTestUtils.setField(todo, "id", todoId);
            given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
            given(folderRepository.findById(folderId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> todoService.moveFolder(folderId, todoId));

            // then
            assertEquals(ErrorCode.FOLDER_NOT_FOUND, exception.getErrorCode());
            verify(todoRepository, times(1)).findById(todoId);
            verify(folderRepository, times(1)).findById(folderId);
        }
    }

    @Nested
    class delete_메서드는 {
        @Test
        void restore_값이_false_이면_투두를_삭제하고_null_을_반환한다() {
            // given
            Long todoId = 1L;
            Boolean restore = false;
            Todo todo = Todo.builder().title("Todo").build();

            given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

            // when
            Long result = todoService.delete(todoId, restore);

            // then
            assertThat(result).isNull();
            verify(todoRepository, times(1)).findById(todoId);
            verify(todoRepository, times(1)).delete(todo);
        }

        @Test
        void restore_값이_true_이면_투두를_휴지통으로_옮기고_기본키를_반환한다() {
            // given
            Long todoId = 1L;
            Boolean restore = true;
            Todo todo = Todo.builder().title("Todo").build();

            given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

            // when
            Long result = todoService.delete(todoId, restore);

            // then
            assertThat(result).isEqualTo(todoId);
            verify(todoRepository, times(1)).findById(todoId);
            verify(todoRepository, times(0)).delete(todo);
            assertThat(todo.getIsDeleted()).isTrue();
        }
        @Test
        void 투두_기본키가_존재하지_않으면_TODO_NOT_FOUND_예외를_던진다() {
            // given
            Long todoId = 1L;
            Boolean restore = false;
            given(todoRepository.findById(todoId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> todoService.delete(todoId, restore));

            // then
            assertEquals(ErrorCode.TODO_NOT_FOUND, exception.getErrorCode());
            verify(todoRepository, times(1)).findById(todoId);
        }
    }

    @Nested
    class deleteInTrash_메서드는 {
        @Test
        void 휴지통에_있는_30일_지난_투두들을_삭제한다() {
            // given
            Todo todo1 = Todo.builder().title("Todo1").build();
            ReflectionTestUtils.setField(todo1, "id", 1L);
            todo1.moveToTrash();
            ReflectionTestUtils.setField(todo1, "deletedTime", LocalDateTime.now().minusDays(31));

            Todo todo2 = Todo.builder().title("Todo2").build();
            ReflectionTestUtils.setField(todo2, "id", 2L);
            todo2.moveToTrash();
            ReflectionTestUtils.setField(todo2, "deletedTime", LocalDateTime.now().minusDays(10));

            List<Todo> todoList = List.of(todo1, todo2);
            given(todoRepository.findAllByIsDeletedOrderByDeletedTimeAsc(true)).willReturn(todoList);

            // when
            todoService.deleteInTrash();

            // then
            verify(todoRepository, times(1)).findAllByIsDeletedOrderByDeletedTimeAsc(true);
            verify(todoRepository, times(1)).delete(todo1);
            verify(todoRepository, times(0)).delete(todo2);
        }
    }
}