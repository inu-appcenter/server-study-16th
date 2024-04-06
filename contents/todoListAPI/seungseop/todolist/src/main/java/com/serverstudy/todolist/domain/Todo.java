package com.serverstudy.todolist.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition="text")
    private String describtion;

    private LocalDateTime deadline;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Progress progress;

    private boolean isDeleted;

    private LocalDateTime deletedTime;

    @ManyToOne(fetch = FetchType.LAZY)    // 안에 들어가는게 무슨 용도인지 확인
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Builder
    public Todo(String title, String describtion, LocalDateTime deadline, Priority priority, Progress progress, User user, Folder folder) {
        this.title = title;
        this.describtion = describtion;
        this.deadline = deadline;
        this.priority = priority;
        this.progress = progress;
        this.isDeleted = false;
        this.user = user;
        this.folder = folder;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeDescribtion(String describtion) {
       this.describtion = describtion;
    }

    public void changeDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void changePriority(Priority priority) {
        this.priority = priority;
    }

    public void changeProgress() {
        if (progress.equals(Progress.Todo)) progress = Progress.Doing;
        else if (progress.equals(Progress.Doing)) progress = Progress.Done;
        else progress = Progress.Todo;
    }

    public void moveToTrash() {
        isDeleted = true;
        deletedTime = LocalDateTime.now();
    }

    public void changeFolder(Folder folder) {
        this.folder = folder;
    }
}
