package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String name;
    private String loginId;
    private String password;


}
