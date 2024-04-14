package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.User;
import com.serverstudy.todolist.dto.UserDto;
import com.serverstudy.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public long join(UserDto.PostReq postReq) {

        String email = postReq.getEmail();

        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("해당하는 이메일이 이미 존재합니다.");

        User user = postReq.toEntity();

        return userRepository.save(user).getId();
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto.Response get(long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        return UserDto.Response.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public long modify(UserDto.PutReq putReq, long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        String password = putReq.getPassword();
        String nickname = putReq.getNickname();

        if (password != null) {
            user.changePassword(password);
        }
        if (nickname != null) {
            user.changeNickname(nickname);
        }

        return user.getId();
    }

    @Transactional
    public long delete(long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        userRepository.delete(user);

        return user.getId();
    }
}
