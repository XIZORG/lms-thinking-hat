package com.lms.hat.thinking.model.course;

import com.lms.hat.thinking.model.material.MaterialResponse;
import com.lms.hat.thinking.model.task.TaskResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String name;
    private String creator;
    private Instant startDate;
    private Instant endDate;
    private String description;
    private Integer passingScore;
    private List<MaterialResponse> materials;
    private String courseStatus;
    private List<TaskResponse> tasks;

    public static CourseResponse fromCourseToResponse(Course course) {
        List<MaterialResponse> mats = course.getMaterials().stream()
                .map(data -> new MaterialResponse(data.getId(), data.getName(), data.getCreator(), data.getLink()))
                .collect(Collectors.toList());

        return new CourseResponse(course.getId(),
                course.getName(),
                course.getCreator(),
                course.getBeginDate(),
                course.getEndDate(),
                course.getDescription(),
                course.getPassingScore(),
                mats);
    }

    public static CourseResponse fromCourseToResponseWithTasks(Course course) {
        List<MaterialResponse> mats = course.getMaterials().stream()
                .map(data -> new MaterialResponse(data.getId(), data.getName(), data.getCreator(), data.getLink()))
                .collect(Collectors.toList());

        return new CourseResponse(course.getId(),
                course.getName(),
                course.getCreator(),
                course.getBeginDate(),
                course.getEndDate(),
                course.getDescription(),
                course.getPassingScore(),
                mats,
                course.getTasks().stream().map(TaskResponse::responseFromTask).collect(Collectors.toList()));
    }

    public CourseResponse(Long id, String name, String creator, Instant beginDate, Instant endDate, String description, Integer passingScore, List<MaterialResponse> materials) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.startDate = beginDate;
        this.endDate = endDate;
        this.description = description;
        this.passingScore = passingScore;
        this.materials = materials;
    }

    public CourseResponse(Long id, String name, String creator, Instant startDate, Instant endDate, String description, Integer passingScore, List<MaterialResponse> materials, List<TaskResponse> tasks) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.passingScore = passingScore;
        this.materials = materials;
        this.tasks = tasks;
    }
}
