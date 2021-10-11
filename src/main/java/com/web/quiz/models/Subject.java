package com.web.quiz.models;

import javax.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    public Subject(String name) {
        this.name = name;
    }

    public Subject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                '}';
    }
}
