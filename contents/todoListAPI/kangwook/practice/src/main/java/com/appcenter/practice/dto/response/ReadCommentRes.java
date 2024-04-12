package com.appcenter.practice.dto.response;


import com.appcenter.practice.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadCommentRes {
    private Long id;
    private String content;
    private Boolean deleted;

    public static ReadCommentRes from(Comment comment){
        return ReadCommentRes.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .deleted(comment.getDeleted())
                .build();
    }
}
