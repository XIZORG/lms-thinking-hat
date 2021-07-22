package com.lms.hat.thinking.controller;

import com.lms.hat.thinking.config.security.services.CustomUserDetails;
import com.lms.hat.thinking.model.task.TaskEditRequest;
import com.lms.hat.thinking.model.task.TaskPerformRequest;
import com.lms.hat.thinking.model.task.TaskResponse;
import com.lms.hat.thinking.model.task.TaskResultResponse;
import com.lms.hat.thinking.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<TaskResultResponse> performTask(@PathVariable(value = "id") Long id,
                                              @Valid @RequestBody TaskPerformRequest answer,
                                              Authentication authentication){
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        TaskResultResponse trr = taskService.performTask(id, cud.getUsername(), answer.getAnswer());
        return ResponseEntity.ok(trr);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable(value = "id") Long id,
                                                 @Valid @RequestBody TaskEditRequest request){
        return ResponseEntity.ok(taskService.editTask(id, request));
    }
}
