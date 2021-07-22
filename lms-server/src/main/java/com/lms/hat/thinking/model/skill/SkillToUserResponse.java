package com.lms.hat.thinking.model.skill;

import com.lms.hat.thinking.model.linked.SkillToUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillToUserResponse {
    private Long id;
    private String name;
    private String image;
    private Integer level;

    public static SkillToUserResponse responseFromSkillToUser(SkillToUser skillToUser) {
        Skill skill = skillToUser.getSkill();
        return new SkillToUserResponse(skill.getId(),skill.getName(),skill.getImage(),skillToUser.getLevel());
    }
}
