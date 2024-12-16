package com.github.gerdanyJr.weekit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.gerdanyJr.weekit.model.entities.Course;

@DataJpaTest
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository courseRepository;

    private List<Course> courses;

    @BeforeEach
    void setup() {
        Course course1 = new Course(null, "teste", "teste", List.of());
        Course course2 = new Course(null, "teste2", "teste2", List.of());
        Course course3 = new Course(null, "teste3", "teste3", List.of());
        Course course4 = new Course(null, "teste4", "teste4", List.of());
        courses = List.of(course1, course2, course3, course4);
    }

    @Test
    @DisplayName("Should return a course given a valid name")
    void givenValidName_whenFindByName_thenReturnCourse() {
        courseRepository.saveAll(courses);
        String courseName = courses
                .get(0)
                .getName();

        Optional<Course> foundCourse = courseRepository
                .findByName(courseName);

        assertTrue(foundCourse.isPresent());
        assertEquals(foundCourse
                .get()
                .getName(),
                courseName);
    }

    @Test
    @DisplayName("Shouldn't return a course given a invalid name")
    void givenInvalidName_whenFindByName_thenReturnNothing() {
        courseRepository.saveAll(courses);

        Optional<Course> foundCourse = courseRepository
                .findByName("");

        assertTrue(foundCourse.isEmpty());
    }

    @Test
    @DisplayName("Should return a list of courses given a valid name")
    void givenValidName_whenFindByNameStartingWith_thenReturnListOfCourses() {
        courseRepository.saveAll(courses);

        List<Course> foundCourses = courseRepository
                .findByNameStartingWith("teste");

        assertEquals(foundCourses.size(), 4);
    }

    @Test
    @DisplayName("Should return a empty list given a invalid name")
    void givenInvalidName_whenFindByNameStartingWith_thenReturnEmptyList() {
        courseRepository.saveAll(courses);

        List<Course> foundCourses = courseRepository
                .findByNameStartingWith("testeste");
                
        assertEquals(foundCourses.size(), 0);
    }
}
