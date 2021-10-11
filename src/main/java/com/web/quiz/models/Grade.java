package com.web.quiz.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @Column(name = "value", nullable = false)
    private String id;

    @Column(name = "order", nullable = false)
    private Integer order;

    public Grade(String id, Integer order) {
        this.id = id;
        this.order = order;
    }

    public Grade() {
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id='" + id + '\'' +
                '}';
    }
}
