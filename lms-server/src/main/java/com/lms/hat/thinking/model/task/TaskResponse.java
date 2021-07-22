package com.lms.hat.thinking.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {
    private String name;
    private String course;
    private String description;
    private Long id;

    public static TaskResponse responseFromTask(Task task) {
        return new TaskResponse(task.getName(),
                task.getCourse().getName(),
                task.getDescription(),
                task.getId());
    }
}
