package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.comment.AddCommentReq;
import com.appcenter.practice.dto.reqeust.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.comment.ReadCommentRes;
import com.appcenter.practice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping(value = "/comments")
    public ResponseEntity<CommonResponse<List<ReadCommentRes>>>getCommentList(){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","댓글리스트 조회 성공",commentService.getCommentList()));
    }

    @GetMapping(value = "/comments/{id}")
    public ResponseEntity<CommonResponse<ReadCommentRes>>getComment(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","댓글 조회 성공",commentService.getComment(id)));
    }


    @PostMapping(value = "/todos/{id}/comments")
    public ResponseEntity<CommonResponse<Long>> addComment(@PathVariable("id") Long todoId, @RequestBody AddCommentReq reqDto){
        return ResponseEntity
                .status(201)
                .body(CommonResponse.of("Created","댓글 생성 성공",commentService.saveComment(todoId,reqDto)));
    }

    @PatchMapping(value = "/comments/{id}")
    public ResponseEntity<CommonResponse<Long>> updateComment(@PathVariable Long id, @RequestBody UpdateCommentReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","댓글 수정 성공",commentService.updateComment(id,reqDto)));
    }

    @DeleteMapping(value = "/comments/{id}")
    public ResponseEntity<CommonResponse<Long>> deleteComment(@PathVariable Long id){
        return ResponseEntity
                .ok(CommonResponse.of("Ok","댓글 삭제 성공",commentService.deleteComment(id)));
    }
}
