package com.web.quiz.models;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question {
    @EmbeddedId
    private PrimaryKey primaryKey;

    @ManyToOne
    @MapsId("idQuiz")
    @JoinColumn(name = "id_quiz")
    @JsonIgnore
    private Quiz quiz;

    @JsonIgnore
    @OneToMany(mappedBy="question")
    private Set<Answer> answers;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "file", nullable = false)
    private String file;

    @Column(name = "type", nullable = false)
    private String type;

    @Embeddable
    public static class PrimaryKey implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "id", nullable = false)
        private Integer id;

        @Column(name = "id_quiz", nullable = false)
        private Integer idQuiz;

        public PrimaryKey(Integer id, Integer idQuiz) {
            this.id = id;
            this.idQuiz = idQuiz;
        }

        public PrimaryKey() {

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getIdQuiz() {
            return idQuiz;
        }

        public void setIdQuiz(Integer idQuiz) {
            this.idQuiz = idQuiz;
        }

        @Override
        public String toString() {
            return "QuestionPrimaryKey{" +
                    "id=" + id +
                    ", idQuiz=" + idQuiz +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PrimaryKey that = (PrimaryKey) o;
            return Objects.equals(id, that.id) && Objects.equals(idQuiz, that.idQuiz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, idQuiz);
        }
    }

    public Question() {
    }

    public Question(PrimaryKey primaryKey, Quiz quiz, Set<Answer> answers, String content, Integer duration, String file, String type) {
        this.primaryKey = primaryKey;
        this.quiz = quiz;
        this.answers = answers;
        this.content = content;
        this.duration = duration;
        this.file = file;
        this.type = type;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "primaryKey=" + primaryKey +
                ", quiz=" + quiz +
                ", content='" + content + '\'' +
                ", duration=" + duration +
                ", file='" + file + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
