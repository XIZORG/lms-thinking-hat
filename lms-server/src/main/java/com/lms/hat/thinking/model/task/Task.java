package com.lms.hat.thinking.model.task;

import com.lms.hat.thinking.model.course.Course;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskAnswer> taskAnswers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column
    private String description;

    public void addAnswer(TaskAnswer answer) {
        taskAnswers.add(answer);
        answer.setTask(this);
    }
}
