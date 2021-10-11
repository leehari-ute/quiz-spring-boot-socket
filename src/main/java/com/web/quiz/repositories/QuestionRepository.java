package com.web.quiz.repositories;

import com.web.quiz.models.Question;
import com.web.quiz.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Question.PrimaryKey> {
    long countQuestionByQuiz(Quiz quiz);
    Optional<Question> findByPrimaryKey(Question.PrimaryKey primaryKey);
}
