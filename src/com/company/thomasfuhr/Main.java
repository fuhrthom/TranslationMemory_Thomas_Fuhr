package com.company.thomasfuhr;

import com.company.thomasfuhr.translationMemory.TranslationMemory;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // create new instance of translationMemory and start the application
        TranslationMemory translationMemory = new TranslationMemory();
        translationMemory.startTranslationMemory();
    }
}
