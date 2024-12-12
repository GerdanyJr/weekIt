package com.github.gerdanyJr.weekit.service;

import java.util.List;

import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.req.CreateParticipationReq;

public interface ParticipationService {
    public Participation create(CreateParticipationReq req);

    public List<Participation> findAll();
}
