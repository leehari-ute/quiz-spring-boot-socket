package com.web.quiz.interfaces;

import com.web.quiz.models.Quiz;
import com.web.quiz.models.Subject;
import com.web.quiz.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IQuiz {
    Quiz insertQuiz(Quiz quiz);
    Optional<Quiz> findById(Integer id);
    Optional<Quiz> findByIdAndIdOwner(Integer id, User owner);
    boolean isHasOwnQuiz(User user, Integer idQuiz);
    List<Quiz> findAllBySubjectAndStatusAndIsPublish(Subject subject, String status, Boolean isPublish, Pageable pageable);
    List<Quiz> findByUser(User user, Pageable pageable);
    List<Quiz> findAllByIdOwner(User idOwner);
    Optional<Quiz> findByIdAndStatusAndIsPublish(Integer id, String status, Boolean isPublish);
    Optional<Quiz> findByIdAndIdOwnerAndIsPublish(Integer id, User idOwner, Boolean isPublish);
    List<Quiz> searchBySubject(Subject subject, String name);
    void deleteById(Integer id);
}
