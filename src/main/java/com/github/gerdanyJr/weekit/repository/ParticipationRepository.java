package com.github.gerdanyJr.weekit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.entities.Student;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    public List<Participation> findByStudent(Student student);
    
    public List<Participation> findByCourse(Course course);

}
