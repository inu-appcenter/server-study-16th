package com.serverstudy.todolist.service;

import com.serverstudy.todolist.domain.Folder;
import com.serverstudy.todolist.domain.User;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 존재하지 않습니다."));

        Folder folder = postReq.toEntity(user);

        return folderRepository.save(folder).getId();
    }

    public List<FolderDto.Response> getAllWithTodoCount(long userId) {

        User user = userRepository.getReferenceById(userId);

        List<Folder> folderList = folderRepository.findAllByUserOrderByNameAsc(user);

        return folderList.stream().map(folder -> {
            int todoCount = todoRepository.countByFolder(folder);
            return FolderDto.Response.builder()
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

        folderRepository.delete(folder);

        return folder.getId();
    }
}
