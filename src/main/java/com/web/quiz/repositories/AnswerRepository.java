package com.web.quiz.repositories;

import com.web.quiz.models.Answer;
import com.web.quiz.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Answer.PrimaryKey> {
    List<Answer> findAnswersByQuestion(Question question);
}
