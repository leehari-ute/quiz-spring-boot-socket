package com.web.quiz.interfaces;

import com.web.quiz.models.Question;
import com.web.quiz.models.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IQuestion {
    Question insertQuestion(Question question);
    long countQuestionInQuiz(Quiz Quiz);
    Optional<Question> findByPrimaryKey(Question.PrimaryKey primaryKey);
    void deleteQuestionByPrimaryKey(Question.PrimaryKey primaryKey);
    List<Integer> getListQuestionIdByQuiz(Quiz quiz);
}
