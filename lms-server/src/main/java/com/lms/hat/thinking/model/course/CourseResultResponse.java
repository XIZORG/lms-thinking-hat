package com.lms.hat.thinking.model.course;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResultResponse {
    private String courseName;
    private String userName;
    private String result;

    public static CourseResultResponse responseCourseResultFromResult(CourseResult courseResult){
        return new CourseResultResponse(courseResult.getCourse().getName(),
                courseResult.getUser().getLogin(),
                courseResult.getStatus().name());
    }
}
