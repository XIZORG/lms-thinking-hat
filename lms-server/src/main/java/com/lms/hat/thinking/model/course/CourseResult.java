package com.lms.hat.thinking.model.course;

import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.model.task.Status;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course_results")
public class CourseResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
