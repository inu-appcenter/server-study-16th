package com.appcenter.practice.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toDo_Id",nullable = false)
    private ToDo toDo;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child=new ArrayList<>();


    public Comment(String content, int level, Member member, ToDo toDo, Comment parent) {
        this.content = content;
        this.level = level;
        this.member = member;
        this.toDo = toDo;
        this.parent=parent;

    }

    public void changwDeleted(boolean deleted){ this.deleted=true;}
    public void changeContent(String content){ this.content=content; }
}
