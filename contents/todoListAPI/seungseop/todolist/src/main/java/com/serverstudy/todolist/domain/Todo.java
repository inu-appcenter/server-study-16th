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
    private String description;

    private LocalDateTime deadline;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Progress progress;

    private Boolean isDeleted;

    private LocalDateTime deletedTime;

    @ManyToOne(fetch = FetchType.LAZY)    // 안에 들어가는게 무슨 용도인지 확인
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Builder
    protected Todo(String title, String description, LocalDateTime deadline, Priority priority, Progress progress, User user, Folder folder) {
        this.title = title;
        this.description = description;
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

    public void changeDescription(String description) {
       this.description = description;
    }

    public void changeDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void changePriority(Priority priority) {
        this.priority = priority;
    }

    public void changeProgress() {
        if (this.progress.equals(Progress.Todo)) this.progress = Progress.Doing;
        else if (this.progress.equals(Progress.Doing)) this.progress = Progress.Done;
        else this.progress = Progress.Todo;
    }

    public void moveToTrash() {
        this.isDeleted = true;
        this.deletedTime = LocalDateTime.now();
    }

    public void changeFolder(Folder folder) {
        this.folder = folder;
    }
}
