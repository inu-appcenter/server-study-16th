package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.UserReq;
import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.exception.ErrorCode;
import com.serverstudy.todolist.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Nested
    class postUser_메서드는 {
        @Test
        void 이메일_비밀번호_닉네임이_주어지면_유저_기본키_HTTP_응답이_반환된다() throws Exception {
            // given
            String userPostJson = """
                    {
                      "email": "success@email.com",
                      "password": "successPWD123",
                      "nickname": "ex성공1"
                    }
                    """;
            Long userId = 1L;
            given(userService.join(any(UserReq.UserPost.class))).willReturn(userId);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPostJson))
                    // then
                    //.andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isCreated())
                    .andExpect(content().string(userId.toString()));
        }
        @Test
        void 만약_잘못된_형식의_이메일이_주어지면_유효성_검사_예외가_반환된다() throws Exception {
            // given
            String invalidEmail = "NoAtandNoDotcom";
            String userPostJson = """
                    {
                      "email": "%s",
                      "password": "successPWD123",
                      "nickname": "ex성공1"
                    }
                    """.formatted(invalidEmail);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPostJson))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
                        assertThat(exception).isNotNull();
                        BindingResult bindingResult = exception.getBindingResult();
                        assertThat(bindingResult.getFieldErrorCount()).isEqualTo(1);
                        FieldError fieldError = bindingResult.getFieldError("email");
                        assertThat(fieldError).isNotNull();
                        assertThat(fieldError.getRejectedValue()).isEqualTo(invalidEmail);
                    });
                    //.andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));
        }
        @Test
        void 만약_잘못된_형식의_비밀번호가_주어지면_유효성_검사_예외가_반환된다() throws Exception {
            // given
            String invalidPassword = "noNumberPassword";
            String userPostJson = """
                    {
                      "email": "success@email.com",
                      "password": "%s",
                      "nickname": "ex성공1"
                    }
                    """.formatted(invalidPassword);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPostJson))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
                        assertThat(exception).isNotNull();
                        BindingResult bindingResult = exception.getBindingResult();
                        assertThat(bindingResult.getFieldErrorCount()).isEqualTo(1);
                        FieldError fieldError = bindingResult.getFieldError("password");
                        assertThat(fieldError).isNotNull();
                        assertThat(fieldError.getRejectedValue()).isEqualTo(invalidPassword);
                    });
        }
        @Test
        void 만약_잘못된_형식의_닉네임이_주어지면_유효성_검사_예외가_반환된다() throws Exception {
            // given
            String invalidNickname = "특수문자닉네임!@#";
            String userPostJson = """
                    {
                      "email": "success@email.com",
                      "password": "successPWD123",
                      "nickname": "%s"
                    }
                    """.formatted(invalidNickname);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPostJson))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
                        assertThat(exception).isNotNull();
                        BindingResult bindingResult = exception.getBindingResult();
                        assertThat(bindingResult.getFieldErrorCount()).isEqualTo(1);
                        FieldError fieldError = bindingResult.getFieldError("nickname");
                        assertThat(fieldError).isNotNull();
                        assertThat(fieldError.getRejectedValue()).isEqualTo(invalidNickname);
                    });
        }
        @Test
        void 만약_중복된_이메일이_주어지면_DUPLICATE_USER_EMAIL_예외가_반환된다() throws Exception {
            // given
            String userPostJson = """
                    {
                      "email": "duplicated@email.com",
                      "password": "successPWD123",
                      "nickname": "ex성공1"
                    }
                    """;
            willThrow(new CustomException(ErrorCode.DUPLICATE_USER_EMAIL)).given(userService).join(any(UserReq.UserPost.class));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPostJson))
                    // then
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER_EMAIL.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER_EMAIL.getMessage()));
        }
    }

    @Nested
    class checkUserEmail_메서드는 {
        @Test
        void 이메일이_주어지면_이메일_HTTP_응답을_반환한다() throws Exception {
            // given
            String email = "success@email.com";
            willDoNothing().given(userService).checkEmailDuplicated(email);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user/check-email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("email", email))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(content().string(email));
            verify(userService, times(1)).checkEmailDuplicated(email);
        }
        @Test
        void 만약_잘못된_형식의_이메일이_주어지면_유효성_검사_예외가_반환된다() throws Exception {
            // given
            String invalidEmail = "NoAtandNoDotcom";

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user/check-email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("email", invalidEmail))
                    // then
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> {
                        ConstraintViolationException exception = (ConstraintViolationException) result.getResolvedException();
                        assertThat(exception).isNotNull();
                        assertThat(exception.getMessage()).contains("이메일 형식이 올바르지 않습니다.");
                    });
                    //.andExpect(result -> assertInstanceOf(ConstraintViolationException.class, result.getResolvedException()));
        }
        @Test
        void 만약_중복된_이메일이_주어지면_DUPLICATE_USER_EMAIL_예외가_반환된다() throws Exception {
            // given
            String duplicatedEmail = "duplicated@email.com";
            willThrow(new CustomException(ErrorCode.DUPLICATE_USER_EMAIL)).given(userService).checkEmailDuplicated(duplicatedEmail);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user/check-email")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("email", duplicatedEmail))
                    // then
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER_EMAIL.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_USER_EMAIL.getMessage()));
        }
    }

    @Nested
    class getUser_메서드는 {
        @Test
        void 유저_기본키가_주어지면_유저_응답_객체_HTTP_응답을_반환한다() throws Exception {
            // given
            Long userId = 1L;
            UserRes userRes = UserRes.builder()
                    .id(userId)
                    .email("example@gmail.com")
                    .nickname("ex닉네임1")
                    .build();
            given(userService.get(userId)).willReturn(userRes);

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("userId", userId.toString()))
                    // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userRes.getId()))
                    .andExpect(jsonPath("$.email").value(userRes.getEmail()))
                    .andExpect(jsonPath("$.nickname").value(userRes.getNickname()));
        }
        @Test
        void 만약_없는_유저의_기본키가_주어지면_USER_NOT_FOUND_예외가_반환된다() throws Exception {
            // given
            Long nonExistingUserId = 404L;
            given(userService.get(nonExistingUserId)).willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

            // when
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("userId", nonExistingUserId.toString()))
                    // then
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class patchUser_메서드는 {
        @Test
        void 유저_기본키와_수정할_정보가_주어지면_유저_정보가_성공적으로_수정된다() throws Exception {
            // given
            Long userId = 1L;
            String userPatchJson = """
                    {
                      "password": "newExamplePWD123",
                      "nickname": "newEx닉네임1"
                    }
                    """;
            Long modifiedUserId = 1L;
            given(userService.modify(any(UserPatch.class), eq(userId))).willReturn(modifiedUserId);

            // when then
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPatchJson)
                            .queryParam("userId", userId.toString())).andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").value(modifiedUserId));
        }
        @Test
        void 만약_존재하지_않는_유저_기본키로_수정을_시도하면_USER_NOT_FOUND_예외가_반환된다() throws Exception {
            // given
            Long nonExistingUserId = 404L;
            String userPatchJson = """
                    {
                      "password": "newExamplePWD123",
                      "nickname": "newEx닉네임1"
                    }
                    """;
            given(userService.modify(any(UserPatch.class), eq(nonExistingUserId))).willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

            // when then
            mockMvc.perform(MockMvcRequestBuilders
                            .patch("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userPatchJson)
                            .queryParam("userId", nonExistingUserId.toString()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
        }
    }

    @Nested
    class deleteUser_메서드는 {
        @Test
        void 유저_기본키가_주어지면_유저가_성공적으로_삭제된다() throws Exception {
            // given
            Long userId = 1L;
            willDoNothing().given(userService).delete(userId);

            // when then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("userId", userId.toString()))
                    .andExpect(status().isNoContent());
        }
        @Test
        void 만약_존재하지_않는_유저를_삭제하려고_하면_유저_삭제_예외가_발생한다() throws Exception {
            // given
            Long userId = 404L;
            CustomException expectedException = new CustomException(ErrorCode.USER_NOT_FOUND);
            willThrow(expectedException).given(userService).delete(userId);

            // when then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .queryParam("userId", userId.toString()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.toString()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
        }
    }
}