package com.lms.hat.thinking.service;

import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.exception.status.RestUnauthorizedException;
import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.skill.*;
import com.lms.hat.thinking.repository.CourseRepository;
import com.lms.hat.thinking.repository.SkillRepository;
import com.lms.hat.thinking.repository.UserEntityRepository;
import com.lms.hat.thinking.service.impl.SkillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SkillServiceTest {
    private UserEntityRepository userRepository;
    private SkillRepository skillRepository;
    private CourseRepository courseRepository;
    private SkillService skillService;

    @BeforeEach
    void setUp() {
        skillRepository = mock(SkillRepository.class);
        userRepository = mock(UserEntityRepository.class);
        courseRepository = mock(CourseRepository.class);
        skillService = new SkillServiceImpl(userRepository, skillRepository, courseRepository);
    }

    @Test
    void testSaveSkill(){
        SkillRequest skillRequest = new SkillRequest();
        skillRequest.setImageSource("http/sss/www");
        skillRequest.setSkillName("power");

        String existingName = "speed";

        Skill skill = new Skill();
        skill.setImage(skillRequest.getImageSource());
        skill.setName(skillRequest.getSkillName());

        when(skillRepository.save(skill)).thenReturn(skill);
        when(skillRepository.findByName(existingName)).thenReturn(new Skill());
        when(skillRepository.findByName(skillRequest.getSkillName())).thenReturn(null);

        SkillResponse skillResponse = skillService.saveSkill(skillRequest);
        assertNotNull(skillResponse);
        assertThat(skillResponse.getName().equals("power"));

        skillRequest.setSkillName(existingName);
        assertThrows(RestConflictException.class, () -> skillService.saveSkill(skillRequest));
    }

    @Test
    void testAddSkillToCourse(){
        String username = "username";
        String skillName = "skillname";
        Long courseId = 1L;
        Integer level = 2;

        Course course = new Course();
        course.setCreator(username);

        Skill skill = new Skill();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(skillRepository.findByName(skillName)).thenReturn(skill);

        SkillToCourseRequest skillToCourseRequest = new SkillToCourseRequest();
        skillToCourseRequest.setLevel(level);
        skillToCourseRequest.setName(skillName);
        List<SkillToCourseRequest> listSkillToCourseRequest = List.of(skillToCourseRequest);

        skillService.addSkillsToCourse(courseId,listSkillToCourseRequest,username);

        assertThat(course.getAvailableSkills().size() > 0);

        String newUsername = "new login";
        assertThrows(RestUnauthorizedException.class, () -> skillService.addSkillsToCourse(courseId,listSkillToCourseRequest,newUsername));
    }

    @Test
    void testEditSkill(){
        Long presentId = 1L;
        Long unPresentId = 2L;

        String presentName = "test2";
        String newName = "test3";

        Skill skill = new Skill();
        skill.setName(presentName);

        when(skillRepository.findById(presentId)).thenReturn(Optional.of(skill));
        when(skillRepository.findById(unPresentId)).thenReturn(Optional.empty());
        when(skillRepository.save(skill)).thenReturn(skill);

        SkillResponse skillResponse = skillService.editSkill(presentId, newName);
        assertNotNull(skillResponse);
        assertThat(!skillResponse.getName().equals(presentName));

        assertThrows(RestConflictException.class, () -> skillService.editSkill(unPresentId, newName));
    }

    @Test
    void testFindSkillByName(){
        String presentName = "power";
        String unPresentNme = "weight";

        Skill skill = new Skill();
        skill.setName(presentName);

        when(skillRepository.findByName(presentName)).thenReturn(skill);
        when(skillRepository.findByName(unPresentNme)).thenReturn(null);

        SkillResponse skillResponse = skillService.findByNAme(presentName);
        assertNotNull(skillResponse);

        assertThat(skillResponse.getName().equals(presentName));

        assertThrows(RestConflictException.class, () -> skillService.findByNAme(unPresentNme));
    }

    @Test
    void testDeleteSkill() {
        Long presentId = 1L;
        Long unPresentId = 2L;

        String presentName = "power";
        Skill skill = new Skill();
        skill.setName(presentName);

        when(skillRepository.findById(presentId)).thenReturn(Optional.of(skill));
        when(skillRepository.findById(unPresentId)).thenReturn(Optional.empty());

        skillService.deleteSkill(presentId);

        verify(skillRepository).delete(skill);

        assertThrows(RestConflictException.class, () -> skillService.deleteSkill(unPresentId));
    }

    @Test
    void testGetAllSkills(){
        Skill first = new Skill();
        first.setName("power");
        Skill second = new Skill();
        second.setName("speed");

        List<Skill> skills = List.of(first, second);

        when(skillRepository.findAll()).thenReturn(skills);

        List<SkillResponse> responses = skillService.getAllSkills();
        assertNotNull(responses);
        assertThat(responses.get(0).getName().equals("power"));
        assertThat(responses.get(1).getName().equals("speed"));
    }
}
