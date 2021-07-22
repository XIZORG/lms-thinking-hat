package com.lms.hat.thinking.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskPerformRequest {
    @NotBlank(message = "some answer")
    private String answer;

    public TaskPerformRequest() {
    }

    public TaskPerformRequest(@NotBlank(message = "some answer") String answer) {
        this.answer = answer;
    }
}
