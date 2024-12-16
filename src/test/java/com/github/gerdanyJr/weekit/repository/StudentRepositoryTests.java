package com.github.gerdanyJr.weekit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.github.gerdanyJr.weekit.model.entities.Student;

@DataJpaTest
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setup() {
        student = new Student(null,
                "teste",
                "teste",
                "123456789",
                "123456789123",
                List.of());
    }

    @DisplayName("Should return a student when call findByRegistration with a valid registration")
    @Test
    void givenValidRegistrationNumber_whenFindByRegistrationNumber_thenReturnStudent() {
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository
                .findByRegistrationNumber(student.getRegistrationNumber());

        assertTrue(foundStudent.isPresent());
        assertEquals(foundStudent
                .get()
                .getRegistrationNumber(),
                student.getRegistrationNumber());
    }

    @DisplayName("Should return a student when call findByCpf with a valid cpf")
    @Test
    void givenValidCpf_whenFindByCpf_thenReturnStudent() {
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository
                .findByCpf(student.getCpf());

        assertTrue(foundStudent.isPresent());
        assertEquals(foundStudent
                .get()
                .getCpf(),
                student.getCpf());
    }

    @DisplayName("Shouldn't return a student when call findByCpf with a invalid cpf")
    @Test
    void givenInvalidCpf_whenFindByCpf_thenReturnNothing() {
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository
                .findByCpf(anyString());

        assertTrue(foundStudent.isEmpty());
    }

    @DisplayName("Shouldn't return a student when call findByRegistration with a invalid registration number")
    @Test
    void givenInvalidRegistration_whenFindByRegistration_thenReturnNothing() {
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository
                .findByRegistrationNumber(anyString());

        assertTrue(foundStudent.isEmpty());
    }
}
