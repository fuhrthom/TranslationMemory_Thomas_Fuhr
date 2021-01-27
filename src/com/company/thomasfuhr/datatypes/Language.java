package com.company.thomasfuhr.datatypes;

import java.util.UUID;

public class Language {

    private final String language;
    private int ID;

    // constructor for language
    public Language (String language) {

        this.language = language;
    }

    // getter for language
    public String getLanguage() {

        return language;
    }
}
