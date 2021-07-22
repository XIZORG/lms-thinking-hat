package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findByName(String name);
}
