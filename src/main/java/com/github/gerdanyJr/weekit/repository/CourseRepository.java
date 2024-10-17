package com.github.gerdanyJr.weekit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.gerdanyJr.weekit.model.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
