package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String name;
    private String loginId;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
