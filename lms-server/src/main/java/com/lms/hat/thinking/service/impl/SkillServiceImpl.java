package com.lms.hat.thinking.service.impl;

import com.lms.hat.thinking.exception.status.RestUnauthorizedException;
import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.skill.*;
import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.repository.*;
import com.lms.hat.thinking.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {
    private final UserEntityRepository userRepository;
    private final SkillRepository skillRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SkillServiceImpl(UserEntityRepository userRepository, SkillRepository skillRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public SkillResponse saveSkill(SkillRequest skillRequest) {
        if(skillRepository.findByName(skillRequest.getSkillName()) != null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("name", HttpStatus.CONFLICT.getReasonPhrase(),
                            "skill with name: " + skillRequest.getSkillName() + "already exist")));
        }

        Skill skill = new Skill();
        skill.setName(skillRequest.getSkillName());
        skill.setImage(skillRequest.getImageSource());
        skill = skillRepository.save(skill);
        return new SkillResponse(skill.getId(), skill.getName(), skill.getImage());
    }

    @Override
    @Transactional
    public void addSkillsToCourse(Long course, List<SkillToCourseRequest> lstcr, String username) {
        Course currentCourse = courseRepository.findById(course).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));
        if (!username.equals(currentCourse.getCreator())) {
            throw new RestUnauthorizedException(Collections.singletonList(
                    new FieldErrorModel("login", HttpStatus.CONFLICT.getReasonPhrase(),
                            "you cannot add task to this course")));
        }

        for (SkillToCourseRequest skillWithLevel : lstcr) {
            Skill currentSkill = skillRepository.findByName(skillWithLevel.getName());
            if (currentSkill == null) {
                throw new RestConflictException(Collections.singletonList(
                        new FieldErrorModel("name", HttpStatus.CONFLICT.getReasonPhrase(),
                                "skill not found")));
            }
            currentCourse.addSkill(currentSkill, skillWithLevel.getLevel());
        }

        courseRepository.save(currentCourse);
    }

    @Override
    public SkillResponse editSkill(Long id, String name) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "skill not found"))));
        skill.setName(name);
        skill = skillRepository.save(skill);
        return new SkillResponse(skill.getId(), skill.getName(), skill.getImage());
    }

    @Override
    public SkillResponse findByNAme(String name) {
        Skill skill = skillRepository.findByName(name);
        if (skill == null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("name", HttpStatus.CONFLICT.getReasonPhrase(),
                            "skill not found")));
        }
        return new SkillResponse(skill.getId(), skill.getName(), skill.getImage());
    }

    @Override
    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "skill not found"))));
        skillRepository.delete(skill);
    }

    @Override
    public List<SkillResponse> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        if (skills.size() == 0) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("skill", HttpStatus.CONFLICT.getReasonPhrase(),
                            "there are no skill in db")));
        }
        List<SkillResponse> skillResponses = skills.stream()
                .map(data -> new SkillResponse(data.getId(), data.getName(), data.getImage()))
                .collect(Collectors.toList());
        return skillResponses;
    }
}
