package com.lms.hat.thinking.model.skill;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillResponse {
    private Long id;
    private String name;
    private String imageUrl;
}
