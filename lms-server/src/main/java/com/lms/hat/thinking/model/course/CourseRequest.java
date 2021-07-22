package com.lms.hat.thinking.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CourseRequest {
    @NotBlank(message = "course_name")
    private String name;

    private String description;

    @NotBlank(message = "99")
    private String passingScore;

    @NotBlank(message = "2018-11-30T18:35:24.00Z")
    private String startDate;

    @NotBlank(message = "2018-11-30T18:35:24.00Z")
    private String endDate;

    public CourseRequest() {
    }
}
