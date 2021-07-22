package com.lms.hat.thinking.model.task;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "task_answers")
public class TaskAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(nullable = false)
    private String choice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskAnswer that = (TaskAnswer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(choice, that.choice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, choice);
    }
}
