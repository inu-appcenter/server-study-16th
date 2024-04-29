package com.jiyunio.todolist.member;

import com.jiyunio.todolist.member.dto.ChangeUserPwDto;
import com.jiyunio.todolist.member.dto.SignInDto;
import com.jiyunio.todolist.member.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean signUp(SignUpDto signUpDto) {
        if (memberRepository.existsByUserEmail(signUpDto.getUserEmail())) { // 이메일 존재
            return false;
        }
        if (signUpDto.getUserPw().equals(signUpDto.getConfirmUserPw())) { // 비밀번호 확인이 맞음
            Member member = Member.builder()
                    .userId(signUpDto.getUserId())
                    .userPw(signUpDto.getUserPw())
                    .userEmail(signUpDto.getUserEmail())
                    .build();
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    public boolean signIn(SignInDto signInDto) {
        if (memberRepository.existsByUserId(signInDto.getUserId())) { // 아이디 존재
            Member member = memberRepository.findByUserId(signInDto.getUserId()).get();
            if (member.getUserPw().equals(signInDto.getUserPw())) { // 비밀번호 맞음
                return true; // 성공
            }
        }
        return false;
    }

    public boolean updateUserPw(Long id, ChangeUserPwDto changeUserPwDto) {
        Member member = memberRepository.findById(id).get();
        if (member.getUserPw().equals(changeUserPwDto.getUserPw())) { // 회원 비밀번호 확인
            if (changeUserPwDto.getChangePw().equals(changeUserPwDto.getConfirmChangePw())) {
                // confirm password
                member.updateUserPw(changeUserPwDto.getChangePw());
                memberRepository.save(member);
                return true;
            }
            return false; // NOT_CONFIRM_PASSWORD
        }
        return false; // NOT_SAME_USERPW
    }

    public boolean deleteMember(Long id, String userPw) {
        Member member = memberRepository.findById(id).get();
        if (member.getUserPw().equals(userPw)) {
            memberRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
