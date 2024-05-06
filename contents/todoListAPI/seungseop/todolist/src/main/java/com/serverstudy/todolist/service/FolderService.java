package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.dto.request.FolderReq;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.serverstudy.todolist.exception.ErrorCode.DUPLICATE_FOLDER_NAME;
import static com.serverstudy.todolist.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    private void checkName(String name, long userId) {
        if (folderRepository.existsByNameAndUserId(name, userId))
            throw new CustomException(DUPLICATE_FOLDER_NAME);
    }

    @Transactional
    public long create(FolderReq.FolderPost folderPost, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다.");
        if (!userRepository.existsById(userId)) throw new CustomException(USER_NOT_FOUND);

        checkName(folderPost.getName(), userId);

        Folder folder = folderPost.toEntity(userId);

        return folderRepository.save(folder).getId();
    }

    public List<FolderRes> getAllWithTodoCount(Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다.");

        List<Folder> folderList = folderRepository.findAllByUserIdOrderByNameAsc(userId);

        return folderList.stream().map(folder -> {
            int todoCount = todoRepository.countByFolder(folder);
            return FolderRes.builder()
                    .folderId(folder.getId())
                    .name(folder.getName())
                    .todoCount(todoCount)
                    .build();
        }).toList();
    }

    @Transactional
    public long modify(FolderPatch folderPatch, long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NoSuchElementException("해당하는 폴더가 존재하지 않습니다."));

        checkName(folderPatch.getName(), folder.getUserId());

        folder.changeName(folderPatch.getName());

        return folder.getId();
    }

    @Transactional
    public void delete(long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NoSuchElementException("해당하는 폴더가 존재하지 않습니다."));

        // 투두 리스트에서 폴더 null 값으로 변경
        List<Todo> todoList = todoRepository.findAllByFolder(folder);
        todoList.forEach(todo -> todo.changeFolder(null));

        folderRepository.delete(folder);
    }
}
