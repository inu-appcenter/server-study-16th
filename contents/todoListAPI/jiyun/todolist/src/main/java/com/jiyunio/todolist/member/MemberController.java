package com.jiyunio.todolist.member;

import com.jiyunio.todolist.member.dto.ChangeUserPwDto;
import com.jiyunio.todolist.member.dto.SignInDto;
import com.jiyunio.todolist.member.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) {
        if (memberService.signUp(signUpDto)) return ResponseEntity.ok("회원가입 성공");
        return new ResponseEntity<>("회원가입 실패", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> signIn(@RequestBody SignInDto signInDto) {
        if (memberService.signIn(signInDto)) return ResponseEntity.ok("로그인 성공");
        return new ResponseEntity<>("로그인 실패", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateUserPw(@PathVariable Long id, @RequestBody ChangeUserPwDto changeUserPwDto) {
        if (memberService.updateUserPw(id, changeUserPwDto)) return ResponseEntity.ok("비밀번호 변경 성공");
        return new ResponseEntity<>("비밀번호 변경 실패", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteMember(@PathVariable Long id, @RequestParam String userPw) {
        if (memberService.deleteMember(id, userPw)) return ResponseEntity.ok("회원 탈퇴 성공");
        return new ResponseEntity<>("회원 탈퇴 실패", HttpStatus.BAD_REQUEST);
    }
}
