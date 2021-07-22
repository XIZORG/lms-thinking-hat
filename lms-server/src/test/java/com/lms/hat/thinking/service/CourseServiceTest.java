package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.comment.CommentRequest;
import com.lms.hat.thinking.model.comment.CommentResponse;
import com.lms.hat.thinking.model.course.*;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.task.TaskRequest;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.repository.*;
import com.lms.hat.thinking.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseServiceTest {
    private CourseService courseService;
    private CourseRepository courseRepository;
    private MaterialRepository materialRepository;
    private UserEntityRepository userRepository;
    private CourseResultRepository courseResultRepository;
    private TaskResultRepository taskResultRepository;


    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        materialRepository = mock(MaterialRepository.class);
        TaskRepository taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserEntityRepository.class);
        SkillRepository skillRepository = mock(SkillRepository.class);
        courseResultRepository = mock(CourseResultRepository.class);
        taskResultRepository = mock(TaskResultRepository.class);


        courseService = new CourseServiceImpl(courseRepository,
                materialRepository,
                taskRepository,
                userRepository,
                skillRepository, courseResultRepository, taskResultRepository);
    }

    @Test
    void testCourseSave(){
        CourseRequest courseRequest = new CourseRequest("TheBestCOurse",
                "bla-bla-bla",
                "5",
                "2018-05-12T20:30:00.000Z",
                "2018-05-15T20:30:00.000Z");


        CourseResponse response = courseService.save(courseRequest, "creator");

        assertThat(response.getCreator().equals("creator"));
        assertThat(response.getDescription().equals(courseRequest.getDescription()));
        assertNotNull(response.getName());
        assertNotNull(response.getStartDate());
    }


    @Test
    void testAddMaterialToCourse(){
        Course currentCourse = getNewCourse();

        List<Long> materialsId = List.of(1L,2L);

        Long presentId = 1L;
        Long unPresentId = 2L;

        when(courseRepository.findById(presentId)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById(unPresentId)).thenReturn(Optional.empty());
        when(materialRepository.findById(notNull())).thenReturn(Optional.of(new Material()));

        DefaultResponse dr = courseService.addMaterialToCourse(presentId, materialsId, "creator");
        assertNotNull(dr);

        assertThrows(Exception.class, () -> courseService.addMaterialToCourse(unPresentId, materialsId, "creator"));

    }

    @Test
    void testAddTaskToCourse(){
        Course currentCourse = getNewCourse();

        Long presentId = 1L;
        Long unPresentId = 2L;

        when(courseRepository.findById(presentId)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById(unPresentId)).thenReturn(Optional.empty());

        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setDescription("bla-bla-bla");
        taskRequest.setName("task");
        taskRequest.setChoices(new LinkedList<>());

        List<TaskRequest> taskRequests = List.of(taskRequest);
        DefaultResponse tr = courseService.addTaskToCourse(presentId,taskRequests, "creator");

        assertNotNull(tr);

        assertThrows(Exception.class, () -> courseService.addTaskToCourse(unPresentId, taskRequests, "creator"));
    }

    @Test
    void testGetAllCourses(){
        List<Course> courseList = List.of(getNewCourse(), getNewCourse());
        when(courseRepository.findAll()).thenReturn(courseList);

        List<CourseResponse> courseResponses = courseService.getAll();
        assertThat(courseResponses.get(0).getName().equals("name"));
        assertNotNull(courseResponses);
    }

    @Test
    void getById(){
        Long presentId = 1L;
        Long unPresentId = 2L;
        Course currentCourse = getNewCourse();

        when(courseRepository.findById(presentId)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById(unPresentId)).thenReturn(Optional.empty());

        CourseResponse presentCourse = courseService.getById(presentId);
        assertThat(presentCourse.getCreator().equals(currentCourse.getCreator()));
        assertThat(presentCourse.getDescription().equals(currentCourse.getDescription()));
        assertThat(presentCourse.getPassingScore().equals(currentCourse.getPassingScore()));

        assertThrows(Exception.class, () -> courseService.getById(unPresentId));
    }

    @Test
    void testGetByName(){
        String presentName = "test1";
        String unPresentName = "test2";
        Course currentCourse = getNewCourse();

        when(courseRepository.findByName(presentName)).thenReturn(currentCourse);
        when(courseRepository.findByName(unPresentName)).thenReturn(null);

        CourseResponse presentCourse = courseService.getByName(presentName);
        assertThat(presentCourse.getCreator().equals(currentCourse.getCreator()));
        assertThat(presentCourse.getDescription().equals(currentCourse.getDescription()));
        assertThat(presentCourse.getPassingScore().equals(currentCourse.getPassingScore()));

        assertThrows(Exception.class, () -> courseService.getByName(unPresentName));
    }

    @Test
    void getCourseResult(){
        Long presentId = 1L;
        Long unPresentId = 2L;
        Course currentCourse = getNewCourse();

        when(courseRepository.findById(presentId)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById(unPresentId)).thenReturn(Optional.empty());

        UserEntity presentUser = getNewUser();
        presentUser.addCourse(currentCourse);
        String username = "presentLogin";

        String unPresentLogin = "unPresentLogin";
        UserEntity unPresentUser = getNewUser();
        unPresentUser.setLogin(unPresentLogin);

        when(userRepository.findByLogin(username)).thenReturn(presentUser);
        when(userRepository.findByLogin(unPresentLogin)).thenReturn(unPresentUser);

        CourseResultResponse crr = courseService.courseResult(presentId, username);
        assertThat(crr.getUserName().equals(username));
        assertThat(crr.getCourseName().equals(currentCourse.getName()));

        assertThrows(Exception.class, () -> courseService.courseResult(unPresentId, username));
        assertThrows(Exception.class, () -> courseService.courseResult(presentId, unPresentLogin));
    }

    @Test
    void testSaveComment(){
        Long presentId = 1L;
        Long unPresentId = 2L;
        Course currentCourse = getNewCourse();

        when(courseRepository.findById(presentId)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById(unPresentId)).thenReturn(Optional.empty());

        UserEntity presentUser = getNewUser();
        String username = "presentLogin";

        when(userRepository.findByLogin(username)).thenReturn(presentUser);

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent("It was the best course i ever see");

        CommentResponse commentResponse = courseService.saveComment(presentId,username,commentRequest);

        assertNotNull(commentResponse);
        assertThat(commentResponse.getContent().equals(commentRequest.getContent()));
        assertNotNull(commentResponse.getUserLogin());

        assertThrows(Exception.class, () -> courseService.courseResult(unPresentId, username));
    }

    @Test
    void testEditCourse(){
        Long presentId = 1L;
        Long unPresentId = 2L;
        Course currentCourse = getNewCourse();

        when(courseRepository.findById(presentId)).thenReturn(Optional.of(currentCourse));
        when(courseRepository.findById(unPresentId)).thenReturn(Optional.empty());

        CourseEditRequest cer = new CourseEditRequest();
        cer.setDescription("I want to change this sh**");
        cer.setName("Name2");
        cer.setPassingScore("11");

        when(courseRepository.save(notNull())).thenReturn(currentCourse);

        CourseResponse cr = courseService.editCourse(presentId, cer);

        assertNotNull(cr);
        assertThat(cr.getName().equals(cer.getName()));
        assertNotNull(cr.getCreator());
        assertThat(cr.getDescription().equals(cer.getDescription()));

        assertThrows(Exception.class, () -> courseService.editCourse(unPresentId, cer));

    }

    private Course getNewCourse(){
        Course currentCourse = new Course();
        currentCourse.setName("name");
        currentCourse.setDescription("bla-bla-bla");
        currentCourse.setPassingScore(45);
        currentCourse.setCreator("creator");
        return currentCourse;
    }

    private UserEntity getNewUser(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setLogin("presentLogin");
        userEntity.setPassword("presentPassword");
        userEntity.setEmail("email@gmail.com");
        return userEntity;
    }
}
