package com.serverstudy.todolist.security;

import com.serverstudy.todolist.domain.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;


@Getter
public class SecurityUser extends org.springframework.security.core.userdetails.User {

    private final User user;
    private final Long id;

    public SecurityUser(User user) {
        super(user.getEmail(), user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole()))
                        .collect(Collectors.toSet()));
        this.user = user;
        this.id = user.getId();
    }

}
