package com.company.thomasfuhr.database;

import com.company.thomasfuhr.datatypes.Languages;
import com.company.thomasfuhr.datatypes.LinkedWords;
import com.company.thomasfuhr.datatypes.Words;
import com.company.thomasfuhr.usertypes.Translator;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReader {

    public static Languages readExistingLanguagesFileFromSystem() throws IOException {

        String file = "languages.json";
        // read json from system as a string
        String json = new String(Files.readAllBytes(Paths.get(file)));
        // parse the string into languages object
        return new Gson().fromJson(json, Languages.class);
    }

    public static Translator readExistingTranslatorFileFromSystem() throws IOException {

        String file = "translator.json";
        // read json from system as a string
        String json = new String(Files.readAllBytes(Paths.get(file)));
        // parse the string into languages object
        return new Gson().fromJson(json, Translator.class);
    }

    public static Words readExistingWordsFileFromSystem() throws IOException {

        String file = "words.json";
        // read json from system as a string
        String json = new String(Files.readAllBytes(Paths.get(file)));
        // parse the string into languages object
        return new Gson().fromJson(json, Words.class);
    }

    public static LinkedWords readExistingLinkedWordsFileFromSystem() throws IOException {

        String file = "linkedWords.json";
        // read json from system as a string
        String json = new String(Files.readAllBytes(Paths.get(file)));
        // parse the string into languages object
        return new Gson().fromJson(json, LinkedWords.class);
    }

}
