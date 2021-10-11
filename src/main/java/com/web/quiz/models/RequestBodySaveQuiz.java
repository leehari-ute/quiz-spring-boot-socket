package com.web.quiz.models;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class RequestBodySaveQuiz {
    private Integer id;
    private String name;
    private String fromGrade;
    private String toGrade;
    private String subject;
    private String status;

    public RequestBodySaveQuiz() {
    }

    public RequestBodySaveQuiz(Integer id, String name, String fromGrade, String toGrade, String subject, String status) {
        this.id = id;
        this.name = name;
        this.fromGrade = fromGrade;
        this.toGrade = toGrade;
        this.subject = subject;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromGrade() {
        return fromGrade;
    }

    public void setFromGrade(String fromGrade) {
        this.fromGrade = fromGrade;
    }

    public String getToGrade() {
        return toGrade;
    }

    public void setToGrade(String toGrade) {
        this.toGrade = toGrade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RequestBodySaveQuiz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fromGrade='" + fromGrade + '\'' +
                ", toGrade='" + toGrade + '\'' +
                ", subject='" + subject + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
