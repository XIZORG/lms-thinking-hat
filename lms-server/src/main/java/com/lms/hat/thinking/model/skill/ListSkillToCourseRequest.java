package com.lms.hat.thinking.model.skill;

import lombok.Data;

import java.util.List;

@Data
public class ListSkillToCourseRequest {
    private List<SkillToCourseRequest> skills;
}
