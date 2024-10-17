package com.github.gerdanyJr.weekit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchByName(@RequestParam(name = "name", required = true) String name) {
        return ResponseEntity.ok(courseService.searchByName(name));
    }

}
