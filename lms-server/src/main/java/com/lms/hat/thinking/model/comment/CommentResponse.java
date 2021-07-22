package com.lms.hat.thinking.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private String userLogin;
    private String content;

    public static CommentResponse responseFromComment(Comment comment) {
        return new CommentResponse(comment.getUser().getLogin(), comment.getContent());
    }
}
