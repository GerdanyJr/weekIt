package com.github.gerdanyJr.weekit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.exceptions.NotFoundException;
import com.github.gerdanyJr.weekit.model.req.CreateCourseReq;
import com.github.gerdanyJr.weekit.repository.CourseRepository;
import com.github.gerdanyJr.weekit.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    Course course;

    Course updatedCourse;

    CreateCourseReq req;

    @BeforeEach
    void setup() {
        course = new Course(1L,
                "Teste",
                "teste",
                List.of());

        updatedCourse = new Course(1L,
                "Updated",
                "Updated",
                List.of());

        req = new CreateCourseReq("Teste", "Teste");
    }

    @Test
    @DisplayName("should return created course when a valid course is provided")
    void givenValidCourse_whenCreateCourse_thenReturnCreatedCourse() {
        when(courseRepository.findByName(req.name()))
                .thenReturn(Optional.empty());

        when(courseRepository.save(any(Course.class)))
                .thenReturn(course);

        Course created = courseService.createCourse(req);

        assertNotNull(created);
    }

    @Test
    @DisplayName("should throw a exception when a existing course is provided")
    void givenExistingCourse_whenCreateCourse_thenThrowException() {
        when(courseRepository.findByName(req.name()))
                .thenReturn(Optional.of(course));

        ConflictException e = assertThrows(ConflictException.class, () -> {
            courseService.createCourse(req);
        });

        assertEquals("Course already found with name: " + req.name(), e.getMessage());
    }

    @Test
    @DisplayName("should return all courses when findAll is called")
    void whenFindAll_thenReturnAllCourses() {
        when(courseRepository.findAll())
                .thenReturn(List.of(course, course, course));

        List<Course> foundCourses = courseService.findAll();

        assertEquals(foundCourses.size(), 3);
    }

    @Test
    @DisplayName("should return a course when findById is called with a valid id")
    void givenExistingId_whenFindById_thenReturnCourse() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        Course foundCourse = courseService.findById(1L);

        assertNotNull(foundCourse);
    }

    @Test
    @DisplayName("should throw a exception when findById is called with a invalid id")
    void givenInexistentId_whenFindById_thenThrowException() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> {
            courseService.findById(1L);
        });

        assertEquals("Course not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("should return courses when searchByName is called with a valid name")
    void givenExistingName_whenSearchByName_thenReturnCourse() {
        when(courseRepository
                .findByNameStartingWith(anyString()))
                .thenReturn(List.of(course));

        List<Course> foundCourses = courseService.searchByName("");

        assertEquals(1, foundCourses.size());
    }

    @Test
    @DisplayName("should return updated course when update is called with a valid id")
    void givenExistingId_whenUpdate_thenReturnUpdatedCourse() {
        Long id = 1L;
        when(courseRepository.findById(id))
                .thenReturn(Optional.of(course));

        when(courseRepository.save(any(Course.class)))
                .thenReturn(updatedCourse);

        Course updated = courseService.update(id, req);

        assertEquals(updatedCourse, updated);
    }

    @Test
    @DisplayName("should throw a exception when update is called with a invalid id")
    void givenInexistingId_whenUpdate_thenReturnUpdatedCourse() {
        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> {
            courseService.update(1L, req);
        });

        assertEquals("Course not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("should call delete on repository when delete is called with a valid id")
    void givenExistingId_whenDelete_thenCallDeleteCourse() {
        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.of(course));

        courseService.delete(1L);

        verify(courseRepository, times(1))
                .delete(course);
    }

    @Test
    @DisplayName("should throw exception when delete is called with a invalid id")
    void givenInexistingId_whenDelete_thenThrowException() {
        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            courseService.delete(1L);
        });

        verify(courseRepository, never())
                .delete(course);
    }
}
