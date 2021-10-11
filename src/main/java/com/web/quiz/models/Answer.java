package com.web.quiz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "answers")
public class Answer {

    @EmbeddedId
    private PrimaryKey primaryKey;

    @ManyToOne
    @MapsId(value = "questionPrimaryKey")
    @JoinColumns ({
            @JoinColumn(name="id_question", referencedColumnName = "id"),
            @JoinColumn(name="id_quiz", referencedColumnName = "id_quiz")
    })
    @JsonIgnore
    private Question question;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Embeddable
    public static class PrimaryKey implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "id", nullable = false)
        private Integer id;

        @Embedded
        private Question.PrimaryKey questionPrimaryKey;

        public PrimaryKey() {
        }

        public PrimaryKey(Integer id, Question.PrimaryKey questionPrimaryKey) {
            this.id = id;
            this.questionPrimaryKey = questionPrimaryKey;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Question.PrimaryKey getQuestionPrimaryKey() {
            return questionPrimaryKey;
        }

        public void setQuestionPrimaryKey(Question.PrimaryKey questionPrimaryKey) {
            this.questionPrimaryKey = questionPrimaryKey;
        }

        @Override
        public String toString() {
            return "PrimaryKey{" +
                    "id=" + id +
                    ", questionPrimaryKey=" + questionPrimaryKey +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(id, that.id) && Objects.equals(questionPrimaryKey, that.questionPrimaryKey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, questionPrimaryKey);
        }
    }

    public Answer() {
    }

    public Answer(PrimaryKey primaryKey, Question question, String content, Boolean isCorrect) {
        this.primaryKey = primaryKey;
        this.question = question;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "primaryKey=" + primaryKey +
                ", question=" + question +
                ", content='" + content + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
