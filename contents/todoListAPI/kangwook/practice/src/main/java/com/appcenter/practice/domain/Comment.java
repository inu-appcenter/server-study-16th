package com.appcenter.practice.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    //삭제 여부에 따라 "삭제된 댓글입니다." 작성
    private boolean deleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toDo_Id",nullable = false)
    private Todo toDo;


    @Builder
    private Comment(String content, Member member, Todo toDo) {
        this.content = content;
        this.member = member;
        this.toDo = toDo;
    }

    public void changeDeleted(){ this.deleted=true;}
    public void changeContent(String content){ this.content=content; }
}
