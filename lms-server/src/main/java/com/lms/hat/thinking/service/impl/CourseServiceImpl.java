package com.lms.hat.thinking.service.impl;

import com.lms.hat.thinking.exception.status.RestUnauthorizedException;
import com.lms.hat.thinking.model.comment.Comment;
import com.lms.hat.thinking.model.comment.CommentRequest;
import com.lms.hat.thinking.model.comment.CommentResponse;
import com.lms.hat.thinking.model.course.*;
import com.lms.hat.thinking.model.linked.SkillToCourse;
import com.lms.hat.thinking.model.linked.SkillToUser;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.skill.Skill;
import com.lms.hat.thinking.model.task.*;
import com.lms.hat.thinking.model.user.UserEntity;
import com.lms.hat.thinking.repository.*;
import com.lms.hat.thinking.model.material.Material;
import com.lms.hat.thinking.exception.model.FieldErrorModel;
import com.lms.hat.thinking.exception.status.RestConflictException;
import com.lms.hat.thinking.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final MaterialRepository materialRepository;
    private final TaskRepository taskRepository;
    private final UserEntityRepository userRepository;
    private final SkillRepository skillRepository;
    private final CourseResultRepository courseResultRepository;
    private final TaskResultRepository taskResultRepository;


    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, MaterialRepository materialRepository, TaskRepository taskRepository, UserEntityRepository userRepository, SkillRepository skillRepository, CourseResultRepository courseResultRepository, TaskResultRepository taskResultRepository) {
        this.courseRepository = courseRepository;
        this.materialRepository = materialRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.courseResultRepository = courseResultRepository;
        this.taskResultRepository = taskResultRepository;
    }

    @Override
    public CourseResponse save(CourseRequest courseRequest, String login) {
        if (courseRepository.findByName(courseRequest.getName()) != null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("name", HttpStatus.CONFLICT.getReasonPhrase(),
                            "course with name: " + courseRequest.getName() + " already exist")));
        }
        Course course = new Course();

        try {
            course.setBeginDate(Instant.parse(courseRequest.getStartDate()));
            course.setEndDate(Instant.parse(courseRequest.getEndDate()));
        } catch (DateTimeParseException ex){
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("data", HttpStatus.CONFLICT.getReasonPhrase(),
                            "wrong data")));
        }
        course.setName(courseRequest.getName());
        course.setPassingScore(Integer.parseInt(courseRequest.getPassingScore()));
        course.setDescription(courseRequest.getDescription());
        course.setCreator(login);

        courseRepository.save(course);
        logger.info("User, login: {}. Successfully created course!", login);
        return CourseResponse.fromCourseToResponse(course);
    }

    @Override
    @Transactional
    public DefaultResponse addMaterialToCourse(Long courseId, List<Long> materials, String username){
        Course currentCourse = getAndCheckCourse(courseId, username);

        for(Long materialId : materials) {
            Material material = materialRepository.findById(materialId)
                    .orElseThrow(() -> new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                            "material not found"))));
            currentCourse.addMaterial(material);
        }
        courseRepository.save(currentCourse);
        return new DefaultResponse();
    }

    private Course getAndCheckCourse(Long courseId, String username) {
        Course currentCourse = courseRepository.findById(courseId).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));
        if (!username.equals(currentCourse.getCreator())) {
            throw new RestUnauthorizedException(Collections.singletonList(
                    new FieldErrorModel("login", HttpStatus.CONFLICT.getReasonPhrase(),
                            "you cannot add task to this course")));
        }
        return currentCourse;
    }

    @Override
    @Transactional
    public DefaultResponse addTaskToCourse(Long courseId, List<TaskRequest> taskRequests, String username) {
        Course currentCourse = getAndCheckCourse(courseId, username);

        for (TaskRequest taskRequest : taskRequests) {
            Task task;
            if ((task = taskRepository.findByName(taskRequest.getName())) == null) {
                task = new Task();
                for (String answer : taskRequest.getChoices()) {
                    TaskAnswer taskAnswer = new TaskAnswer();
                    taskAnswer.setChoice(answer);
                    task.addAnswer(taskAnswer);
                }
                task.setName(taskRequest.getName());
                task.setDescription(taskRequest.getDescription());

            }
            taskRepository.save(task);
            currentCourse.addTask(task);
        }

        courseRepository.save(currentCourse);
        return new DefaultResponse();
    }

    @Override
    @Transactional
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(CourseResponse::fromCourseToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CourseResponse> getAllCourseByUser(String login) {
        UserEntity currentUser = userRepository.findByLogin(login);
        Set<Course> courseList = currentUser.getCourses();
        List<CourseResponse> courseResponses = courseRepository.findAll().stream()
                .map(CourseResponse::fromCourseToResponse).collect(Collectors.toList());
        for (CourseResponse courseResponse : courseResponses) {
            if (courseList.stream().anyMatch(data -> data.getName().equals(courseResponse.getName()))) {
                courseResponse.setCourseStatus(CourseStatus.SUBSCRIBED.name());
            } else {
                courseResponse.setCourseStatus(CourseStatus.UNSUBSCRIBED.name());
            }
        }

        return courseResponses;
    }


    @Override
    @Transactional
    public CourseResponse getById(Long id) {
        return CourseResponse.fromCourseToResponseWithTasks(
                courseRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found")))));
    }

    @Override
    @Transactional
    public CourseResponse getByName(String name) {
        Course currentCourse = courseRepository.findByName(name);
        if (currentCourse == null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                            "course not found")));
        }
        return CourseResponse.fromCourseToResponseWithTasks(currentCourse);
    }

    private void addSkillsToUser(String login, String skillName, Integer level) {
        UserEntity currentUser = userRepository.findByLogin(login);
        Skill currentSkill = skillRepository.findByName(skillName);

        if (currentSkill == null) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("name", HttpStatus.CONFLICT.getReasonPhrase(),
                            "skill not found")));
        }

        SkillToUser skillToUser = currentUser.getAvailableSkills()
                .stream()
                .filter(data -> data.getSkill().getName().equals(currentSkill.getName()))
                .findFirst().orElse(null);

        if (skillToUser != null) {
            skillToUser.setLevel(skillToUser.getLevel() + level);
        } else {
            currentUser.addSkill(currentSkill, level);
        }
        userRepository.save(currentUser);
    }

    @Override
    @Transactional
    public CourseResultResponse courseResult(Long course_id, String userLogin) {
        Course currentCourse = courseRepository.findById(course_id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));

        UserEntity user = userRepository.findByLogin(userLogin);

        if (!user.getCourses().contains(currentCourse)) {
            throw new RestConflictException(Collections.singletonList(
                    new FieldErrorModel("course", HttpStatus.CONFLICT.getReasonPhrase(),
                            "user is not subscribed to the course ")));
        }

        Set<TaskResult> results = user.getTaskResults();
        long points = results.stream().filter(data -> data.getStatus().equals(Status.DONE)).count();

        for (TaskResult taskResult : results) {
            if (taskResult.getCourse().equals(currentCourse)) {
                taskResultRepository.delete(taskResult.getId());
            }
        }

        CourseResult courseResult = new CourseResult();
        if(points >= currentCourse.getPassingScore()) {
            courseResult.setStatus(Status.DONE);
        } else {
            courseResult.setStatus(Status.FAILED);
        }
        courseResult.setCourse(currentCourse);
        user.addCourseResult(courseResult);


        Set<SkillToCourse> skillsToUser = new HashSet<>(currentCourse.getAvailableSkills());

        for (SkillToCourse skill : skillsToUser) {
            addSkillsToUser(userLogin, skill.getSkill().getName(), skill.getLevel());
        }

        for (Material material : currentCourse.getMaterials()) {
            user.addMaterial(material);
        }

        user.removeCourse(currentCourse);
        userRepository.save(user);

        logger.info("The user: {} has completed the course and received the results", userLogin);
        return CourseResultResponse.responseCourseResultFromResult(courseResult);
    }

    @Override
    public CommentResponse saveComment(Long courseId, String username, CommentRequest commentRequest) {
        UserEntity user = userRepository.findByLogin(username);
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setUser(user);

        course.addComment(comment);
        courseRepository.save(course);
        return CommentResponse.responseFromComment(comment);
    }

    @Override
    public CourseResponse editCourse(Long id, CourseEditRequest courseRequest) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RestConflictException(Collections.singletonList(
                new FieldErrorModel("id", HttpStatus.CONFLICT.getReasonPhrase(),
                        "course not found"))));
        course.setName(courseRequest.getName());
        course.setPassingScore(Integer.parseInt(courseRequest.getPassingScore()));
        course.setDescription(courseRequest.getDescription());
        return CourseResponse.fromCourseToResponse(courseRepository.save(course));
    }

    @Override
    public List<CourseResultResponse> getAllUserCourseResults(String login) {
        UserEntity user = userRepository.findByLogin(login);
        List<CourseResult> courseResults = courseResultRepository.findAll();

        return courseResults.stream()
                .filter(data -> data.getUser().equals(user))
                .map(CourseResultResponse::responseCourseResultFromResult)
                .collect(Collectors.toList());
    }
}
