package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.FolderDto;
import com.serverstudy.todolist.service.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<?> postFolder (@Valid @RequestBody FolderDto.PostReq postReq, Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        long folderId = folderService.create(postReq, userId);

        return ResponseEntity.ok(folderId);
    }

    @GetMapping
    public ResponseEntity<?> getFoldersByUser(Long userId) {

        if (userId == null) throw new IllegalArgumentException("userId 값이 비어있습니다");

        List<FolderDto.Response> responseList = folderService.getAllWithTodoCount(userId);

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<?> putFolder(@Valid @RequestBody FolderDto.PutReq putReq, @PathVariable Long folderId) {

        long modifiedFolderId = folderService.modify(putReq, folderId);

        return ResponseEntity.ok(modifiedFolderId);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long folderId) {

        long deletedFolderId = folderService.delete(folderId);

        return ResponseEntity.ok(deletedFolderId);
    }
}
