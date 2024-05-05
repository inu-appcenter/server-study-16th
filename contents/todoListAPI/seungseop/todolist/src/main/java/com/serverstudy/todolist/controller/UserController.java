package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import com.serverstudy.todolist.dto.request.UserReq.UserPost;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> postUser(@Valid @RequestBody UserPost userPost) {

        long userId = userService.join(userPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkUserEmail(
            @RequestParam
            @NotBlank(message = "이메일은 공백으로 입력할 수 없습니다.")
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$", message = "이메일 형식이 올바르지 않습니다.")
            String email) {

        boolean checked = userService.checkEmail(email);

        return ResponseEntity.ok(checked);
    }

    @GetMapping
    public ResponseEntity<?> getUser(Long userId) {

        UserRes response = userService.get(userId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<?> patchUser(@Valid @RequestBody UserPatch UserPatch, Long userId) {

        long modifiedUserId = userService.modify(UserPatch, userId);

        return ResponseEntity.ok(modifiedUserId);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser(Long userId) {

        userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
