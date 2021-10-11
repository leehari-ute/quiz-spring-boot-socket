package com.web.quiz.services;

import com.web.quiz.interfaces.IQuiz;
import com.web.quiz.models.Quiz;
import com.web.quiz.models.Subject;
import com.web.quiz.models.User;
import com.web.quiz.repositories.QuizRepository;
import com.web.quiz.utils.OffsetBasedPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService implements IQuiz {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public Quiz insertQuiz(Quiz quiz) {
        return this.quizRepository.save(quiz);
    }

    @Override
    public Optional<Quiz> findById(Integer id) {
        return this.quizRepository.findById(id);
    }

    @Override
    public Optional<Quiz> findByIdAndIdOwner(Integer id, User owner) {
        return this.quizRepository.findByIdAndIdOwner(id, owner);
    }

    @Override
    public boolean isHasOwnQuiz(User user, Integer idQuiz) {
        return this.quizRepository.findByIdAndIdOwner(idQuiz, user).isPresent();
    }

    @Override
    public List<Quiz> findAllBySubjectAndStatusAndIsPublish(Subject subject, String status, Boolean isPublish, Pageable pageable) {
        return this.quizRepository.findAllBySubjectAndStatusAndIsPublish(subject, status, isPublish, pageable).getContent();
    }


    @Override
    public List<Quiz> findByUser(User user, Pageable pageable) {
        return this.quizRepository.findByIdOwner(user, pageable).getContent();
    }

    @Override
    public List<Quiz> findAllByIdOwner(User idOwner)
    {
        return this.quizRepository.findAllByIdOwner(idOwner);
    }

    @Override
    public Optional<Quiz> findByIdAndStatusAndIsPublish(Integer id, String status, Boolean isPublish) {
        return this.quizRepository.findByIdAndStatusAndIsPublish(id, status, isPublish);
    }

    @Override
    public Optional<Quiz> findByIdAndIdOwnerAndIsPublish(Integer id, User idOwner, Boolean isPublish) {
        return this.quizRepository.findByIdAndIdOwnerAndIsPublish(id, idOwner, isPublish);
    }

    @Override
    public List<Quiz> searchBySubject(Subject subject, String name) {
        if (subject.getName().equalsIgnoreCase("all")) {
            return this.quizRepository.findByStatusAndIsPublishAndNameContains("public", true, name);
        }
        return this.quizRepository.findByStatusAndIsPublishAndSubjectAndNameContains("public", true, subject, name);
    }

    @Override
    public void deleteById(Integer id) {
        this.quizRepository.deleteById(id);
    }


}
