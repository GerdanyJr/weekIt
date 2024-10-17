package com.github.gerdanyJr.weekit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;
import com.github.gerdanyJr.weekit.service.CourseService;

@RestController
@RequestMapping("/minicursos")
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody CreateCourseReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(req));
    }

}