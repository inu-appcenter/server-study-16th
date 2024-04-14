package com.todolist.todolist.dto;

public interface EntityMapper<REQUEST,RESPONSE,ENTITY> {
    ENTITY toEntity(final REQUEST request);
    RESPONSE toDto(final ENTITY entity);
}
