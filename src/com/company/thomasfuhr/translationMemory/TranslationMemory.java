package com.company.thomasfuhr.translationMemory;

import com.company.thomasfuhr.database.Database;
import com.company.thomasfuhr.datatypes.Language;
import com.company.thomasfuhr.usertypes.Admin;
import com.company.thomasfuhr.usertypes.Translator;
import com.company.thomasfuhr.view.View;

import java.io.IOException;
import java.util.ArrayList;

public class TranslationMemory {

    public void startTranslationMemory() throws IOException {

        // show the initialMenu and run second mask depending on userinput
        handleUserInputFromStart(View.showInitialMenu());
    }

    private void handleUserInputFromStart(int input) throws IOException {

        // user wants to search a word
        if (input == 1) {

            handleSearchWordMask(View.showSearchWordMask());
        }
        // user wants to see his count of created words
        else if (input == 2) {

            // show created words count
            View.showUserCreatedWordsCount(Database.getInstance().getWordsCreated());
            startTranslationMemory();
        }
        // user wants to see the count of all words
        else if (input == 3) {

            // show count of all words
            View.showAllWordsCount(Database.getInstance().getAllWordsCount());
            startTranslationMemory();
        }
        // user wants to login
        else if (input == 4) {

            String[] credentials = View.showLoginMask();

            // username equals admin
            if (credentials[0].equals(Admin.getUsername())) {

                // if password equals admins password, open admin menu
                if (Database.checkPassword(credentials[1], true)) {

                    // open admin menu
                    handleAdminMenu(View.showAdminMask());
                }
                // if not equal, open login error mask
                else {

                    // open login error mask
                    handleLoginError();
                }
            }
            // username equals translator
            else if (credentials[0].equals(Translator.getUsername())) {

                // if password equals admins password, open admin menu
                if (Database.checkPassword(credentials[1], false)) {

                    // open translator menu
                    handleTranslatorMenu(View.showTranslatorMask());
                }
                // if not equal, open login error mask
                else {

                    // open login error mask
                    handleLoginError();
                }
            }
            // username does not exist
            else {

                // open login error mask because username does not exist
                handleLoginError();
            }
        }
        // user wants to exit the programm
        else {

            // terminate the programm
            System.exit(0);
        }
    }

    private void handleSearchWordMask(String wordToSearch) throws IOException {

        ArrayList<Integer[]> linkedWord = Database.getInstance().getWord(wordToSearch);

        // word could be found
        if (linkedWord != null) {

            // show found word and go back to menu
            View.showFoundWord(false, linkedWord);
            startTranslationMemory();
        } else {

            // word could not be found
            handleWordNotFoundMask(View.showNotFoundWord(), wordToSearch);
        }
    }

    private void handleWordNotFoundMask(String userInputAfterNotFound, String searchedWord) throws IOException {

        if (userInputAfterNotFound.equals("create")) {

            // add word
            if (Database.getInstance().createWord(searchedWord)) {

                View.showSuccess();
                // add languages to linked words list
                Database.getInstance().addLanguagesToLinkedWords();
            } else {

                View.showError();
            }
        }

        startTranslationMemory();
    }

    private void handleLoginError() throws IOException {

        if (View.showLoginError().equals("t")) {

            handleUserInputFromStart(4);
        } else {

            startTranslationMemory();
        }
    }

    private void handleAdminMenu(int i) throws IOException {

        // admin wants to add a language
        if (i == 1) {

            Language language = new Language(View.showAddLanguageMask());

            //language could not be added
            if (!Database.getInstance().addLanguage(language)) {

                // show user failure message
                View.showError();
            }
            //language could be added
            else {

                // show user success message
                View.showSuccess();

                // add languages to linked words list
                Database.getInstance().addLanguagesToLinkedWords();
            }

            // go back to menu
            handleAdminMenu(View.showAdminMask());
        }
        // admin wants to search word and show translation
        else if (i == 2) {

            ArrayList<Integer[]> linkedWord = Database.getInstance().getWord(View.showSearchWordMask());

            if (linkedWord != null) {

                View.showFoundWord(false, linkedWord);
            } else {

                View.showNotFoundWordWithoutCreate();
            }

            handleAdminMenu(View.showAdminMask());
        }
        // admin wants to add language to translator
        else if (i == 3) {

            // open add language menu
            handleAddLanguageToTranslatorMask(View.showLanguagesFromTranslator(Database.getInstance().getLanguagesFromTranslator()));
        }
        // going back to menu
        else {

            startTranslationMemory();
        }
    }

    private void handleAddLanguageToTranslatorMask(String input) throws IOException {

        if (input.equals("add")) {

            // admin wants do add language to translator
            if (Database.getInstance().addLanguageToTranslator(View.showAddLanguageToTranslatorMask(Database.getInstance().getLanguagesAsStrings()))) {

                View.showSuccess();
            }
        }

        // open admin menu
        handleAdminMenu(View.showAdminMask());
    }

