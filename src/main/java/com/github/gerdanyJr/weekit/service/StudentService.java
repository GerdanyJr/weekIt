package com.github.gerdanyJr.weekit.service;

import java.util.List;

import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.req.CreateStudentReq;

public interface StudentService {
    public Student create(CreateStudentReq req);

    public List<Student> findAll();

    public Student findById(Long id);

    public Student findByRegistrationNumber(String registrationNumber);

    public Student update(Long id, CreateStudentReq req);
}
