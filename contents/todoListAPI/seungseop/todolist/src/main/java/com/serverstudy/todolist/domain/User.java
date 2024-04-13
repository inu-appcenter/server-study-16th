package com.serverstudy.todolist.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Column
    @NotNull
    private String password;

    @Column
    @NotNull
    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Builder
    protected User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = new HashSet<>();
        addRole(Role.USER);
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

}
