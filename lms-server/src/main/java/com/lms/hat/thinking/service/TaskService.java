package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.task.TaskEditRequest;
import com.lms.hat.thinking.model.task.TaskResponse;
import com.lms.hat.thinking.model.task.TaskResultResponse;

public interface TaskService {
    TaskResultResponse performTask(Long taskId, String userLogin, String answer);
    TaskResponse editTask(Long id, TaskEditRequest ter);
}
