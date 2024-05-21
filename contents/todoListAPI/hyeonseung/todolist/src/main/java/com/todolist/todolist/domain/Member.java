package com.todolist.todolist.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    // 1:N ] N쪽이 FK가진 연관관계의 주인
    private List<Todo> todoList = new ArrayList<>();

    @Builder
    private Member(String name, String loginId, String password, List<Todo> todoList){
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.todoList = todoList;
    }



    public void updateLoginId(String loginId){
        this.loginId = loginId;
    }

    public void updatePassword(String Password){
        this.password = password;
    }

    public void updateName(String name){
        this.name = name;
    }
}
