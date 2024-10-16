package com.github.gerdanyJr.weekit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.gerdanyJr.weekit.model.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    public Optional<Student> findByRegistrationNumber(String registrationNumber);
}
