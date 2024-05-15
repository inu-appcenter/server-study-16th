package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.common.ExampleData;
import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import com.serverstudy.todolist.dto.request.UserReq.UserPost;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.exception.ErrorResponse;
import com.serverstudy.todolist.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user")
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
    public ResponseEntity<Long> postUser(@Valid @RequestBody UserPost userPost) {

        Long userId = userService.join(userPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
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

    @Operation(summary = "유저 조회", description = "해당 유저의 정보를 조회합니다.", parameters = {
            @Parameter(name = "userId", description = "유저 Id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "유저 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "USER_NOT_FOUND", value = USER_NOT_FOUND_DATA),
            }))
    })
    @GetMapping
    public ResponseEntity<UserRes> getUser(@NotNull Long userId) {

        UserRes response = userService.get(userId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "유저 정보 수정", description = "해당 유저의 정보를 수정합니다.", parameters = {
            @Parameter(name = "userId", description = "유저 Id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않음", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "USER_NOT_FOUND", value = USER_NOT_FOUND_DATA),
            }))
    })
    @PatchMapping
    public ResponseEntity<Long> patchUser(@Valid @RequestBody UserPatch UserPatch, @NotNull Long userId) {

        Long modifiedUserId = userService.modify(UserPatch, userId);

        return ResponseEntity.ok(modifiedUserId);
    }


    @Operation(summary = "유저 삭제", description = "해당 유저를 삭제합니다.", parameters = {
            @Parameter(name = "userId", description = "유저 Id", example = "1")
    }, responses = {
            @ApiResponse(responseCode = "204", description = "유저 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = {
                    @ExampleObject(name = "INVALID_PARAMETER", value = INVALID_PARAMETER_DATA),
            })),
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@NotNull Long userId) {

        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }

}
