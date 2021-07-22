package com.lms.hat.thinking.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TaskRequest {
    @NotBlank(message = "Task1")
    private String name;

    private String description;

    @NotNull
    private List<String> choices;
}
