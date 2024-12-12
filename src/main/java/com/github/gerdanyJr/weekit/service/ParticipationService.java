package com.github.gerdanyJr.weekit.service;

import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.req.CreateParticipationReq;

public interface ParticipationService {
    public Participation create(CreateParticipationReq req);
}
