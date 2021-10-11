package com.web.quiz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_game")
    @JsonIgnore
    private Game idGame;

    @Column(name = "point")
    private Integer point;

    @Column(name = "name")
    private String name;

    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User idUser;

    @Column(name = "session_id", nullable = false, length = 45)
    private String sessionId;

    @Column(name = "status", nullable = false, length = 45)
    private String status;

    @Column(name = "correct_percent")
    private Integer correctPercent;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        createAt = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Player() {
    }

    public Player(String id,Game idGame, String sessionId, String status, Integer point, Integer correctPercent) {
        this.id = id;
        this.idGame = idGame;
        this.sessionId = sessionId;
        this.status = status;
        this.point = point;
        this.correctPercent = correctPercent;
    }

    public Player(String id, Game idGame, Integer point, String name, LocalDateTime createAt, User idUser, String sessionId, String status, Integer correctPercent) {
        this.id = id;
        this.idGame = idGame;
        this.point = point;
        this.name = name;
        this.createAt = createAt;
        this.idUser = idUser;
        this.sessionId = sessionId;
        this.status = status;
        this.correctPercent = correctPercent;
    }

    public Integer getCorrectPercent() {
        return correctPercent;
    }

    public void setCorrectPercent(Integer correctPercent) {
        this.correctPercent = correctPercent;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Game getIdGame() {
        return idGame;
    }

    public void setIdGame(Game idGame) {
        this.idGame = idGame;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
//                ", idGame=" + idGame.toString() +
                ", point=" + point +
                ", name='" + name + '\'' +
                ", createAt=" + createAt +
//                ", idUser=" + idUser.toString() +
                ", sessionId='" + sessionId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
