package com.github.gerdanyJr.weekit.service;

import java.util.List;

import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.req.CreateParticipationReq;

public interface ParticipationService {
    public Participation create(CreateParticipationReq req);

    public List<Participation> findAll();

    public Participation findById(Long id);

    public List<Participation> findAllByStudent(Long studentId);

    public List<Participation> findAllByCourse(Long courseId);

    public Participation update(Long id, CreateParticipationReq req);

    public void delete(Long id);
}
