package com.todolist.todolist.dto.todo;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.domain.Todo;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-27T17:08:26+0900",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.10 (Oracle Corporation)"
)
public class TodoMapperImpl implements TodoMapper {

    @Override
    public Todo toEntity(TodoRequestDto todoRequestDto) {
        if ( todoRequestDto == null ) {
            return null;
        }

        Todo.TodoBuilder todo = Todo.builder();

        todo.title( todoRequestDto.getTitle() );
        todo.contents( todoRequestDto.getContents() );
        todo.isCompleted( todoRequestDto.getIsCompleted() );
        todo.dueAt( todoRequestDto.getDueAt() );

        return todo.build();
    }

    @Override
    public TodoResponseDto toDto(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoResponseDto.TodoResponseDtoBuilder todoResponseDto = TodoResponseDto.builder();

        todoResponseDto.memberId( todoMemberId( todo ) );
        todoResponseDto.id( todo.getId() );
        todoResponseDto.title( todo.getTitle() );
        todoResponseDto.contents( todo.getContents() );
        if ( todo.getIsCompleted() != null ) {
            todoResponseDto.isCompleted( todo.getIsCompleted() );
        }
        todoResponseDto.dueAt( todo.getDueAt() );

        return todoResponseDto.build();
    }

    private Long todoMemberId(Todo todo) {
        if ( todo == null ) {
            return null;
        }
        Member member = todo.getMember();
        if ( member == null ) {
            return null;
        }
        Long id = member.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
