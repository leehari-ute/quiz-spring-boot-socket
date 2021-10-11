package com.web.quiz.models;

import java.util.Set;

public class RequestBodySaveQuestion {
    private Question question;
    private Set<Answer> answers;

    public RequestBodySaveQuestion() {
    }

    public RequestBodySaveQuestion(Question question, Set<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "RequestBodySaveQuestion{" +
                "question=" + question +
                ", answers=" + answers +
                '}';
    }
}
