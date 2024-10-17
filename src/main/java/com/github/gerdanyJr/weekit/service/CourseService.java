package com.github.gerdanyJr.weekit.service;

import java.util.List;
import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;

public interface CourseService {
    public Course createCourse(CreateCourseReq req);

    public List<Course> findAll();

    public Course findById(Long id);
}
