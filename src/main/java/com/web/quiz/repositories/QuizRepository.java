package com.web.quiz.repositories;

import com.web.quiz.models.Quiz;
import com.web.quiz.models.Subject;
import com.web.quiz.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Optional<Quiz> findByIdAndIdOwner(Integer id, User idOwner);
    Page<Quiz> findAllBySubjectAndStatusAndIsPublish(Subject subject, String status, Boolean isPublish, Pageable pageable);
    Page<Quiz> findByIdOwner(User idOwner, Pageable pageable);
    List<Quiz> findAllByIdOwner(User idOwner);
    Optional<Quiz> findByIdAndIdOwnerAndIsPublish(Integer id, User idOwner, Boolean isPublish);
    List<Quiz> findByStatusAndIsPublishAndSubjectAndNameContains(String status, Boolean isPublish, Subject subject, String name);
    List<Quiz> findByStatusAndIsPublishAndNameContains(String status, Boolean isPublish, String name);
    Optional<Quiz> findByIdAndStatusAndIsPublish(Integer id, String status, Boolean isPublish);
}
