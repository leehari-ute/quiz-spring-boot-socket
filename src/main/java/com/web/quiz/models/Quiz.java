package com.web.quiz.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_owner")
    private User idOwner;

    @OneToMany(mappedBy="quiz", fetch = FetchType.EAGER)
    private Set<Question> questions;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title_image")
    private String titleImage;

    @ManyToOne
    @JoinColumn(name = "subject")
    private Subject subject;

    @Column(name = "language")
    private String language;

    @Column(name = "status")
    private String status;

    @Column(name = "from_grade")
    private String fromGrade;

    @Column(name = "to_grade")
    private String toGrade;

    @Column(name = "number_questions")
    private Integer numberQuestions;

    @Column(name = "played_times")
    private Integer playedTimes;

    @Column(name = "update_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updateAt;

    @Column(name = "is_publish", nullable = false)
    private Boolean isPublish = false;

    @PrePersist
    protected void onCreate() {
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }

    public Quiz() {
    }

    public Quiz(Integer id, User idOwner, Set<Question> questions, String name, String titleImage, Subject subject, String language, String status, String fromGrade, String toGrade, Integer numberQuestions, Integer playedTimes, LocalDateTime updateAt, Boolean isPublish) {
        this.id = id;
        this.idOwner = idOwner;
        this.questions = questions;
        this.name = name;
        this.titleImage = titleImage;
        this.subject = subject;
        this.language = language;
        this.status = status;
        this.fromGrade = fromGrade;
        this.toGrade = toGrade;
        this.numberQuestions = numberQuestions;
        this.playedTimes = playedTimes;
        this.updateAt = updateAt;
        this.isPublish = isPublish;
    }

    public Boolean getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Boolean isPublish) {
        this.isPublish = isPublish;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getPlayedTimes() {
        return playedTimes;
    }

    public void setPlayedTimes(Integer playedTimes) {
        this.playedTimes = playedTimes;
    }

    public Integer getNumberQuestions() {
        return numberQuestions;
    }

    public void setNumberQuestions(Integer numberQuestions) {
        this.numberQuestions = numberQuestions;
    }

    public String getToGrade() {
        return toGrade;
    }

    public void setToGrade(String toGrade) {
        this.toGrade = toGrade;
    }

    public String getFromGrade() {
        return fromGrade;
    }

    public void setFromGrade(String fromGrade) {
        this.fromGrade = fromGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(User idOwner) {
        this.idOwner = idOwner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", idOwner=" + idOwner.toString() +
//                ", questions=" + questions +
                ", name='" + name + '\'' +
                ", titleImage='" + titleImage + '\'' +
                ", subject=" + subject +
                ", language='" + language + '\'' +
                ", status='" + status + '\'' +
                ", fromGrade='" + fromGrade + '\'' +
                ", toGrade='" + toGrade + '\'' +
                ", numberQuestions=" + numberQuestions +
                ", playedTimes=" + playedTimes +
                ", updateAt=" + updateAt +
                ", isPublish=" + isPublish +
                '}';
    }
}
