package com.lms.hat.thinking.model.skill;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SkillToUserRequest {
    @NotBlank(message = "power")
    private String skillName;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer level;
}
