package com.web.quiz.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_quiz")
    private Quiz idQuiz;

    @Column(name = "code", length = 6)
    private String code;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "id_host")
    private User idHost;

    @PrePersist
    protected void onCreate() {
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }

    public Game() {
    }

    public Game(Quiz idQuiz, String status) {
        this.idQuiz = idQuiz;
        this.status = status;
    }

    public Game(Integer id, Quiz idQuiz, String code, String status, LocalDateTime updateAt, User idHost) {
        this.id = id;
        this.idQuiz = idQuiz;
        this.code = code;
        this.status = status;
        this.updateAt = updateAt;
        this.idHost = idHost;
    }

    public User getIdHost() {
        return idHost;
    }

    public void setIdHost(User idHost) {
        this.idHost = idHost;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Quiz getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(Quiz idQuiz) {
        this.idQuiz = idQuiz;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", idQuiz=" + idQuiz +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", updateAt=" + updateAt +
                ", idHost=" + idHost +
                '}';
    }
}
