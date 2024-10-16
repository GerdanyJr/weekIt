package com.github.gerdanyJr.weekit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
