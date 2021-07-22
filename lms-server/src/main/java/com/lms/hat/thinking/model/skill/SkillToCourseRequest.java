package com.lms.hat.thinking.model.skill;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SkillToCourseRequest {
    @NotBlank(message = "power")
    private String name;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer level;
}
