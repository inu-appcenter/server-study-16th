package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPost;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.exception.CustomException;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.serverstudy.todolist.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    private void isNameDuplicated(String name, Long userId) {

        if (folderRepository.existsByNameAndUserId(name, userId)) {
            throw new CustomException(DUPLICATE_FOLDER_NAME);
        }
    }

    @Transactional
    public long create(FolderPost folderPost, Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }

        isNameDuplicated(folderPost.getName(), userId);

        Folder folder = folderPost.toEntity(userId);

        return folderRepository.save(folder).getId();
    }

    public List<FolderRes> getAllWithTodoCount(Long userId) {

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
    public long modify(FolderPatch folderPatch, Long folderId) {

        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new CustomException(FOLDER_NOT_FOUND));

        isNameDuplicated(folderPatch.getName(), folder.getUserId());

        folder.changeName(folderPatch.getName());

        return folder.getId();
    }

    @Transactional
    public void delete(Long folderId) {

        folderRepository.findById(folderId).ifPresent(folder -> {
            // 투두 리스트에서 폴더 null 값으로 변경
            List<Todo> todoList = todoRepository.findAllByFolder(folder);
            todoList.forEach(todo -> todo.changeFolder(null));

            folderRepository.delete(folder);
        });
    }
}
