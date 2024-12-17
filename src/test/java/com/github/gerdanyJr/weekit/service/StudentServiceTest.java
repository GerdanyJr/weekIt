package com.github.gerdanyJr.weekit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.exceptions.NotFoundException;
import com.github.gerdanyJr.weekit.model.req.CreateStudentReq;
import com.github.gerdanyJr.weekit.repository.StudentRepository;
import com.github.gerdanyJr.weekit.service.impl.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private CreateStudentReq req;

    private Student student;

    @BeforeEach
    void setup() {
        req = new CreateStudentReq("teste",
                "teste",
                "12345678987",
                "123456789876");

        student = new Student(1L,
                "teste",
                "teste",
                "12345678987",
                "123456789876",
                List.of());
    }

    @Test
    @DisplayName("should return a student when a valid student is passed")
    void givenValidStudent_whenCreate_thenReturnCreatedStudent() {
        when(studentRepository.findByCpf(anyString()))
                .thenReturn(Optional.empty());

        when(studentRepository.findByRegistrationNumber(anyString()))
                .thenReturn(Optional.empty());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        Student registeredStudent = studentService.create(req);

        assertNotNull(registeredStudent);
    }

    @Test
    @DisplayName("should throw a exception when a existing cpf is passed")
    void givenExistingCpf_whenCreate_thenThrowException() {
        when(studentRepository.findByCpf(anyString()))
                .thenReturn(Optional.of(student));

        ConflictException e = assertThrows(ConflictException.class, () -> {
            studentService.create(req);
        });

        assertEquals("Student already found with cpf: " + req.cpf(), e.getMessage());
    }

    @Test
    @DisplayName("should throw a exception when a existing regiistration is passed")
    void givenExistingRegistration_whenCreate_thenThrowException() {
        when(studentRepository.findByCpf(anyString()))
                .thenReturn(Optional.empty());

        when(studentRepository.findByRegistrationNumber(anyString()))
                .thenReturn(Optional.of(student));

        ConflictException e = assertThrows(ConflictException.class, () -> {
            studentService.create(req);
        });

        assertEquals("Student already found with registration: " + req.registrationNumber(), e.getMessage());
    }

    @Test
    @DisplayName("should return all registered students")
    void whenFindAll_thenReturnStudents() {
        when(studentRepository.findAll())
                .thenReturn(List.of(student, student, student));

        List<Student> students = studentService.findAll();

        assertNotNull(students);
        assertEquals(3, students.size());
    }

    @Test
    @DisplayName("should return student when a existent id is passed")
    void givenExistingId_whenFindById_thenReturnStudent() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        Student student = studentService.findById(1L);

        assertNotNull(student);
    }

    @Test
    @DisplayName("should throw exception when a non existent id is passed")
    void givenNonExistentId_whenFindById_thenThrowException() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> {
            studentService.findById(1L);
        });

        assertEquals("Student not found with id: " + 1L, e.getMessage());
    }

    @Test
    @DisplayName("should return student when a existing registration is passed")
    void givenExistingRegistration_whenFindByRegistration_thenReturnStudent() {
        when(studentRepository.findByRegistrationNumber(anyString()))
                .thenReturn(Optional.of(student));

        Student student = studentService.findByRegistrationNumber("teste");

        assertNotNull(student);
    }

    @Test
    @DisplayName("should throw exception when a non existent registration is passed")
    void givenNonExistentRegistration_whenFindByRegistration_thenThrowException() {
        when(studentRepository.findByRegistrationNumber(anyString()))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> studentService.findByRegistrationNumber("teste"));

        assertEquals("Student not found with registration: teste", e.getMessage());
    }

    @Test
    @DisplayName("should throw exception when CPF already exists with different ID")
    void givenExistingCpfWithDifferentId_whenUpdate_thenThrowConflictException() {
        when(studentRepository.findByCpf(anyString()))
                .thenReturn(Optional.of(student));

        ConflictException exception = assertThrows(ConflictException.class, () -> studentService.update(2L, req));

        assertEquals("Student already found with cpf: " + req.cpf(), exception.getMessage());
    }

    @Test
    @DisplayName("should throw ConflictException when registration number already exists with different ID")
    void givenExistingRegistrationWithDifferentId_whenUpdate_thenThrowConflictException() {
        when(studentRepository.findByRegistrationNumber(anyString()))
                .thenReturn(Optional.of(student));

        ConflictException exception = assertThrows(ConflictException.class, () -> studentService.update(2L, req));

        assertEquals("Student already found with registration: " + req.registrationNumber(), exception.getMessage());
    }

    @Test
    @DisplayName("should throw NotFoundException when student does not exist")
    void givenNonExistentStudentId_whenUpdate_thenThrowNotFoundException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> studentService.update(1L, req));
        assertEquals("Student not found with id: 1", exception.getMessage());
    }

    @Test
    @DisplayName("should update when student exists and no conflicts occur")
    void givenValidStudent_whenUpdate_thenReturnUpdatedStudent() {
        when(studentRepository.findByCpf(req.cpf()))
                .thenReturn(Optional.of(student));

        when(studentRepository.findByRegistrationNumber(req.registrationNumber()))
                .thenReturn(Optional.of(student));

        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        when(studentRepository.save(student))
                .thenReturn(student);

        Student updatedStudent = studentService.update(1L, req);

        assertNotNull(updatedStudent);
        assertEquals(req.cpf(), updatedStudent.getCpf());
        assertEquals(req.registrationNumber(), updatedStudent.getRegistrationNumber());
    }

    @Test
    @DisplayName("should delete a student when a existing id is passed")
    void givenExistingId_whenDelete_thenDeleteStudent() {
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        studentService.delete(1L);

        verify(studentRepository, times(1))
                .delete(student);
    }

    @Test
    @DisplayName("should throw a exception when a non existent id is passed")
    void givenNonExistingId_whenDelete_thenThrowException() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> studentService.delete(1L));

        assertEquals("Student not found with id: 1", e.getMessage());
        verify(studentRepository, times(0))
                .delete(student);
    }
}
