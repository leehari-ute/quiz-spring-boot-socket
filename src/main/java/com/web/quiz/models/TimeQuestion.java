package com.web.quiz.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "time_question")
public class TimeQuestion {
    @Id
    @Column(name = "value", nullable = false)
    private Integer id;

    @Column(name = "text", nullable = false)
    private String text;

    public TimeQuestion() {
    }

    public TimeQuestion(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
