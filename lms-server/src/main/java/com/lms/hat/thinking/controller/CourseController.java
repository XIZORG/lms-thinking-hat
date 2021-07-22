package com.lms.hat.thinking.controller;

import com.lms.hat.thinking.config.security.services.CustomUserDetails;
import com.lms.hat.thinking.model.comment.CommentRequest;
import com.lms.hat.thinking.model.comment.CommentResponse;
import com.lms.hat.thinking.model.course.CourseEditRequest;
import com.lms.hat.thinking.model.course.CourseRequest;
import com.lms.hat.thinking.model.course.CourseResponse;
import com.lms.hat.thinking.model.course.CourseResultResponse;
import com.lms.hat.thinking.model.material.MaterialToCourseRequest;
import com.lms.hat.thinking.model.response.DefaultResponse;
import com.lms.hat.thinking.model.skill.SkillToCourseRequest;
import com.lms.hat.thinking.model.task.TaskRequest;
import com.lms.hat.thinking.service.SkillService;
import com.lms.hat.thinking.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseServiceImpl courseService;
    private final SkillService skillService;

    @Autowired
    public CourseController(CourseServiceImpl courseService, SkillService skillService) {
        this.courseService = courseService;
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createNewCourse(@Valid @RequestBody CourseRequest courseRequest,
                                                          Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(courseService.save(courseRequest, cud.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses(Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(courseService.getAllCourseByUser(cud.getUsername()));
    }

    @GetMapping("/results")
    public ResponseEntity<List<CourseResultResponse>> getAllCourseResults(Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(courseService.getAllUserCourseResults(cud.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(courseService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CourseResponse> editCourseById(@PathVariable(value = "id") Long id,
                                                 @RequestBody CourseEditRequest cer){
        return ResponseEntity.ok(courseService.editCourse(id, cer));
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<CourseResultResponse> getCourseResult(@PathVariable(value = "id") Long course_id,
                                                                Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(courseService.courseResult(course_id,cud.getUsername()));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CourseResponse> getCourseByName(@PathVariable(value = "name") String name){
        return ResponseEntity.ok(courseService.getByName(name));
    }

    @PostMapping("/{id}/material")
    public ResponseEntity<DefaultResponse> addMaterial(@PathVariable(value = "id") Long id,
                                                       @RequestBody MaterialToCourseRequest materialToCourseRequest,
                                                       Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(courseService
                .addMaterialToCourse(id, materialToCourseRequest.getMaterialId(),cud.getUsername()));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponse> addComment(@PathVariable(value = "id") Long id,
                                              @RequestBody CommentRequest commentRequest,
                                             Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        CommentResponse cr = courseService.saveComment(id, cud.getUsername(), commentRequest);
        return ResponseEntity.ok(cr);
    }

    @PostMapping("/{id}/task")
    public ResponseEntity<DefaultResponse> addTask(@PathVariable(value = "id") Long id,
                                                @Valid @RequestBody List<TaskRequest> taskRequest,
                                                Authentication authentication) {
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        return ResponseEntity.ok(courseService.addTaskToCourse(id, taskRequest, cud.getUsername()));
    }

    @PostMapping("/{id}/skill")
    public ResponseEntity<DefaultResponse> addSkill(@PathVariable(value = "id") Long id,
                                                   @Valid @RequestBody List<SkillToCourseRequest> lstcr,
                                           Authentication authentication){
        CustomUserDetails cud = (CustomUserDetails)authentication.getPrincipal();
        skillService.addSkillsToCourse(id, lstcr, cud.getUsername());
        return ResponseEntity.ok(new DefaultResponse());
    }
}
