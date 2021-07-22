package com.lms.hat.thinking.model.linked;

import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.skill.Skill;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SkillToCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column
    private Integer level;
}


