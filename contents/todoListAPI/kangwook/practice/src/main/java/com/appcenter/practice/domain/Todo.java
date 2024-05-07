package com.appcenter.practice.domain;


import com.appcenter.practice.common.BaseEntity;
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
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // boolean은 null값을 가질 수 없다. 기본적으로 false값을 가진다. Boolean 클래스 객체는 null값을 가질 수 있다.
    private Boolean completed;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "toDo",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> commentList= new ArrayList<>();

    @Builder
    private Todo(String content,Boolean completed, Member member) {
        this.content = content;
        this.completed = completed;
        this.member = member;
    }

    public void changeCompleted(Boolean completed){
        this.completed = completed;
    }
    public void changeContent(String content){
        this.content= content;
    }
}
