package com.jiyunio.todolist.todo;

import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.todo.dto.UpdateTodoDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todoId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member  Id")
    private Member member;

    @Lob // 길이 제한 X
    private String content;

    private Boolean checked;

    private String category;

    private LocalDate writeDate;

    private LocalDate setDate;

    @Builder
    protected Todo(Member member, String content, Boolean checked, String category, LocalDate writeDate, LocalDate setDate) {
        this.member = member;
        this.content = content;
        this.checked = checked;
        this.category = category;
        this.writeDate = writeDate;
        this.setDate = setDate;
    }

    protected void updateTodo(UpdateTodoDTO updateTodoDto) {
        this.content = updateTodoDto.getContent();
        this.checked = updateTodoDto.getChecked();
        this.writeDate = updateTodoDto.getWriteDate();
        this.setDate = updateTodoDto.getSetDate();
        this.category = updateTodoDto.getCategory();
    }
}
