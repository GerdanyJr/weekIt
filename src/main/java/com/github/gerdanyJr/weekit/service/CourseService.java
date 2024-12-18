package com.github.gerdanyJr.weekit.service;

import java.util.List;
import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;

public interface CourseService {
    public Course createCourse(CreateCourseReq req);

    public List<Course> findAll();

    public Course findById(Long id);

    public List<Course> searchByName(String name);

    public Course update(Long id, CreateCourseReq req);

    public void delete(Long id);
}
