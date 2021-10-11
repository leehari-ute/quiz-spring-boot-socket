package com.web.quiz.services;

import com.web.quiz.interfaces.ITimeQuestion;
import com.web.quiz.models.TimeQuestion;
import com.web.quiz.repositories.TimeQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeQuestionService implements ITimeQuestion {
    private final TimeQuestionRepository timeQuestionRepository;

    @Autowired
    public TimeQuestionService(TimeQuestionRepository timeQuestionRepository) {
        this.timeQuestionRepository = timeQuestionRepository;
    }

    @Override
    public List<TimeQuestion> getAll() {
        return this.timeQuestionRepository.findAll();
    }
}
