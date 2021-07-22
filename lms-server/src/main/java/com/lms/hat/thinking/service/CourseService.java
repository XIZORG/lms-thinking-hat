package com.lms.hat.thinking.service;

import com.lms.hat.thinking.model.comment.CommentRequest;
import com.lms.hat.thinking.model.comment.CommentResponse;
import com.lms.hat.thinking.model.course.CourseEditRequest;
import com.lms.hat.thinking.model.course.CourseRequest;
import com.lms.hat.thinking.model.course.CourseResultResponse;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.task.TaskRequest;
import com.lms.hat.thinking.model.course.CourseResponse;
import com.lms.hat.thinking.model.task.TaskResponse;

import java.util.List;

public interface CourseService {
    CourseResponse save(CourseRequest course, String login);
    DefaultResponse addMaterialToCourse(Long courseId, List<Long> materials, String username);
    DefaultResponse addTaskToCourse(Long courseId, List<TaskRequest> taskRequests, String username);
    List<CourseResponse> getAll();
    List<CourseResponse> getAllCourseByUser(String login);
    CourseResponse getById(Long id);
    CourseResponse getByName(String name);
    CourseResultResponse courseResult(Long courseId, String login);
    CommentResponse saveComment(Long courseId, String userLogin, CommentRequest commentRequest);
    CourseResponse editCourse(Long id, CourseEditRequest courseRequest);
    List<CourseResultResponse> getAllUserCourseResults(String login);

}
