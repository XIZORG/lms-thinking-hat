package com.lms.hat.thinking.model.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResultResponse {
    private String taskName;
    private String username;
    private String courseName;
    private String result;

    public static TaskResultResponse responseFromTaskResult(TaskResult taskResult) {
        TaskResultResponse trr = new TaskResultResponse(taskResult.getTask().getName(),
                taskResult.getUser().getLogin(),
                taskResult.getCourse().getName(),
                taskResult.getStatus().name());
        return trr;
    }
}
