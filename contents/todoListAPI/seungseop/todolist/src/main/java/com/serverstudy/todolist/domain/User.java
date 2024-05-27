package com.serverstudy.todolist.domain;

import com.serverstudy.todolist.domain.enums.Role;
import com.serverstudy.todolist.dto.request.UserReq.UserPatch;
import jakarta.persistence.*;
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
    private User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = new HashSet<>();
        addRole(Role.USER);
    }

    public void modifyUser(UserPatch userPatch) {
        this.password = userPatch.getPassword();
        this.nickname = userPatch.getNickname();
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

}
