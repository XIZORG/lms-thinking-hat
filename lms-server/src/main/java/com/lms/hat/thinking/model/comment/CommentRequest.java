package com.lms.hat.thinking.model.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentRequest {
    @NotBlank(message = "this is the best course I ever seen!")
    String content;
}
