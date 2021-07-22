package com.lms.hat.thinking.model.task;


import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.user.UserEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "task_results")
public class TaskResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
