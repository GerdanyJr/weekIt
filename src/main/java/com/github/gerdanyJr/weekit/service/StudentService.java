package com.github.gerdanyJr.weekit.service;

import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.req.CreateStudentReq;

public interface StudentService {
    public Student create(CreateStudentReq req);
}
