package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.task.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TaskResultRepository extends JpaRepository<TaskResult, Long> {
    @Modifying
    @Query("delete from TaskResult t where t.id = ?1")
    void delete(Long entityId);
}
