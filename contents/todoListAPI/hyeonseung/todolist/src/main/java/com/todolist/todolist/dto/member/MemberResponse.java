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


    public MemberResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.password = member.getPassword();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }
}
