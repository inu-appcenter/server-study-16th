package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.UserReq.UserPost;
import com.serverstudy.todolist.dto.request.UserReq.UserPut;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
    public ResponseEntity<?> checkUserEmail(@RequestParam @Email String email) {

        boolean checked = userService.checkEmail(email);

        return ResponseEntity.ok(checked);
    }

    @GetMapping
    public ResponseEntity<?> getUser(Long userId) {

        UserRes response = userService.get(userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> putUser(@Valid @RequestBody UserPut UserPut, Long userId) {

        long modifiedUserId = userService.modify(UserPut, userId);

        return ResponseEntity.ok(modifiedUserId);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser(Long userId) {

        userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
