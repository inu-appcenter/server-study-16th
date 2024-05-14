package com.serverstudy.todolist.controller;

import com.serverstudy.todolist.dto.request.FolderReq.FolderPatch;
import com.serverstudy.todolist.dto.request.FolderReq.FolderPost;
import com.serverstudy.todolist.dto.response.FolderRes;
import com.serverstudy.todolist.service.FolderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/folder")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<Long> postFolder(@Valid @RequestBody FolderPost folderPost, @NotNull Long userId) {  // @RequestParam만 붙여도 null 값 입력 시 예외 발생

        Long folderId = folderService.create(folderPost, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(folderId);
    }

    @GetMapping
    public ResponseEntity<List<FolderRes>> getFoldersByUser(@NotNull Long userId) {

        List<FolderRes> responseList = folderService.getAllWithTodoCount(userId);

        return ResponseEntity.ok(responseList);
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<Long> patchFolder(@Valid @RequestBody FolderPatch folderPatch, @PathVariable Long folderId) {

        Long modifiedFolderId = folderService.modify(folderPatch, folderId);

        return ResponseEntity.ok(modifiedFolderId);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long folderId) {

        folderService.delete(folderId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
