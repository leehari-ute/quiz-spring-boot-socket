package com.web.quiz.repositories;

import com.web.quiz.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, String> {
    List<Grade> findByOrderByOrder();
}
