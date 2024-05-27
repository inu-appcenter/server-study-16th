package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.User;
import com.serverstudy.todolist.domain.enums.Role;
import com.serverstudy.todolist.dto.request.UserReq.UserLoginPost;
import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import com.serverstudy.todolist.dto.request.UserReq.UserPost;
import com.serverstudy.todolist.dto.response.JwtRes;
import com.serverstudy.todolist.dto.response.UserRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.exception.ErrorCode;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import com.serverstudy.todolist.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.serverstudy.todolist.exception.ErrorCode.DUPLICATE_USER_EMAIL;
import static com.serverstudy.todolist.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final FolderRepository folderRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtRes join(UserPost userPost) {

        checkEmailDuplicated(userPost.getEmail());

        String encodedPassword = passwordEncoder.encode(userPost.getPassword());

        User user = userRepository.save(
                userPost.toEntity(encodedPassword)
        );

        return JwtRes.builder()
                .accessToken(jwtTokenProvider.createToken(user.getEmail()))
                .build();
    }

    public void checkEmailDuplicated(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATE_USER_EMAIL);
        }
    }

    public JwtRes login (UserLoginPost userLoginPost) {

        // 이메일 불일치
        User user = userRepository.findByEmail(userLoginPost.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_CREDENTIALS));
        // 비밀번호 불일치
        if (!passwordEncoder.matches(userLoginPost.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.BAD_CREDENTIALS);
        }

        return JwtRes.builder()
                .accessToken(jwtTokenProvider.createToken(user.getEmail()))
                .build();
    }

    public UserRes get(Long userId) {

        User user = getUser(userId);

        return UserRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public UserRes modify(UserPatch userPatch, Long userId) {

        User user = getUser(userId);

        user.modifyUser(userPatch);

        return UserRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    @Transactional
    public void delete(Long userId) {

        // 투두 리스트와 폴더 삭제
        todoRepository.deleteAll(todoRepository.findAllByUserId(userId));
        folderRepository.deleteAll(folderRepository.findAllByUserId(userId));

        // 유저 삭제
        userRepository.deleteById(userId);
    }

    @Transactional
    public JwtRes getAdmin() {

        User admin = userRepository.findByEmail("ADMIN").orElseGet(() -> {
            User user = User.builder().email("ADMIN").nickname("ADMIN").password("ADMIN").build();
            user.addRole(Role.ADMIN);
            return userRepository.save(user);
        });

        return JwtRes.builder()
                .accessToken(jwtTokenProvider.createToken(admin.getEmail()))
                .build();
    }

    public List<UserRes> getAll() {

        return userRepository.findAll().stream()
                .filter(user -> !user.getEmail().equals("ADMIN"))   // 관리자 제외
                .map(user -> UserRes.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .build()
        ).toList();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }
}
