package com.github.gerdanyJr.weekit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;
import com.github.gerdanyJr.weekit.repository.CourseRepository;
import com.github.gerdanyJr.weekit.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course createCourse(CreateCourseReq req) {
        Optional<Course> foundByName = courseRepository.findByName(req.name());

        foundByName.ifPresent((course) -> {
            throw new ConflictException("Course already found with name: " + req.name());
        });

        Course course = new Course();
        BeanUtils.copyProperties(req, course);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

}
