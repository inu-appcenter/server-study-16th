package com.todolist.todolist.dto.todo;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.domain.Todo;
import com.todolist.todolist.dto.EntityMapper;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface TodoMapper extends EntityMapper<TodoRequestDto,TodoResponseDto, Todo> {

    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    @Override
    @Mapping(target = "member", ignore = true)
    Todo toEntity(final TodoRequestDto todoRequestDto);

    @Override
    @Mapping(target = "memberId", source = "member.id")
    TodoResponseDto toDto(final Todo todo);
}
