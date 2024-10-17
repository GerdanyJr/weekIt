package com.github.gerdanyJr.weekit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.req.CreateStudentReq;
import com.github.gerdanyJr.weekit.repository.StudentRepository;
import com.github.gerdanyJr.weekit.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student create(CreateStudentReq req) {
        Optional<Student> foundByCpf = studentRepository.findByCpf(req.cpf());

        foundByCpf.ifPresent((student) -> {
            throw new ConflictException("Student already found with cpf: " + req.cpf());
        });

        Optional<Student> foundByRegistration = studentRepository.findByRegistrationNumber(req.registrationNumber());

        foundByRegistration.ifPresent((student) -> {
            throw new ConflictException("Student already found with registration: " + req.registrationNumber());
        });

        Student student = new Student();
        BeanUtils.copyProperties(req, student);
        return studentRepository.save(student);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student findByRegistrationNumber(String registrationNumber) {
        return studentRepository.findByRegistrationNumber(registrationNumber).get();
    }

    @Override
    public Student update(Long id, CreateStudentReq req) {
        Optional<Student> foundByCpf = studentRepository.findByCpf(req.cpf());

        foundByCpf.ifPresent((student) -> {
            if (student.getId() != id) {
                throw new ConflictException("Student already found with cpf: " + req.cpf());
            }
        });

        Optional<Student> foundByRegistration = studentRepository.findByRegistrationNumber(req.registrationNumber());

        foundByRegistration.ifPresent((student) -> {
            if (student.getId() != id) {
                throw new ConflictException("Student already found with registration: " + req.registrationNumber());
            }
        });

        Student foundStudent = studentRepository.findById(id).get();

        BeanUtils.copyProperties(req, foundStudent);

        return studentRepository.save(foundStudent);
    }

    @Override
    public void delete(Long id) {
        Student student = studentRepository.findById(id).get();

        studentRepository.delete(student);
    }

}
