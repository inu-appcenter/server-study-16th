package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.AddCommentReq;
import com.appcenter.practice.dto.reqeust.UpdateCommentReq;
import com.appcenter.practice.dto.response.ReadCommentRes;
import com.appcenter.practice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ReadCommentRes>>getCommentList(){
        return ResponseEntity.ok(commentService.getCommentList());
    }


    @PostMapping
    public ResponseEntity<?> addComment(Long todoId, @RequestBody AddCommentReq reqDto){
        return ResponseEntity
                .status(201)
                .body(commentService.saveComment(todoId,reqDto));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, UpdateCommentReq reqDto){
        return ResponseEntity.ok(commentService.updateComment(id,reqDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id){
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}
