package com.serverstudy.todolist.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.serverstudy.todolist.domain.QTodo;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Todo> findAllByConditions(Long folderId, Long userId, Priority priority, Progress progress, Boolean isDeleted) {

        QTodo todo = QTodo.todo;
        BooleanExpression booleanExpression;

        if (folderId != null)
            booleanExpression = todo.folder.id.eq(folderId);
        else
            booleanExpression = todo.userId.eq(userId);

        if (priority != null)
            booleanExpression = booleanExpression.and(todo.priority.eq(priority));

        if (progress != null)
            booleanExpression = booleanExpression.and(todo.progress.eq(progress));

        if (isDeleted != null)
            booleanExpression = booleanExpression.and(todo.isDeleted.eq(isDeleted));

        return queryFactory.selectFrom(todo)
                .where(booleanExpression)
                .orderBy(todo.deadline.asc(), todo.priority.asc())
                .fetch();
    }
}