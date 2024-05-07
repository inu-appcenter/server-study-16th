package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.comment.AddCommentReq;
import com.appcenter.practice.dto.reqeust.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.comment.ReadCommentRes;
import com.appcenter.practice.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping(value = "/comments")
    public ResponseEntity<CommonResponse<List<ReadCommentRes>>>getCommentList(){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_FOUND.getMessage(), commentService.getCommentList()));
    }

    @GetMapping(value = "/comments/{id}")
    public ResponseEntity<CommonResponse<ReadCommentRes>>getComment(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_FOUND.getMessage(), commentService.getComment(id)));
    }


    @PostMapping(value = "/todos/{id}/comments")
    public ResponseEntity<CommonResponse<Long>> addComment(@PathVariable("id") Long todoId, @RequestBody @Valid AddCommentReq reqDto){
        return ResponseEntity
                .status(COMMENT_CREATE.getStatus())
                .body(CommonResponse.of(COMMENT_CREATE.getMessage(), commentService.saveComment(todoId,reqDto)));
    }

    @PatchMapping(value = "/comments/{id}")
    public ResponseEntity<CommonResponse<Long>> updateComment(@PathVariable Long id, @RequestBody @Valid UpdateCommentReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_UPDATE.getMessage(), commentService.updateComment(id,reqDto)));
    }

    @DeleteMapping(value = "/comments/{id}")
    public ResponseEntity<CommonResponse<Long>> deleteComment(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_DELETE.getMessage(), commentService.deleteComment(id)));
    }
}
