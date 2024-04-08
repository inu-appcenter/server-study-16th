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
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int level;

    //삭제 여부에 따라 "삭제된 댓글입니다." 작성
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toDo_Id",nullable = false)
    private ToDo toDo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Comment> children= new ArrayList<>();


    @Builder
    public Comment(String content, int level, Member member, ToDo toDo, Comment parent) {
        this.content = content;
        this.level = level;
        this.member = member;
        this.toDo = toDo;
        this.parent=parent;
    }

    public void changeDeleted(){ this.deleted=true;}
    public void changeContent(String content){ this.content=content; }
}
