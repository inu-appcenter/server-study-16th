package com.jiyunio.todolist.member;

import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import com.jiyunio.todolist.member.dto.ChangeUserPwDTO;
import com.jiyunio.todolist.member.dto.SignInDTO;
import com.jiyunio.todolist.member.dto.SignUpDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public String signUp(@Valid SignUpDTO signUpDto) {
        if (memberRepository.existsByUserEmail(signUpDto.getUserEmail())) {
            // 이미 존재하는 이메일
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.EXIST_EMAIL);
        }

        if (memberRepository.existsByUserId(signUpDto.getUserId())) {
            // 이미 존재하는 아이디
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.EXIST_USERID);
        }

        if (signUpDto.getUserPw().equals(signUpDto.getConfirmUserPw())) {
            // 회원가입 성공
            Member member = Member.builder()
                    .userId(signUpDto.getUserId())
                    .userPw(signUpDto.getUserPw())
                    .userEmail(signUpDto.getUserEmail())
                    .build();
            memberRepository.save(member);
            return member.getUserId();
        }
        // 비밀번호 불일치
        throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_SAME_CONFIRM_PASSWORD);
    }

    public String signIn(@Valid SignInDTO signInDto) {
        if (memberRepository.existsByUserId(signInDto.getUserId())) {
            Member member = memberRepository.findByUserId(signInDto.getUserId()).get();
            if (member.getUserPw().equals(signInDto.getUserPw())) {
                // 로그인 성공
                return member.getUserId();
            }
            // 회원의 비밀번호와 불일치
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
        }
        // 아이디 없음
        throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
    }

    public void updateUserPw(Long id, @Valid ChangeUserPwDTO changeUserPwDto) {
        Member member = memberRepository.findById(id).get();
        if (member.getUserPw().equals(changeUserPwDto.getUserPw())) { // 회원 비밀번호 확인
            if (changeUserPwDto.getChangePw().equals(changeUserPwDto.getConfirmChangePw())) {
                // 비밀번호 업데이트 성공
                member.updateUserPw(changeUserPwDto.getChangePw());
                memberRepository.save(member);
            }
            // 변경 비밀번호 불일치
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_SAME_CONFIRM_PASSWORD);
        }
        // 회원의 비밀번호와 불일치
        throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
    }

    public void deleteMember(Long id, String userPw) {
        Member member = memberRepository.findById(id).get();
        if (member.getUserPw().equals(userPw)) {
            // 회원 탈퇴 성공
            memberRepository.deleteById(id);
        }
        // 비밀번호 불일치
        throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
    }
}
