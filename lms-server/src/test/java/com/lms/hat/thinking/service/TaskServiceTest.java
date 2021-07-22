package com.lms.hat.thinking.service;

import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.task.*;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.repository.TaskRepository;
import com.lms.hat.thinking.repository.UserEntityRepository;
import com.lms.hat.thinking.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    private TaskRepository taskRepository;
    private UserEntityRepository userEntityRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        userEntityRepository = mock(UserEntityRepository.class);
        taskService = new TaskServiceImpl(taskRepository,userEntityRepository);
    }

    @Test
    void testPerformTask() {
        Long presentId = 1L;
        Long unPresentId = 2L;

        String presentName = "test2";
        String username = "creator";

        Task task = new Task();
        task.setName(presentName);
        task.setCourse(new Course());

        TaskAnswer taskAnswer = new TaskAnswer();
        taskAnswer.setChoice("izi");

        task.setTaskAnswers(Set.of(taskAnswer));

        when(taskRepository.findById(presentId)).thenReturn(Optional.of(task));
        when(taskRepository.findById(unPresentId)).thenReturn(Optional.empty());
        when(userEntityRepository.findByLogin(username)).thenReturn(new UserEntity());

        TaskResultResponse trr = taskService.performTask(presentId, username, "izi");
        assertNotNull(trr);
        assertThat(trr.getResult().equals("DONE"));
        assertThat(trr.getTaskName().equals(presentName));

        assertThrows(RestConflictException.class, () -> taskService.performTask(unPresentId,username,"ziz"));
    }

    @Test
    void testEditTask(){
        Long presentId = 1L;
        Long unPresentId = 2L;
        String presentName = "test2";

        Task task = new Task();
        task.setName(presentName);
        task.setCourse(new Course());

        TaskAnswer taskAnswer = new TaskAnswer();
        taskAnswer.setChoice("izi");

        task.setTaskAnswers(Set.of(taskAnswer));

        TaskEditRequest ter = new TaskEditRequest();
        ter.setDescription("some bla");
        ter.setName("present name)");

        when(taskRepository.findById(presentId)).thenReturn(Optional.of(task));
        when(taskRepository.findById(unPresentId)).thenReturn(Optional.empty());
        when(taskRepository.save(task)).thenReturn(task);

        TaskResponse taskResponse = taskService.editTask(presentId, ter);
        assertNotNull(taskResponse);
        assertThat(!taskResponse.getName().equals(presentName));
        assertThat(taskResponse.getDescription().equals("some bla"));

        assertThrows(RestConflictException.class, () -> taskService.editTask(unPresentId, ter));
    }
}
