package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.common.ExampleData;
import com.serverstudy.todolist.domain.User;
import com.serverstudy.todolist.domain.enums.Role;
import com.serverstudy.todolist.dto.request.UserReq;
import com.serverstudy.todolist.dto.request.UserReq.UserLoginPost;
import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import com.serverstudy.todolist.dto.request.UserReq.UserPost;
import com.serverstudy.todolist.dto.response.JwtRes;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.exception.ErrorResponse;
import com.serverstudy.todolist.repository.UserRepository;
import com.serverstudy.todolist.security.SecurityUser;
import com.serverstudy.todolist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "User", description = "User API 입니다.")
@Validated
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController implements ExampleData {

    private final UserService userService;

    @Operation(summary = "유저 생성", description = "새로운 유저를 생성합니다.", responses = {
            @ApiResponse(responseCode = "201", description = "유저 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "409", description = "중복된 유저 이메일", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "DUPLICATE_USER_EMAIL", value = DUPLICATE_USER_EMAIL_DATA),
            }))
    })
    @PostMapping
    public ResponseEntity<JwtRes> postUser(@Valid @RequestBody UserPost userPost) {

        JwtRes token = userService.join(userPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @Operation(summary = "유저 로그인", description = "해당 유저로 로그인합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "유저 로그인 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "BAD_CREDENTIALS", value = BAD_CREDENTIALS_DATA),
            }))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtRes> login(@RequestBody UserLoginPost loginDto) {

        JwtRes token = userService.login(loginDto);

        return ResponseEntity.ok(token);
    }

    @Operation(summary = "이메일 중복 검사", description = "이메일의 중복 여부를 검사합니다.", parameters = {
            @Parameter(name = "email", description = "중복 검사할 이메일", example = "example@gmail.com")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "중복 없음", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "409", description = "중복된 유저 이메일", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "DUPLICATE_USER_EMAIL", value = DUPLICATE_USER_EMAIL_DATA),
            }))
    })
    @GetMapping("/check-email")
    public ResponseEntity<String> checkUserEmail(
            @RequestParam
            @NotBlank(message = "이메일은 공백으로 입력할 수 없습니다.")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$", message = "이메일 형식이 올바르지 않습니다.")
            String email) {

        userService.checkEmailDuplicated(email);

        return ResponseEntity.ok(email);
    }

    @Operation(summary = "유저 조회", description = "해당 유저의 정보를 조회합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "USER_NOT_FOUND", value = USER_NOT_FOUND_DATA),
            }))
    })
    @GetMapping
    public ResponseEntity<UserRes> getUser(@AuthenticationPrincipal SecurityUser user) {

        UserRes response = userService.get(user.getId());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 정보 수정", description = "해당 유저의 정보를 수정합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "USER_NOT_FOUND", value = USER_NOT_FOUND_DATA),
            }))
    })
    @PatchMapping
    public ResponseEntity<UserRes> patchUser(@Valid @RequestBody UserPatch UserPatch, @AuthenticationPrincipal SecurityUser user) {

        UserRes modifiedUserRes = userService.modify(UserPatch, user.getId());

        return ResponseEntity.ok(modifiedUserRes);
    }


    @Operation(summary = "유저 삭제", description = "해당 유저를 삭제합니다.", responses = {
            @ApiResponse(responseCode = "204", description = "유저 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal SecurityUser user) {

        userService.delete(user.getId());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "관리자 로그인", description = "관리자 계정으로 로그인합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "관리자 로그인 성공", useReturnTypeSchema = true),
    })
    @PostMapping("/admin")
    public ResponseEntity<JwtRes> getAdmin() {

        JwtRes token = userService.getAdmin();

        return ResponseEntity.ok(token);
    }

    @Operation(summary = "모든 유저 정보 조회", description = "모든 유저 정보를 조회합니다. 관리자 계정으로 로그인 되어 있어야 합니다.", responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공", useReturnTypeSchema = true),
    })
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public ResponseEntity<List<UserRes>> getAllUsers() {

        List<UserRes> userResList = userService.getAll();

        return ResponseEntity.ok(userResList);
    }

}
