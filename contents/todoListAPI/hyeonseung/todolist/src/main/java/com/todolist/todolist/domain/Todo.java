package com.todolist.todolist.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String contents;

    @ColumnDefault("false")
    private Boolean isCompleted = false;

    private LocalDateTime dueAt;

    // 할일을 작성한 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    protected Todo(String title, String contents, Boolean isCompleted, LocalDateTime dueAt, Member member) {
        this.title = title;
        this.contents = contents;
        this.isCompleted = isCompleted;
        this.dueAt = dueAt;
        this.member = member;
    }

    public void matchMember(Member member){
        this.member = member;
    }

    public void updateTitle(String title) {
        if(title != null) this.title = title;
    }

    public void updateContents(String contents) {
        if(contents != null) this.contents = contents;
    }

    public void updateIsCompleted(Boolean isCompleted) {
        if(isCompleted != null) this.isCompleted = isCompleted;
    }

    public void updateDueAt(LocalDateTime dueAt) {
        if(dueAt != null) this.dueAt = dueAt;
    }

}