package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.UserDto;
import com.serverstudy.todolist.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> postUser(@Valid @RequestBody UserDto.PostReq postReq) {

        long userId = userService.join(postReq);

        return ResponseEntity.ok(userId);
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkUserEmail(@RequestParam @Email String email) {

        boolean checked = userService.checkEmail(email);

        return ResponseEntity.ok(checked);
    }

    @GetMapping
    public ResponseEntity<?> getUser(Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        UserDto.Response response = userService.get(userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> putUser(@Valid @RequestBody UserDto.PutReq PutReq, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long modifiedUserId = userService.modify(PutReq, userId);

        return ResponseEntity.ok(modifiedUserId);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser(Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long deletedUserId = userService.delete(userId);

        return ResponseEntity.ok(deletedUserId);
    }

}
