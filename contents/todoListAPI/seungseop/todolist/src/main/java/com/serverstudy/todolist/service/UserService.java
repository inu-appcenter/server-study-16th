package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.User;
import com.serverstudy.todolist.dto.request.UserReq.UserPost;
import com.serverstudy.todolist.dto.request.UserReq.UserPut;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
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
    private final TodoRepository todoRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public long join(UserPost userPost) {

        String email = userPost.getEmail();

        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("해당하는 이메일이 이미 존재합니다.");

        User user = userPost.toEntity();

        return userRepository.save(user).getId();
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserRes get(Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다.");
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        return UserRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public long modify(UserPut userPut, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다.");
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        String password = userPut.getPassword();
        String nickname = userPut.getNickname();

        if (password != null) {
            user.changePassword(password);
        }
        if (nickname != null) {
            user.changeNickname(nickname);
        }

        return user.getId();
    }

    @Transactional
    public void delete(Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다.");
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        // 투두 리스트와 폴더 삭제
        todoRepository.deleteAll(todoRepository.findAllByUserId(userId));
        folderRepository.deleteAll(folderRepository.findAllByUserId(userId));

        userRepository.delete(user);
    }
}
