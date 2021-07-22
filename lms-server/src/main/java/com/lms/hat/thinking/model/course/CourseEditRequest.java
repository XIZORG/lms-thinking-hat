package com.lms.hat.thinking.model.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CourseEditRequest {
    @NotBlank(message = "course_name")
    private String name;

    private String description;

    @NotBlank(message = "99")
    private String passingScore;
}
