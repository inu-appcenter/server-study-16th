package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Priority;
import com.serverstudy.todolist.domain.Progress;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.dto.TodoDto;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public long create(TodoDto.PostReq postReq, long userId) {

        //User user = userRepository.getReferenceById(userId);
        if (!userRepository.existsById(userId)) throw new NoSuchElementException("해당하는 유저가 존재하지 않습니다.");

        Folder folder = null;

        if (postReq.getFolderId() != null)
            folder = folderRepository.findById(postReq.getFolderId()).orElse(null);

        Todo todo = postReq.toEntity(userId, folder);

        return todoRepository.save(todo).getId();
    }

    public List<TodoDto.Response> findAllByConditions(TodoDto.GetReq getReq, long userId) {

        Long folderId = getReq.getFolderId();
        Priority priority = getReq.getPriority() == null ? null : Priority.getPriority(0);
        Progress progress = getReq.getProgress() == null ? null : Progress.getProgress(getReq.getProgress().name());
        boolean isDeleted = getReq.getIsDeleted() != null && getReq.getIsDeleted();

        List<Todo> todoList = todoRepository.findAllByConditions(folderId, userId, priority, progress, isDeleted);

        return todoList.stream().map(todo -> {
            Integer dateFromDelete;
            if (todo.getIsDeleted())
                dateFromDelete = (int) ChronoUnit.DAYS.between(todo.getDeletedTime(), LocalDateTime.now());
            else
                dateFromDelete = null;

            Long foundFolderId;
            String folderName;
            if (todo.getFolder() != null){
                foundFolderId = todo.getFolder().getId();
                folderName = todo.getFolder().getName();
            }
            else {
                foundFolderId = null;
                folderName = null;
            }

            return TodoDto.Response.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .description(todo.getDescription())
                    .deadline(todo.getDeadline())
                    .priority(todo.getPriority().getNumber())
                    .progress(todo.getProgress().name())
                    .isDeleted(todo.getIsDeleted())
                    .dateFromDelete(dateFromDelete)
                    .folderId(foundFolderId)
                    .folderName(folderName)
                    .build();
        }).toList();
    }

    @Transactional
    public long update(TodoDto.PutReq putReq, long todoId, long userId) {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new NoSuchElementException("해당하는 todo가 존재하지 않습니다."));

        if (!todo.getUserId().equals(userId)) throw new IllegalArgumentException("해당 유저와 todo 작성자가 동일하지 않습니다. ");

        Folder folder = null;

        if (putReq.getFolderId() != null)
            folder = folderRepository.findById(putReq.getFolderId()).orElseThrow(() -> new NoSuchElementException("해당하는 folder가 존재하지 않습니다."));

        todo.updateTodo(putReq, folder);

        return todo.getId();
    }

    @Transactional
    public long switchProgress(long todoId, long userId) {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new NoSuchElementException("해당하는 todo가 존재하지 않습니다."));

        if (!todo.getUserId().equals(userId)) throw new IllegalArgumentException("해당 유저와 todo 작성자가 동일하지 않습니다. ");

        todo.switchProgress();

        return todo.getId();
    }

    @Transactional
    public long moveFolder(Long folderId, long todoId, long userId) {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new NoSuchElementException("해당하는 todo가 존재하지 않습니다."));
        if (!todo.getUserId().equals(userId)) throw new IllegalArgumentException("해당 유저와 todo 작성자가 동일하지 않습니다.");

        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NoSuchElementException("해당하는 folder가 존재하지 않습니다."));

        todo.changeFolder(folder);

        return todo.getId();
    }

    @Transactional
    public long moveToTrash(long todoId, long userId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new NoSuchElementException("해당하는 todo가 존재하지 않습니다."));

        if (!todo.getUserId().equals(userId)) throw new IllegalArgumentException("해당 유저와 todo 작성자가 동일하지 않습니다. ");

        todo.moveToTrash();

        return todo.getId();
    }

    // 일정 주기로 실행할 스케줄링 메서드
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void deleteInTrash() {
        // 현재 시간을 체크하여 자정에만 실행되도록 함
        LocalTime currentTime = LocalTime.now();

        List<Todo> todoList = todoRepository.findAllByIsDeletedOrderByDeletedTimeAsc(true);

        for (Todo todo : todoList) {
            int dateFromDelete = (int) ChronoUnit.DAYS.between(todo.getDeletedTime(), LocalDateTime.now());

            if (dateFromDelete < 30) break;
            todoRepository.delete(todo);
        }
    }

}
