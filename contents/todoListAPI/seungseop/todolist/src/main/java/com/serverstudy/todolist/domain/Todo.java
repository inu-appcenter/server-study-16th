package com.serverstudy.todolist.domain;

import com.serverstudy.todolist.dto.TodoDto;
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

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Builder
    protected Todo(String title, String description, LocalDateTime deadline, Priority priority, Progress progress, long userId, Folder folder) {
        if (title == null) this.title = "";
        else this.title = title;

        if (description == null) this.description = "";
        this.description = description;

        this.deadline = deadline;
        this.priority = priority;
        this.progress = progress;
        this.isDeleted = false;
        this.userId = userId;
        this.folder = folder;
    }

    public void switchProgress() {
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

    public void updateTodo(TodoDto.PutReq putReq, Folder folder) {
        changeTitle(putReq.getTitle());
        changeDescription(putReq.getDescription());
        changeDeadline(putReq.getDeadline());
        changePriority(putReq.getPriority());
        changeProgress(putReq.getProgress().name());
        changeFolder(folder);
    }

    private void changeTitle(String title) {
        if (title == null) this.title = "";
        else this.title = title;
    }

    private void changeDescription(String description) {
        if (description == null) this.description = "";
        this.description = description;
    }

    private void changeDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    private void changePriority(Integer priority) {
        this.priority = priority == null ? null : Priority.getPriority(priority);
    }

    private void changeProgress(String progress) {
        this.progress = progress == null ? null : Progress.getProgress(progress);
    }

}
