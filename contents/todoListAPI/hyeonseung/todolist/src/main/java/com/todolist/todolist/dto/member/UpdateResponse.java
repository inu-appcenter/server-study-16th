package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponse {

    private Long id;
    private String name;
    private String loginId;
    private String password;
    private LocalDateTime modifiedAt;

    public UpdateResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.password = member.getPassword();
        this.modifiedAt = member.getModifiedAt();
    }
}
