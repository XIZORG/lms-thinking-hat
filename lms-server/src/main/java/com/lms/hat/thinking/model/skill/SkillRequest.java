package com.lms.hat.thinking.model.skill;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SkillRequest {
    @NotBlank(message = "power!")
    private String skillName;
    @NotBlank(message = "/http/bla-bla!")
    private String imageSource;
}
