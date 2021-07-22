package com.lms.hat.thinking.repository;

import com.lms.hat.thinking.model.course.Course;
import com.lms.hat.thinking.model.course.CourseResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseResultRepository extends JpaRepository<CourseResult, Long> {
}
