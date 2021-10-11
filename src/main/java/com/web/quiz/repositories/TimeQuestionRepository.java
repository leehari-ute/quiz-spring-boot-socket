package com.web.quiz.repositories;

import com.web.quiz.models.TimeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeQuestionRepository extends JpaRepository<TimeQuestion, Integer> {
}