    private void handleTranslatorMenu(int i) throws IOException {

        // translator wants to search a word
        if (i == 1) {

            // search word
            handleSearchWordMaskTranslaor(View.showSearchWordMask());
        }
        // translator wants to show his translated words count
        else if (i == 2) {

            // show translated words count
            View.showTranslatedWords(Database.getInstance().getTranslatedWordsCount());
        }
        // translator wants to show words with missing translations
        else if (i == 3) {

            // save all words with missing translations into variable and create new array with translation percents of words
            ArrayList<ArrayList<Integer[]>> wordsWithMissingTranslations = Database.getInstance().getWordsWithMissingTranslations();
            Integer[] percents = calculatePercentsArray(wordsWithMissingTranslations);

            // show words with missing translation
            String translatorInput = View.showWordsWithMissingTranslations(wordsWithMissingTranslations, percents);

            // if word did exist
            if (doesWordExist(wordsWithMissingTranslations, translatorInput)) {

                // start translation
                translateWord(translatorInput);
            }
        }
        // translator wants to show count of all words
        else if (i == 4) {

            // show count of all words
            View.showAllWordsCount(Database.getInstance().getAllWordsCount());

            // open translator menu
            handleTranslatorMenu(View.showTranslatorMask());
        }

        // if input was not 1-4, go back go main menu
        startTranslationMemory();
    }

    private boolean doesWordExist(ArrayList<ArrayList<Integer[]>> wordsWithMissingTranslations, String translatorInput) {

        // go through every word
        for (ArrayList<Integer[]> word : wordsWithMissingTranslations) {

            // check if translatorInput is word from wordsList
            if(Database.getInstance().getWordString(word.get(0)[0]).equals(translatorInput)) {

                // if word exists, return true
                return true;
            }
        }

        return false;
    }

    private Integer[] calculatePercentsArray(ArrayList<ArrayList<Integer[]>> wordsWithMissingTranslations) {

        Integer[] percents = new Integer[wordsWithMissingTranslations.size()];

        int index = 0;
        // go through every word in words with missing translations
        for (ArrayList<Integer[]> word : wordsWithMissingTranslations) {

            int nullObjects = 0;
            double translationsSize = word.size();

            // go through every translation and count the missing translations
            for (Integer[] translation : word) {

                if(translation[0] == null) {

                    nullObjects += 1;
                }
            }

            double existingTranslations = (translationsSize - nullObjects);
            double onePercent = (translationsSize / 100);
            double existingPercent = existingTranslations / onePercent;

            percents[index] = (int)existingPercent;

            index += 1;
        }

        return percents;
    }

    private void handleSearchWordMaskTranslaor(String wordToSearch) throws IOException {

        // search word in database and save into object
        ArrayList<Integer[]> linkedWord = Database.getInstance().getWord(wordToSearch);

        // if word could be found
        if (linkedWord != null) {

            View.showFoundWord(false, linkedWord);
        }
        // if word could not be found
        else {

            // show word could not be found
            View.showNotFoundWordWithoutCreate();
        }

        // show menu
        handleTranslatorMenu(View.showTranslatorMask());
    }

    private void translateWord(String translatorInput) throws IOException {

        // search word in database and save into object
        ArrayList<Integer[]> linkedWord = Database.getInstance().getWord(translatorInput);

        // show "foundWordMask" with translator output, and save translator input into String object
        String enteredLanguage = View.showEnterLanguage();

        // check if translator input does exsist in languages
        if (Database.getInstance().getLanguagesAsStrings().contains(enteredLanguage)) {

            // check if translation is missing for entered language
            if (Database.getInstance().translationForLanguageMissing(linkedWord, enteredLanguage)) {

                // user wants to translate
                // check if translator has language permission
                if (Database.getInstance().getTranslatorLanguages().contains(Database.getInstance().getLanguageID(enteredLanguage))) {

                    // if permissions are there, add translation word
                    // if it worked, show success, if not show error
                    if (Database.getInstance().addTranslatedWord(translatorInput, enteredLanguage, View.showTypeTranslatedWordMask(translatorInput))) {

                        // translation could be added
                        View.showSuccess();
                    } else {

                        // translation could not be added
                        View.showError();
                    }
                }
                // translator has no language permission
                else {

                    // show permissions error
                    View.showNoLanguagePermission();
                }
            }
            // pair with null and entered language does not exist
            else {

                // show error
                View.showTranslationAlreadyExist();
            }

            // open translator mask
            handleTranslatorMenu(View.showTranslatorMask());
        }
    }

}
