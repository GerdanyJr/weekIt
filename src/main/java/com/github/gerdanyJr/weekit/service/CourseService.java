package com.github.gerdanyJr.weekit.service;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;

public interface CourseService {
    public Course createCourse(CreateCourseReq req);
}
