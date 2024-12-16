package com.github.gerdanyJr.weekit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.enums.AcademicRole;

@DataJpaTest
public class ParticipationRepositoryTests {

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    private Course course;

    private Participation participation;

    @BeforeEach
    void setup() {
        student = new Student(null,
                "teste",
                "teste",
                "123456789",
                "123456789123",
                List.of());

        course = new Course(null,
                "teste",
                "teste",
                List.of());

        participation = new Participation(null,
                AcademicRole.STUDENT,
                student,
                course);
    }

    @Test
    @DisplayName("should return existing participations from student")
    void givenExistingStudent_whenFindByStudent_thenReturnParticipation() {
        courseRepository.save(course);
        studentRepository.save(student);
        participationRepository.save(participation);

        List<Participation> foundParticipations = participationRepository.findByStudent(student);

        assertEquals(foundParticipations.size(), 1);
        assertEquals(foundParticipations.get(0).getStudent().getId(), student.getId());
    }

    @Test
    @DisplayName("shouldn't return participations if student doesn't have")
    void givenStudentWithoutParticipation_whenFindByStudent_thenReturnNothing() {
        courseRepository.save(course);
        studentRepository.save(student);

        List<Participation> foundParticipations = participationRepository.findByStudent(student);

        assertEquals(foundParticipations.size(), 0);
    }

    @Test
    @DisplayName("should return existing participations from course")
    void givenExistingCourse_whenFindByCourse_thenReturnParticipation() {
        courseRepository.save(course);
        studentRepository.save(student);
        participationRepository.save(participation);

        List<Participation> foundParticipations = participationRepository.findByCourse(course);

        assertEquals(foundParticipations.size(), 1);
        assertEquals(foundParticipations.get(0).getCourse().getId(), course.getId());
    }

    @Test
    @DisplayName("shouldn't return participations if course doesn't have")
    void givenCourseWithoutParticipation_whenFindByCourse_thenReturnNothing() {
        courseRepository.save(course);
        studentRepository.save(student);

        List<Participation> foundParticipations = participationRepository.findByCourse(course);

        assertEquals(foundParticipations.size(), 0);
    }

}
