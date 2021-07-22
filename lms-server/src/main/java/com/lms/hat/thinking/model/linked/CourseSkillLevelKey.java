package com.lms.hat.thinking.model.linked;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CourseSkillLevelKey implements Serializable {
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "course_id")
    private Long courseId;
}
