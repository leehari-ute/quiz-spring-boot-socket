package com.web.quiz.services;

import com.web.quiz.interfaces.ISubject;
import com.web.quiz.models.Subject;
import com.web.quiz.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements ISubject {
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAllSubject() {
        return this.subjectRepository.findAll();
    }
}
