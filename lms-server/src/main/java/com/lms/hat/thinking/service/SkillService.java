package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.skill.SkillRequest;
import com.lms.hat.thinking.model.skill.SkillResponse;
import com.lms.hat.thinking.model.skill.SkillToCourseRequest;

import java.util.List;

public interface SkillService {
    SkillResponse saveSkill(SkillRequest skillRequest);
    void addSkillsToCourse(Long course, List<SkillToCourseRequest> skillList, String username);
    SkillResponse editSkill(Long id, String name);
    SkillResponse findByNAme(String name);
    void deleteSkill(Long id);
    List<SkillResponse> getAllSkills();
}
