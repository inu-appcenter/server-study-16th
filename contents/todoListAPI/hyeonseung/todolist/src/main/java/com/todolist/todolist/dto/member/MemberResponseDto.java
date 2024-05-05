package com.todolist.todolist.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MemberResponseDto {

    private Long id;
    private String name;
    private String loginId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


}
