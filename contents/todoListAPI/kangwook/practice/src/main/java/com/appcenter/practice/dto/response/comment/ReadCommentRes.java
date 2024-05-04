package com.appcenter.practice.dto.response.comment;


import com.appcenter.practice.domain.Comment;
import lombok.Getter;

@Getter
public class ReadCommentRes {
    private final Long id;
    private final String content;
    private final Boolean deleted;

    private ReadCommentRes(Long id, String content, Boolean deleted) {
        this.id = id;
        this.content = content;
        this.deleted = deleted;
    }

    public static ReadCommentRes from(Comment comment){
        return new ReadCommentRes(comment.getId(),comment.getContent(),comment.getDeleted());
    }
}
