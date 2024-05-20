package com.jiyunio.todolist.member;

import com.jiyunio.todolist.ResponseDTO;
import com.jiyunio.todolist.member.dto.ChangeUserPwDTO;
import com.jiyunio.todolist.member.dto.SignInDTO;
import com.jiyunio.todolist.member.dto.SignUpDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 API")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signUp")
    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이메일 이용\n\n 아이디 : 5 ~ 10자 \n\n 비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpDto) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .result(memberService.signUp(signUpDto)) // 화면에 "ㅇㅇ님 환영합니다" 글씨 원함
                .msg("회원가입 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    @Operation(summary = "로그인", description = "아이디와 비밀번호 이용")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDTO signInDto) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .result(memberService.signIn(signInDto)) // 로그인하면 회원 페이지에 ㅇㅇ님 원함
                .msg("로그인 성공")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "회원 비밀번호 수정", description = "비밀번호, 수정 비밀번호 이용")
    public ResponseEntity<?> updateUserPw(@Parameter(description = "member의 id") @PathVariable Long id, @Valid @RequestBody ChangeUserPwDTO changeUserPwDto) {
        memberService.updateUserPw(id, changeUserPwDto);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("비밀번호 변경 성공")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "회원 탈퇴", description = "비밀번호 이용")
    public ResponseEntity<ResponseDTO> deleteMember(@Parameter(description = "member의 id") @PathVariable Long id, @RequestParam String userPw) {
        memberService.deleteMember(id, userPw);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("회원 탈퇴 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }
}
