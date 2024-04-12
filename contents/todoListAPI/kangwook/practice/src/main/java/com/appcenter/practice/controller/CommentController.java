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

    @GetMapping(value = "/")
    public ResponseEntity<List<ReadCommentRes>>getCommentList(){
        return ResponseEntity.ok(commentService.getCommentList());
    }



    @PostMapping(value = "/")
    public ResponseEntity<?> addComment(Long todoId, @RequestBody AddCommentReq reqDto){
        commentService.saveComment(todoId,reqDto);
        return ResponseEntity.ok(reqDto);
    }

    @PatchMapping(value = "/")
    public ResponseEntity<?> updateComment(UpdateCommentReq reqDto){
        return ResponseEntity.ok(commentService.updateComment(reqDto));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> deletedTrue(@PathVariable Long id){
        return ResponseEntity.ok(commentService.deletedTrue(id));
    }
}
