package com.company.thomasfuhr.model.usertypes;

import java.util.ArrayList;

public class Translator {

    private static String username = "translator";
    private static String password = "9876";

    public ArrayList<Integer> languages;
    private int wordsTranslated;

    // getter for username
    public static String getUsername() {
        return username;
    }
    // getter for password
    public static String getPassword() {
        return password;
    }
    // getter for wordsTranslated
    public int getWordsTranslated() {

        return wordsTranslated;
    }
}
