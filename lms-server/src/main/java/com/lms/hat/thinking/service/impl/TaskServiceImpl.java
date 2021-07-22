package com.lms.hat.thinking.service.impl;

import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.model.task.*;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.repository.TaskRepository;
import com.lms.hat.thinking.repository.UserEntityRepository;
import com.lms.hat.thinking.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           UserEntityRepository userEntityRepository) {
        this.taskRepository = taskRepository;
        this.userEntityRepository = userEntityRepository;
    }


    @Override
    @Transactional
    public TaskResultResponse performTask(Long taskId, String login, String answer) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "task not found"))));

        TaskResult tr = new TaskResult();
        UserEntity user = userEntityRepository.findByLogin(login);

        if (task.getTaskAnswers().stream().anyMatch(data -> data.getChoice().equals(answer))) {
            tr.setStatus(Status.DONE);
        } else {
            tr.setStatus(Status.FAILED);
        }

        tr.setTask(task);
        tr.setCourse(task.getCourse());
        user.addTaskResult(tr);

        userEntityRepository.save(user);
        logger.info("User with login: {} successfully done task, id: {}", login, taskId);
        return TaskResultResponse.responseFromTaskResult(tr);
    }

    @Override
    public TaskResponse editTask(Long id, TaskEditRequest ter) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "task not found"))));
        task.setName(ter.getName());
        task.setDescription(ter.getDescription());
        return TaskResponse.responseFromTask(taskRepository.save(task));
    }
}
