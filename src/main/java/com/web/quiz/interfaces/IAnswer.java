package com.web.quiz.interfaces;

import com.web.quiz.models.Answer;
import com.web.quiz.models.Question;

import java.util.List;

public interface IAnswer {
    Answer insertAnswer(Answer answer);
    List<Answer> findAnswersByQuestion(Question question);
}
