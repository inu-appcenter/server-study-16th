package com.jiyunio.todolist.member;

import com.jiyunio.todolist.ResponseDTO;
import com.jiyunio.todolist.member.dto.ChangeUserPwDTO;
import com.jiyunio.todolist.member.dto.SignInDTO;
import com.jiyunio.todolist.member.dto.SignUpDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDTO signUpDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ResponseDTO> responseDTOList = returnBindingResult(bindingResult);
            return new ResponseEntity<>(responseDTOList, HttpStatus.BAD_REQUEST);
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .userId(memberService.signUp(signUpDto)) // 화면에 "ㅇㅇ님 환영합니다" 글씨 원함
                .msg("회원가입 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDTO signInDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ResponseDTO> responseDTOList = returnBindingResult(bindingResult);
            return new ResponseEntity<>(responseDTOList, HttpStatus.BAD_REQUEST);
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .userId(memberService.signIn(signInDto)) // 로그인하면 회원 페이지에 ㅇㅇ님 원함
                .msg("로그인 성공")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUserPw(@PathVariable Long id, @Valid @RequestBody ChangeUserPwDTO changeUserPwDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ResponseDTO> responseDTOList = returnBindingResult(bindingResult);
            return new ResponseEntity<>(responseDTOList, HttpStatus.BAD_REQUEST);
        }

        memberService.updateUserPw(id, changeUserPwDto);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("비밀번호 변경 성공")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDTO> deleteMember(@PathVariable Long id, @RequestParam String userPw) {
        memberService.deleteMember(id, userPw);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("회원 탈퇴 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }

    public List<ResponseDTO> returnBindingResult(BindingResult bindingResult) {
        List<FieldError> list = bindingResult.getFieldErrors();
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        for (FieldError error : list) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .msg(error.getDefaultMessage())
                    .build();
            responseDTOList.add(responseDTO);
        }
        return responseDTOList;
    }
}
