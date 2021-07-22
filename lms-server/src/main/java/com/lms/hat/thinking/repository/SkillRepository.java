package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Skill findByName(String name);
}
