package com.company.thomasfuhr;

import com.company.thomasfuhr.database.Database;
import com.company.thomasfuhr.datatypes.Language;
import com.company.thomasfuhr.translationMemory.TranslationMemory;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // create german because its based on german search and creating, so german has to exist
        Database.getInstance().addLanguage(new Language("Deutsch"));

        // create new instance of translationMemory and start the application
        TranslationMemory translationMemory = new TranslationMemory();
        translationMemory.startTranslationMemory();
    }
}
