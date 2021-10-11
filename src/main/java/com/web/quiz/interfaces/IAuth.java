package com.web.quiz.interfaces;

public interface IAuth {
    boolean isAuth();
    void autoLogin(String username, String password);
}
