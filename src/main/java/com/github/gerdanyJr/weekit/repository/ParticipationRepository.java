package com.github.gerdanyJr.weekit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.gerdanyJr.weekit.model.entities.Participation;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    public Optional<Participation> findByStudentId(Long studentId);

    public Optional<Participation> findByCourseId(Long courseId);

}
