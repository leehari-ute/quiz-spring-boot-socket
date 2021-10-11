package com.web.quiz.services;

import com.web.quiz.interfaces.IQuestion;
import com.web.quiz.models.Question;
import com.web.quiz.models.Quiz;
import com.web.quiz.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService implements IQuestion {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question insertQuestion(Question question) {
        return this.questionRepository.save(question);
    }

    @Override
    public long countQuestionInQuiz(Quiz quiz) {
        return this.questionRepository.countQuestionByQuiz(quiz);
    }

    @Override
    public Optional<Question> findByPrimaryKey(Question.PrimaryKey primaryKey) {
        return this.questionRepository.findByPrimaryKey(primaryKey);
    }

    @Override
    public void deleteQuestionByPrimaryKey(Question.PrimaryKey primaryKey) {
        this.questionRepository.deleteById(primaryKey);
    }

    @Override
    public List<Integer> getListQuestionIdByQuiz(Quiz quiz) {
        Set<Question> questions = quiz.getQuestions();
        Iterator<Question> value = questions.iterator();
        List<Integer> listQuestionId = new ArrayList<>();
        while (value.hasNext()) {
            Question question = value.next();
            listQuestionId.add(question.getPrimaryKey().getId());
        }
        return listQuestionId;
    }
}
