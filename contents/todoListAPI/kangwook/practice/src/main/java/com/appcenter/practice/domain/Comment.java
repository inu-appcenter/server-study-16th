package com.appcenter.practice.domain;


import com.appcenter.practice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    //삭제 여부에 따라 "삭제된 댓글입니다." 작성
    private Boolean deleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toDo_Id",nullable = false)
    private Todo toDo;


    @Builder
    private Comment(String content,Boolean deleted, Todo toDo) {
        this.content = content;
        this.deleted = deleted;
        this.toDo = toDo;
    }

    public void changeDeleted(Boolean deleted){ this.deleted=deleted;}
    public void changeContent(String content){ this.content=content; }
}
