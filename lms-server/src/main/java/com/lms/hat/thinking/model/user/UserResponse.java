package com.lms.hat.thinking.model.user;

import com.lms.hat.thinking.model.course.CourseResponse;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.model.skill.SkillToUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String login;

    private String email;

    private String phone;

    private String fullName;

    private Integer age;

    private List<CourseResponse> courses;

    private List<String> materials;

    private List<SkillToUserResponse> availableSkills;

    public static UserResponse responseUserFromEntity(UserEntity userEntity) {
        List<CourseResponse> courses = userEntity.getCourses().stream()
                .map(CourseResponse::fromCourseToResponse).collect(Collectors.toList());

        List<String> materials = userEntity.getMaterials().stream().map(Material::getName).collect(Collectors.toList());

        List<SkillToUserResponse> availableSkills = userEntity.getAvailableSkills().stream()
                .map(SkillToUserResponse::responseFromSkillToUser)
                .collect(Collectors.toList());

        return new UserResponse(userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getEmail(),
                userEntity.getPhone(),
                userEntity.getFullName(),
                userEntity.getAge(),
                courses,
                materials,
                availableSkills);
    }
}
