package com.company.thomasfuhr.datatypes;

import com.github.cliftonlabs.json_simple.JsonArray;

public class JsonArrayWord {

    JsonArray words;
    Word word;

    // constructor
    public JsonArrayWord(JsonArray words, Word word) {

        this.words = words;
        this.word = word;
    }

    // getter for word
    public Word getWord() {

        return word;
    }

    // getter for words
    public JsonArray getWords() {

        return words;
    }
}
