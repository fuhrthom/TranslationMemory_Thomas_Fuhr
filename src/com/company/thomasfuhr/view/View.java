package com.company.thomasfuhr.view;

import com.company.thomasfuhr.model.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class View {

    public static int showInitialMenu() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter \"1\" to open translation section, \"2\" to see your createdWords count, \"3\" to see the count of all words, \"4\" to open login-mask or anything else to exit the programm.");

        try {

            // parse input value to integer and return
            return Integer.parseInt(br.readLine());
        } catch (NumberFormatException nfe) {

            // terminate the programm
            System.exit(0);
        }

        return 0;
    }

    public static int showTranslatorMask() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter \"1\" to open translation section, " +
                "\"2\" to see your translated words count, " +
                "\"3\" to see all words with missing translation, " +
                "\"4\" to show the count of all words or anything else to go back.");

        try {

            // parse input value to integer and return
            return Integer.parseInt(br.readLine());
        } catch (NumberFormatException e) {

            // terminate the programm
            return 0;
        }
    }

    public static int showAdminMask() {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter \"1\" to add a language, \"2\" to search a word and show the translations, \"3\" to add a language to translator or anything else to go back to main menu.");

        try {

            // parse input value to integer and return
            return Integer.parseInt(br.readLine());
        } catch (NumberFormatException | IOException nfe) {

            return 0;
        }
    }


    public static String[] showLoginMask() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter username:");
        String username = br.readLine();

        System.out.println("Enter password:");
        String password = br.readLine();

        return new String[]{username, password};
    }


    public static String showLoginError() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.err.println("Wrong username or password! Type \"t\" to try again or anything else to go back.");

        return br.readLine();
    }

    public static String showAddLanguageMask() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the language you want to add:");

        return br.readLine();
    }

    public static String showLanguagesFromTranslator(ArrayList<String> languagesFromTranslator) throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("The following languages are already in translator: " + languagesFromTranslator);
        System.out.println("Type \"add\" if you want to add an language to translator or anything else to go back.");

        return br.readLine();
    }

    public static String showAddLanguageToTranslatorMask(Collection<String> languages) throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the language to be added to translator (" + languages + ") or exit to go back to menu:");

        return br.readLine();
    }

    public static String showSearchWordMask() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the word you want to search:");

        return br.readLine();
    }

    public static String showWordsWithMissingTranslations(ArrayList<ArrayList<Integer[]>> wordsWithMissingTranslations, Integer[] percents) throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Here are translations missing: \n");

        int index = 0;
        for (ArrayList<Integer[]> word : wordsWithMissingTranslations) {

            System.out.println("- " + Database.getInstance().getWordString(word.get(0)[0]) + " " + percents[index] + " %");

            for (Integer[] translation : word) {

                String translatedWord = "(keine)";

                try {

                    translatedWord = Database.getInstance().getWordString(translation[0]);
                } catch (NullPointerException ignored) {
                }

                System.out.println(Database.getInstance().getLanguageString(translation[1]) + ": " + translatedWord);
            }

            System.out.println();

            index += 1;
        }

        System.out.println("\nTo Translate a word, type the word. Enter anything to go back.\n");

        return br.readLine();
    }

    public static String showEnterLanguage() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the language you want to add:");

        return br.readLine();
    }

    public static String showTypeTranslatedWordMask(String translatorInput) throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter the word in " + translatorInput + ". It will be added.");

        return br.readLine();
    }

    public static String showNotFoundWord() throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.err.println("Your word was not found. If you want to create it, enter \"create\" (You can only create words with letters in your language. Numbers and Symbols are not allowed). To go back enter anything else.");

        return br.readLine();
    }


    public static void showSuccess() {
        System.out.println("-----Successfull-----");
    }

    public static void showError() {
        System.err.println("-----Operation could not be completed-----");
    }

    public static void showFoundWord(boolean isTranslator, ArrayList<Integer[]> linkedWord) throws IOException {

        // create bufferedReader to handle userinput
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Your word was found: \n");

        for (Integer[] translation : linkedWord) {

            String translatedWord = "(keine)";

            try {

                translatedWord = Database.getInstance().getWordString(translation[0]);
            } catch (NullPointerException ignored) {
            }

            System.out.println(Database.getInstance().getLanguageString(translation[1]) + ": " + translatedWord);
        }

        if (isTranslator) {

            System.out.println("\nTo Translate the word, type the language. ");
        }
        System.out.print("Enter anything to go back.\n");

        br.readLine();
    }

    public static void showNotFoundWordWithoutCreate() {

        System.err.println("Your word was not found. Going back to menu.");
    }

    public static void showUserCreatedWordsCount(int wordsCreated) {

        System.out.println("You have created " + wordsCreated + " word(s).");
    }

    public static void showAllWordsCount(int allWordsCount) {

        System.out.println("There are " + allWordsCount + " word(s).");
    }

    public static void showNoLanguagePermission() {
        System.err.println("-----You have no permission for this language. Please ask your admin to add the permission.-----");
    }

    public static void showTranslatedWords(int translatedWords) {

        System.out.println("You have translated " + translatedWords + " word(s).");
    }

    public static void showTranslationAlreadyExist() {

        System.err.println("-----This translation already exists.-----");
    }

    public static void showRegexNotMatching() {

        System.err.println("-----You can only create words with letters in your language. Numbers and Symbols are not allowed.-----");
    }

    public static void showNoWordsWithMissingTranslationFound() {

        System.err.println("-----No words with missing translation could be found.-----");
    }

    public static void showAdminLanguageDoesNotExist() {

        System.err.println("-----Your language could not be found. Please try again and make sure spell it correct.-----");
    }
}
