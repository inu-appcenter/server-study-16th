package com.jiyunio.todolist.member;

import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import com.jiyunio.todolist.jwt.JwtDTO;
import com.jiyunio.todolist.jwt.JwtProvider;
import com.jiyunio.todolist.member.dto.ChangeUserPwDTO;
import com.jiyunio.todolist.member.dto.SignInDTO;
import com.jiyunio.todolist.member.dto.SignUpDTO;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

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
                    .userPw(passwordEncoder.encode(signUpDto.getUserPw()))
                    .userEmail(signUpDto.getUserEmail())
                    .build();
            memberRepository.save(member);
            return member.getUserId();
        } else {
            // 비밀번호 불일치
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_SAME_CONFIRM_PASSWORD);
        }
    }

    public JwtDTO signIn(@Valid SignInDTO signInDto) {
//        if (memberRepository.existsByUserId(signInDto.getUserId())) {
//            Member member = memberRepository.findByUserId(signInDto.getUserId()).get();
//            if (passwordEncoder.matches(signInDto.getUserPw(), member.getUserPw())) {
                // 로그인 성공
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDto.getUserId(), signInDto.getUserPw());
                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                return jwtProvider.createToken(signInDto.getUserId());
//
//            }
//            // 회원의 비밀번호와 불일치
//            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
//        }
//        // 아이디 없음
//            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
    }

    public void updateUserPw(Long id, @Valid ChangeUserPwDTO changeUserPwDto) {
        Member member = memberRepository.findById(id).get();
        if (member.getUserPw().equals(changeUserPwDto.getUserPw())) { // 회원 비밀번호 확인
            if (changeUserPwDto.getChangePw().equals(changeUserPwDto.getConfirmChangePw())) {
                // 비밀번호 업데이트 성공
                member.updateUserPw(changeUserPwDto.getChangePw());
                memberRepository.save(member);
            } else {
                // 변경 비밀번호 불일치
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_SAME_CONFIRM_PASSWORD);
            }
        } else {
            // 회원의 비밀번호와 불일치
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
        }
    }

    public void deleteMember(Long id, String userPw) {
        Member member = memberRepository.findById(id).get();
        if (member.getUserPw().equals(userPw)) {
            // 회원 탈퇴 성공
            memberRepository.deleteById(id);
        } else { // 비밀번호 불일치
            throw new CustomException(HttpStatus.NOT_FOUND, ErrorCode.WRONG_USERID_PASSWORD);
        }
    }
}