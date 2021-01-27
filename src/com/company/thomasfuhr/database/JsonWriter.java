package com.company.thomasfuhr.database;

import com.company.thomasfuhr.datatypes.LinkedWords;
import com.company.thomasfuhr.datatypes.Word;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JsonWriter {

    public static boolean writeLanguagesFileToSystem(JsonArray languagesToWriteToJSON) {

        // try to create a file with given string
        try (FileWriter file = new FileWriter("languages.json")) {

            JsonObject jsonObject = new JsonObject();
            // add key languages and object arrayList<Hashmap> into json object
            jsonObject.put("languages", languagesToWriteToJSON);

            // write into json file
            file.write(jsonObject.toJson());

            // return success
            return true;
        } catch (IOException e) {

            System.err.println(e.getMessage());
            // return failure
            return false;
        }
    }

    public static boolean writeWordsFileToSystem(JsonArray wordsToWriteToJSON) {

        // try to create a file with given string
        try (FileWriter file = new FileWriter("words.json")) {

            JsonObject jsonObject = new JsonObject();
            // add key languages and object arrayList<Hashmap> into json object
            jsonObject.put("words", wordsToWriteToJSON);

            // write into json file
            file.write(jsonObject.toJson());

            // return success
            return true;
        } catch (IOException e) {

            System.err.println(e.getMessage());
            // return failure
            return false;
        }
    }

    public static void writeTranslatorFileToSystem(ArrayList<Integer> dataToWriteToJSON, int wordsTranslated) {

        // try to create a file with given string
        try (FileWriter file = new FileWriter("translator.json")) {

            JsonObject jsonObject = new JsonObject();
            // add key languages and object arrayList<Integer> into json object
            jsonObject.put("languages", dataToWriteToJSON);
            jsonObject.put("wordsTranslated", wordsTranslated);

            // write into json file
            file.write(jsonObject.toJson());

            // return success
        } catch (IOException e) {

            System.err.println(e.getMessage());
            // return failure
        }
    }

    public static boolean writeWordToLinkedFileWithGermanLanguage(Word word, LinkedWords linkedWords) {

        try {

            // try reading exsisting file from system, if failing, catch block will be executed
            linkedWords = JsonReader.readExistingLinkedWordsFileFromSystem();

            // if file did exist, add new Integer[] with the wordID and languageID
            linkedWords.linkedWords.get(0).add(new ArrayList<>() {{

                add(new Integer[]{word.getID(), Database.getInstance().getLanguageID("Deutsch")});
            }});

        } catch (IOException e) {

            // no exsisting file was found
            // create new instance of linkedWords object and add an empty arraylist into it
            linkedWords = new LinkedWords();

            // add an empty arraylist add the linked word within its language
            ArrayList<ArrayList<ArrayList<Integer[]>>> wordgroups = new ArrayList<>();
            ArrayList<ArrayList<Integer[]>> words = new ArrayList<>();
            ArrayList<Integer[]> translations = new ArrayList<>();

            translations.add(new Integer[]{word.getID(), Database.getInstance().getLanguageID("Deutsch")});
            words.add(translations);
            wordgroups.add(words);

            linkedWords.linkedWords = wordgroups;
        }

        // write the JSONArray to file system
        // try to create a file with given string
        try (FileWriter file = new FileWriter("linkedWords.json")) {

            JsonObject jsonObject = new JsonObject();
            // add key languages and object arrayList<arraylist<Integer[]>> into json object
            jsonObject.put("linkedWords", linkedWords.linkedWords);

            // write into json file
            file.write(jsonObject.toJson());

            // return success
            return true;
        } catch (IOException e) {

            System.err.println(e.getMessage());
            // return failure
            return false;
        }
    }

    public static boolean writeLinkedWordsToFile(LinkedWords linkedWords) {

        try (FileWriter file = new FileWriter("linkedWords.json")) {

            JsonObject jsonObject = new JsonObject();
            // add key languages and object arrayList<arraylist<Integer[]>> into json object
            jsonObject.put("linkedWords", linkedWords.linkedWords);

            // write into json file
            file.write(jsonObject.toJson());

            // return success
            return true;
        } catch (IOException e) {

            System.err.println(e.getMessage());
            // return failure
            return false;
        }
    }

}
