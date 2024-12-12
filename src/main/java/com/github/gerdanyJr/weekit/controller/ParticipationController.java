package com.github.gerdanyJr.weekit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.req.CreateParticipationReq;
import com.github.gerdanyJr.weekit.service.ParticipationService;

@RestController
@RequestMapping("/participacoes")
public class ParticipationController {
    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    public ResponseEntity<Participation> create(@RequestBody CreateParticipationReq req) {
        return ResponseEntity.ok(participationService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<Participation>> findAll() {
        return ResponseEntity.ok(participationService.findAll());
    }
}
