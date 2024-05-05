package com.serverstudy.todolist.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "folder_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private Long userId;

    @Builder
    private Folder(String name, long userId) {
        this.name = name;
        this.userId = userId;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
