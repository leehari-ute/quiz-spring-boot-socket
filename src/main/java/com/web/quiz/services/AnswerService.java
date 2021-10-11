package com.web.quiz.services;

import com.web.quiz.interfaces.IAnswer;
import com.web.quiz.models.Answer;
import com.web.quiz.models.Question;
import com.web.quiz.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService implements IAnswer {
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer insertAnswer(Answer answer) {
        return this.answerRepository.save(answer);
    }

    @Override
    public List<Answer> findAnswersByQuestion(Question question) {
        return this.answerRepository.findAnswersByQuestion(question);
    }
}
