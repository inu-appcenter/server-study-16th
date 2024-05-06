package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPost;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @Operation
    @PostMapping
    public ResponseEntity<?> postFolder (@Valid @RequestBody FolderPost folderPost, Long userId) {

        long folderId = folderService.create(folderPost, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(folderId);
    }

    @GetMapping
    public ResponseEntity<?> getFoldersByUser(Long userId) {

        List<FolderRes> responseList = folderService.getAllWithTodoCount(userId);

        return ResponseEntity.ok(responseList);
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<?> patchFolder(@Valid @RequestBody FolderPatch folderPatch, @PathVariable Long folderId) {

        long modifiedFolderId = folderService.modify(folderPatch, folderId);

        return ResponseEntity.ok(modifiedFolderId);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long folderId) {

        folderService.delete(folderId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
