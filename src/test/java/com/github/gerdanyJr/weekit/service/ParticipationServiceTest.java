package com.github.gerdanyJr.weekit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.enums.AcademicRole;
import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.exceptions.NotFoundException;
import com.github.gerdanyJr.weekit.model.req.CreateParticipationReq;
import com.github.gerdanyJr.weekit.repository.CourseRepository;
import com.github.gerdanyJr.weekit.repository.ParticipationRepository;
import com.github.gerdanyJr.weekit.repository.StudentRepository;
import com.github.gerdanyJr.weekit.service.impl.ParticipationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ParticipationServiceTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ParticipationServiceImpl participationService;

    private Course course;

    private Student student;

    private Participation participation;

    private CreateParticipationReq req;

    private CreateParticipationReq updatedReq;

    @BeforeEach
    void setup() {
        student = new Student(
                1L,
                "teste",
                "teste",
                "12345678987",
                "12345678987",
                List.of());

        course = new Course(
                1L,
                "teste",
                "teste",
                List.of());

        participation = new Participation(
                1L,
                AcademicRole.STUDENT,
                student,
                course);

        req = new CreateParticipationReq(
                AcademicRole.STUDENT,
                1L,
                1L);

        updatedReq = new CreateParticipationReq(
                AcademicRole.INSTRUCTOR,
                2L,
                2L);
    }

    @Test
    @DisplayName("should return created participation when a valid participation is passed")
    void givenValidParticipation_whenCreate_thenReturnCreatedParticipation() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.of(course));

        when(participationRepository.findByCourse(course))
                .thenReturn(List.of());

        when(participationRepository.save(any(Participation.class)))
                .thenReturn(participation);

        Participation createdParticipation = participationService.create(req);

        assertNotNull(createdParticipation);
    }

    @Test
    @DisplayName("should throw exception when a invalid student id is passed")
    void givenInvalidStudentId_whenCreate_thenThrowException() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.create(req));

        assertEquals("Student not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("should throw exception when a invalid course id is passed")
    void givenInvalidCourseId_whenCreate_thenThrowException() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.create(req));

        assertEquals("Course not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("should throw exception when a existing participation is passed")
    void givenExistingParticipation_whenCreate_thenThrowException() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.of(course));

        when(participationRepository.findByCourse(course))
                .thenReturn(List.of(participation));

        ConflictException e = assertThrows(
                ConflictException.class,
                () -> participationService.create(req));

        assertEquals("Student already registered in course with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("should return all participations when findAll")
    void whenFindAll_thenReturnParticipations() {
        when(participationRepository.findAll())
                .thenReturn(List.of(participation));

        List<Participation> participations = participationService.findAll();

        assertEquals(1, participations.size());
    }

    @Test
    @DisplayName("given valid id when findById then return participation")
    void givenValidId_whenFindById_thenReturnParticipation() {
        when(participationRepository.findById(anyLong()))
                .thenReturn(Optional.of(participation));

        Participation participation = participationService.findById(1L);

        assertNotNull(participation);
    }

    @Test
    @DisplayName("given invalid id when findById then throw exception")
    void givenInvalidId_whenFindById_thenThrowException() {
        when(participationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.findById(1L));

        assertEquals("Participation not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("given a valid student id when findAllByStudent then return participations")
    void givenValidStudentId_whenFindAllByStudent_thenReturnPartipations() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        when(participationRepository.findByStudent(student))
                .thenReturn(List.of(participation));

        List<Participation> participations = participationService.findAllByStudent(1L);

        assertEquals(1, participations.size());
    }

    @Test
    @DisplayName("given a invalid student id when findAllByStudent then throw exception")
    void givenInvalidStudentId_whenFindAllByStudent_thenThrowException() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.findAllByStudent(1L));

        assertEquals("Student not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("given a valid course id when findAllByCourse then return participations")
    void givenValidStudentId_whenFindAllByCourse_thenReturnPartipations() {
        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.of(course));

        when(participationRepository.findByCourse(course))
                .thenReturn(List.of(participation));

        List<Participation> participations = participationService.findAllByCourse(1L);

        assertEquals(1, participations.size());
    }

    @Test
    @DisplayName("given a invalid course id when findAllByCourse then throw exception")
    void givenInvalidStudentId_whenFindAllByCourse_thenThrowException() {
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.findAllByCourse(1L));

        assertEquals("Course not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("given a valid participation id when delete then call delete on participationRepository")
    void givenValidParticipationId_whenDelete_thenDeleteParticipation() {
        when(participationRepository.findById(anyLong()))
                .thenReturn(Optional.of(participation));

        participationService.delete(1L);

        verify(participationRepository,
                times(1))
                .delete(participation);
    }

    @Test
    @DisplayName("given a invalid participation id when delete then throw exception")
    void givenInvalidParticipationId_whenDelete_thenThrowException() {
        when(participationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> participationService.delete(1L));

        verify(participationRepository,
                times(0))
                .delete(participation);
    }

    @Test
    @DisplayName("should return updated participation when a valid participation is passed to update")
    void givenValidParticipation_whenUpdate_thenReturnUpdatedParticipation() {
        when(participationRepository.findById(anyLong()))
                .thenReturn(Optional.of(participation));

        when(participationRepository.save(any(Participation.class)))
                .thenReturn(participation);

        Participation updatedParticipation = participationService.update(1L, req);

        assertNotNull(updatedParticipation);
    }

    @Test
    @DisplayName("should throw exception when a invalid participation id is passed to update")
    void givenInvalidParticipationId_whenUpdate_thenThrowException() {
        when(participationRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.update(1L, req));

        assertEquals("Participation not found with id: 1", e.getMessage());
    }

    @Test
    @DisplayName("should throw exception when a invalid course id is passed")
    void givenInvalidCourseId_whenUpdate_thenThrowException() {
        when(participationRepository.findById(anyLong()))
                .thenReturn(Optional.of(participation));

        when(courseRepository.findById(2L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.update(1L, updatedReq));

        assertEquals("Course not found with id: " + updatedReq.courseId(), e.getMessage());
    }

    @Test
    @DisplayName("should throw exception when a invalid student id is passed")
    void givenInvalidStudentId_whenUpdate_thenThrowException() {
        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> participationService.update(1L, updatedReq));

        assertEquals("Participation not found with id: 1",
                e.getMessage());
    }
}
