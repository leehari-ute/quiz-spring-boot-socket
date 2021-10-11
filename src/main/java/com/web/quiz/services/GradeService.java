package com.web.quiz.services;

import com.web.quiz.interfaces.IGrade;
import com.web.quiz.models.Grade;
import com.web.quiz.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService implements IGrade {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public List<Grade> findByOrderByOrder() {
        return this.gradeRepository.findByOrderByOrder();
    }
}
