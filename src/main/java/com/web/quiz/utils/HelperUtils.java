package com.web.quiz.utils;

public class HelperUtils {
    static final String characters = "0123456789";
    // static final characters =
    //   "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateID(int length) {
        StringBuilder result = new StringBuilder("");
        int charactersLength = characters.length();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt((int) Math.floor(Math.random() * charactersLength)));
        }
        return result.toString();
    }
}
