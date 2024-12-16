package com.github.gerdanyJr.weekit.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.gerdanyJr.weekit.model.entities.Course;
import com.github.gerdanyJr.weekit.model.entities.Participation;
import com.github.gerdanyJr.weekit.model.entities.Student;
import com.github.gerdanyJr.weekit.model.exceptions.ConflictException;
import com.github.gerdanyJr.weekit.model.exceptions.NotFoundException;
import com.github.gerdanyJr.weekit.model.req.CreateParticipationReq;
import com.github.gerdanyJr.weekit.repository.CourseRepository;
import com.github.gerdanyJr.weekit.repository.ParticipationRepository;
import com.github.gerdanyJr.weekit.repository.StudentRepository;
import com.github.gerdanyJr.weekit.service.ParticipationService;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public ParticipationServiceImpl(ParticipationRepository participationRepository,
            StudentRepository studentRepository, CourseRepository courseRepository) {
        this.participationRepository = participationRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Participation create(CreateParticipationReq req) {
        Student foundStudent = studentRepository
                .findById(req.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + req.studentId()));

        Course foundCourse = courseRepository
                .findById(req.courseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + req.courseId()));

        List<Participation> courseParticipations = participationRepository
                .findByCourse(foundCourse);

        courseParticipations.forEach((participation) -> {
            if (participation.getStudent().getId() == req.studentId()) {
                throw new ConflictException("Student already registered in course with id: " + req.courseId());
            }
        });

        return participationRepository.save(new Participation(null, req.role(), foundStudent, foundCourse));
    }

    @Override
    public List<Participation> findAll() {
        return participationRepository.findAll();
    }

    @Override
    public Participation findById(Long id) {
        Participation participation = participationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Participation not found with id: " + id));

        return participation;
    }

    @Override
    public List<Participation> findAllByStudent(Long id) {
        Student foundStudent = studentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found with id: " + id));

        return participationRepository.findByStudent(foundStudent);
    }

    @Override
    public List<Participation> findAllByCourse(Long courseId) {
        Course foundCourse = courseRepository
                .findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + courseId));

        return participationRepository.findByCourse(foundCourse);
    }

    @Override
    public Participation update(Long id, CreateParticipationReq req) {
        Participation participation = participationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Participation not found with id: " + id));

        if (req.courseId() != participation.getCourse().getId()) {
            Course foundCourse = courseRepository
                    .findById(req.courseId())
                    .orElseThrow(() -> new NotFoundException("Course not found with id: " + req.courseId()));
            participation.setCourse(foundCourse);
        }

        if (req.studentId() != participation.getStudent().getId()) {
            Student foundStudent = studentRepository
                    .findById(req.studentId())
                    .orElseThrow(() -> new NotFoundException("Student not found with id: " + req.studentId()));
            participation.setStudent(foundStudent);
        }

        BeanUtils.copyProperties(req, participation);

        return participationRepository.save(participation);
    }

    @Override
    public void delete(Long id) {
        Participation foundParticipation = participationRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Participation not found with id: " + id));

        participationRepository.delete(foundParticipation);
    }

}
