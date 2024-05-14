package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.domain.enums.Priority;
import com.serverstudy.todolist.domain.enums.Progress;
import com.serverstudy.todolist.dto.request.TodoReq.TodoGet;
import com.serverstudy.todolist.dto.request.TodoReq.TodoPost;
import com.serverstudy.todolist.dto.request.TodoReq.TodoPut;
import com.serverstudy.todolist.dto.response.TodoRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.exception.ErrorCode;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.serverstudy.todolist.exception.ErrorCode.FOLDER_NOT_FOUND;
import static com.serverstudy.todolist.exception.ErrorCode.TODO_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public long create(TodoPost todoPost, Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Folder folder = (todoPost.getFolderId() != null)
                ? getFolder(todoPost.getFolderId())
                : null;

        Todo todo = todoPost.toEntity(userId, folder);

        return todoRepository.save(todo).getId();
    }

    public List<TodoRes> findAllByConditions(TodoGet todoGet, Long userId) {

        Long folderId = todoGet.getFolderId();
        Priority priority = todoGet.getPriority();
        Progress progress = todoGet.getProgress();
        Boolean isDeleted = todoGet.getIsDeleted();

        List<Todo> todoList = todoRepository.findAllByConditions(folderId, userId, priority, progress, isDeleted);

        return todoList.stream().map(todo -> {

            Integer dateFromDelete = (todo.getIsDeleted())
                    ? (int) ChronoUnit.DAYS.between(todo.getDeletedTime(), LocalDateTime.now())
                    : null;

            Long foundFolderId = (todo.getFolder() != null)
                    ? todo.getFolder().getId()
                    : null;

            String folderName = (todo.getFolder() != null)
                    ? todo.getFolder().getName()
                    : null;

            return TodoRes.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .description(todo.getDescription())
                    .deadline(todo.getDeadline())
                    .priority(todo.getPriority())
                    .progress(todo.getProgress())
                    .isDeleted(todo.getIsDeleted())
                    .dateFromDelete(dateFromDelete)
                    .folderId(foundFolderId)
                    .folderName(folderName)
                    .build();
        }).toList();
    }

    @Transactional
    public long update(TodoPut todoPut, Long todoId) {

        Todo todo = getTodo(todoId);

        Folder folder = (todoPut.getFolderId() != null)
                ? getFolder(todoPut.getFolderId())
                : null;

        todo.updateTodo(todoPut, folder);

        return todo.getId();
    }

    @Transactional
    public long switchProgress(long todoId) {

        Todo todo = getTodo(todoId);

        todo.switchProgress();

        return todo.getId();
    }

    @Transactional
    public long moveFolder(Long folderId, Long todoId) {

        Todo todo = getTodo(todoId);

        Folder folder = (folderId != null)
                ? getFolder(folderId)
                : null;

        todo.changeFolder(folder);

        return todo.getId();
    }

    @Transactional
    public Long delete(Long todoId, Boolean restore) {

        Todo todo = getTodo(todoId);

        Long result;
        if (restore) {
            todo.moveToTrash();
            result = todoId;
        }
        else {
            todoRepository.delete(todo);
            result = null;
        }

        return result;
    }

    // 일정 주기로 실행할 스케줄링 메서드
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void deleteInTrash() {

        List<Todo> todoList = todoRepository.findAllByIsDeletedOrderByDeletedTimeAsc(true);

        for (Todo todo : todoList) {
            int dateFromDelete = (int) ChronoUnit.DAYS.between(todo.getDeletedTime(), LocalDateTime.now());

            if (dateFromDelete < 30) {
                break;
            }
            todoRepository.delete(todo);
        }
    }

    private Todo getTodo(Long todoId) {

        return todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));
    }

    private Folder getFolder(Long folderId) {

        return folderRepository.findById(folderId)
                .orElseThrow(() -> new CustomException(FOLDER_NOT_FOUND));
    }

}
