package com.serverstudy.todolist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.User;
import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import com.serverstudy.todolist.dto.response.UserRes;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.serverstudy.todolist.dto.request.UserReq.UserPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    TodoRepository todoRepository;
    @Mock
    FolderRepository folderRepository;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class join_메서드는 {
        UserPost givenUserPost() throws JsonProcessingException {
            String userPostJson = """
                    {
                      "email": "example@gmail.com",
                      "password": "examplePWD123",
                      "nickname": "ex닉네임1"
                    }
                    """;
            return objectMapper.readValue(userPostJson, UserPost.class);
        }

        @Test
        void 이메일_비밀번호_닉네임이_주어지면_새로운_유저를_생성하고_기본키를_반환한다() throws JsonProcessingException {
            // given
            /*인자 생성*/
            UserPost userPost = givenUserPost();
            /*호출 반환 값 생성*/
            User user = User.builder()
                    .email(userPost.getEmail())
                    .password(userPost.getPassword())
                    .nickname(userPost.getNickname())
                    .build();
            ReflectionTestUtils.setField(user, "id", 1L);
            /*Mocking*/
            given(userRepository.existsByEmail(userPost.getEmail())).willReturn(false);
            given(userRepository.save(any(User.class))).willReturn(user);

            // when
            long result = userService.join(userPost);

            // then
            /*반환 값 검증*/
            assertThat(result).isEqualTo(1L);
            /*호출 횟수 검증*/
            verify(userRepository, times(1)).existsByEmail(userPost.getEmail());
            verify(userRepository, times(1)).save(any(User.class));
            /*호출 인자 검증*/
            verify(userRepository).save(userCaptor.capture());
            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getEmail()).isEqualTo(userPost.getEmail());
            assertThat(savedUser.getPassword()).isEqualTo(userPost.getPassword());
            assertThat(savedUser.getNickname()).isEqualTo(userPost.getNickname());
        }
        @Test
        void 만약_중복된_이메일이_주어지면_DUPLICATE_USER_EMAIL_예외를_던진다() throws JsonProcessingException {
            // given
            /*인자 생성*/
            UserPost userPost = givenUserPost();
            /*Mocking*/
            given(userRepository.existsByEmail(userPost.getEmail())).willReturn(true);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> userService.join(userPost));

            // then
            assertEquals(ErrorCode.DUPLICATE_USER_EMAIL, exception.getErrorCode());
            /*호출 횟수 검증*/
            verify(userRepository, times(1)).existsByEmail(userPost.getEmail());
            verify(userRepository, times(0)).save(any(User.class));
        }

    }

    @Nested
    class checkEmailDuplicated_메서드는 {
        @Test
        void 이메일이_주어지면_아무것도_반환하지_않는다() {
            // given
            String email = "notDuplicated@email.com";
            given(userRepository.existsByEmail(email)).willReturn(false);

            // when
            userService.checkEmailDuplicated(email);

            // then
            verify(userRepository, times(1)).existsByEmail(email);
        }
        @Test
        void 만약_중복된_이메일이_주어지면_DUPLICATE_USER_EMAIL_예외를_던진다() {
            // given
            String email = "duplicated@email.com";
            given(userRepository.existsByEmail(email)).willReturn(true);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> userService.checkEmailDuplicated(email));

            // then
            assertEquals(ErrorCode.DUPLICATE_USER_EMAIL, exception.getErrorCode());
            verify(userRepository, times(1)).existsByEmail(email);
        }
    }

    @Nested
    class get_메서드는 {
        @Test
        void 유저_기본키가_주어지면_유저_응답_객체를_반환한다() {
            // given
            Long userId = 1L;
            User user = User.builder()
                    .email("test@email.com")
                    .nickname("nickname")
                    .password("password")
                    .build();
            ReflectionTestUtils.setField(user, "id", 1L);
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            UserRes result = userService.get(userId);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo(user.getEmail());
            assertThat(result.getNickname()).isEqualTo(user.getNickname());
            verify(userRepository, times(1)).findById(userId);
        }
        @Test
        void 만약_없는_유저의_기본키가_주어지면_USER_NOT_FOUND_예외를_던진다() {
            // given
            Long userId = 1L;
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> userService.get(userId));

            // then
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, times(1)).findById(userId);
        }
    }

    @Nested
    class modify_메서드는 {
        private UserPatch givenUserPatch() throws JsonProcessingException {
            String userPatchJson = """
                    {
                      "password": "newExamplePWD123",
                      "nickname": "newEx닉네임1"
                    }
                    """;
            return objectMapper.readValue(userPatchJson, UserPatch.class);
        }

        @Test
        void 비밀번호_닉네임과_유저_기본키가_주어지면_해당_정보를_수정하고_기본키를_반환한다() throws JsonProcessingException {
            // given
            UserPatch userPatch = givenUserPatch();
            Long userId = 1L;
            User user = User.builder()
                    .email("test@email.com")
                    .nickname("nickname")
                    .password("password")
                    .build();
            ReflectionTestUtils.setField(user, "id", 1L);
            given(userRepository.findById(userId)).willReturn(Optional.of(user));

            // when
            long result = userService.modify(userPatch, userId);

            // then
            assertThat(result).isEqualTo(userId);
            assertThat(user.getPassword()).isEqualTo(userPatch.getPassword());
            assertThat(user.getNickname()).isEqualTo(userPatch.getNickname());
            verify(userRepository, times(1)).findById(userId);
        }
        @Test
        void 만약_없는_유저의_기본키가_주어지면_USER_NOT_FOUND_예외를_던진다() throws JsonProcessingException {
            // given
            UserPatch userPatch = givenUserPatch();
            Long userId = 1L;
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> userService.modify(userPatch, userId));

            // then
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, times(1)).findById(userId);
        }
    }

    @Nested
    class delete_메서드는 {
        @Test
        void 유저_기본키가_주어지면_해당_유저와_관련된_모든_정보를_삭제한다() {
            // given
            Long userId = 1L;
            User user = User.builder()
                    .email("test@email.com")
                    .nickname("nickname")
                    .password("password")
                    .build();
            ReflectionTestUtils.setField(user, "id", userId);
            List<Todo> todoList = Collections.emptyList();
            List<Folder> folderList = Collections.emptyList();

            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(todoRepository.findAllByUserId(userId)).willReturn(todoList);
            given(folderRepository.findAllByUserId(userId)).willReturn(folderList);

            // when
            userService.delete(userId);

            // then
            verify(userRepository, times(1)).findById(userId);
            verify(todoRepository, times(1)).findAllByUserId(userId);
            verify(todoRepository, times(1)).deleteAll(todoList);
            verify(folderRepository, times(1)).findAllByUserId(userId);
            verify(folderRepository, times(1)).deleteAll(folderList);
            verify(userRepository, times(1)).delete(user);
        }
        @Test
        void 만약_없는_유저의_기본키가_주어지면_USER_NOT_FOUND_예외를_던진다() {
            // given
            Long userId = 1L;
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> userService.delete(userId));

            // then
            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
            verify(userRepository, times(1)).findById(userId);
            verify(todoRepository, times(0)).findAllByUserId(userId);
            verify(todoRepository, times(0)).deleteAll(any());
            verify(folderRepository, times(0)).findAllByUserId(userId);
            verify(folderRepository, times(0)).deleteAll(any());
            verify(userRepository, times(0)).delete(any(User.class));
        }
    }
}