package com.appcenter.practice.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // boolean은 null값을 가질 수 없다. 기본적으로 false값을 가진다. Boolean 클래스 객체는 null값을 가질 수 있다.
    private boolean completed;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "toDo",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList= new ArrayList<>();

    @Builder
    public ToDo(String content, Member member) {
        this.content = content;
        this.member = member;
    }

    public void changeCompleted(boolean completed){
        this.completed = true;
    }
    public void changeContent(String content){
        this.content= content;
    }
}
