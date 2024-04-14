package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.Todo;
import com.serverstudy.todolist.dto.FolderDto;
import com.serverstudy.todolist.repository.FolderRepository;
import com.serverstudy.todolist.repository.TodoRepository;
import com.serverstudy.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;

    private final UserRepository userRepository;

    private final TodoRepository todoRepository;

    @Transactional
    public long create(FolderDto.PostReq postReq, long userId) {

        //User user = userRepository.getReferenceById(userId);
        if (!userRepository.existsById(userId)) throw new NoSuchElementException("해당하는 유저가 존재하지 않습니다.");

        Folder folder = postReq.toEntity(userId);

        return folderRepository.save(folder).getId();
    }

    public List<FolderDto.Response> getAllWithTodoCount(long userId) {

        List<Folder> folderList = folderRepository.findAllByUserIdOrderByNameAsc(userId);

        return folderList.stream().map(folder -> {
            int todoCount = todoRepository.countByFolder(folder);
            return FolderDto.Response.builder()
                    .folderId(folder.getId())
                    .name(folder.getName())
                    .todoCount(todoCount)
                    .build();
        }).toList();
    }

    @Transactional
    public long modify(FolderDto.PutReq putReq, long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NoSuchElementException("해당하는 폴더가 존재하지 않습니다."));

        folder.changeName(putReq.getName());

        return folder.getId();
    }

    @Transactional
    public long delete(long folderId) {

        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NoSuchElementException("해당하는 폴더가 존재하지 않습니다."));

        // 투두 리스트에서 폴더 null 값으로 변경
        List<Todo> todoList = todoRepository.findAllByFolder(folder);
        todoList.forEach(todo -> todo.changeFolder(null));
        
        folderRepository.delete(folder);

        return folder.getId();
    }
}
