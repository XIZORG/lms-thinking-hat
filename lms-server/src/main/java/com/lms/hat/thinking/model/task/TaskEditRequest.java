package com.lms.hat.thinking.model.task;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskEditRequest {
    @NotBlank(message = "Task1")
    private String name;

    private String description;
}
