package com.github.gerdanyJr.weekit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.req.CreateStudentReq;
import com.github.gerdanyJr.weekit.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody @Valid CreateStudentReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("matricula/{registrationNumber}")
    public ResponseEntity<Student> findByRegistrationNumber(@PathVariable String registrationNumber) {
        return ResponseEntity.ok(studentService.findByRegistrationNumber(registrationNumber));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody @Valid CreateStudentReq req) {
        return ResponseEntity.ok(studentService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
