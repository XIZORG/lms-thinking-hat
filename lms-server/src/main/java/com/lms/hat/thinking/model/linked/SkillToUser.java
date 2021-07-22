package com.lms.hat.thinking.model.linked;

import com.lms.hat.thinking.model.skill.Skill;
import com.lms.hat.thinking.model.user.UserEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SkillToUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column
    private Integer level;
}


