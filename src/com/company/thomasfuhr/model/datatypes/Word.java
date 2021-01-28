package com.company.thomasfuhr.model.datatypes;

public class Word {

    private final String word;
    private final Integer ID;

    // constructor for word
    public Word(String word, int ID) {

        this.word = word;
        this.ID = ID;
    }

    // getter for word
    public String getWord() {

        return word;
    }

    // getter for wordID
    public int getID() {

        return ID;
    }
}
