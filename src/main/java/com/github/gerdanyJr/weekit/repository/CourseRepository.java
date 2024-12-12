package com.github.gerdanyJr.weekit.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.github.gerdanyJr.weekit.model.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

    List<Course> findByNameStartingWith(String name);

}
