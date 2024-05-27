package com.todolist.todolist.security;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.repository.MemberRepository;
import com.todolist.todolist.validators.BaseException;
import com.todolist.todolist.validators.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberOptional = memberRepository.findByLoginId(username);
        if(!memberOptional.isPresent()){
            throw new BaseException(ErrorCode.NOT_EXIST_ID);
        }

        Member member = memberOptional.get();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole());
        return new
                User(member.getLoginId(),member.getPassword(), Collections.singletonList(authority));

    }
}
