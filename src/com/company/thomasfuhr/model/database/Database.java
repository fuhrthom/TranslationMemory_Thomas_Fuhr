package com.company.thomasfuhr.model.database;

import com.company.thomasfuhr.model.datatypes.*;
import com.company.thomasfuhr.model.usertypes.Admin;
import com.company.thomasfuhr.model.usertypes.Translator;
import com.github.cliftonlabs.json_simple.JsonArray;
import java.io.IOException;
import java.util.*;

public class Database {

    // singleton pattern start
    static Database database = new Database();

    public static Database getInstance() {

        return database;
    }
    // singleton pattern end

    Words words;
    Languages languages;
    LinkedWords linkedWords;
    Translator translator;

    int wordsCreated = 0;
    int wordsTranslated = 0;

    public int getWordsCreated() {

        // return int value
        return wordsCreated;
    }

    private int getNextID(ArrayList<HashMap<Integer, String>> arrayList) {

        // default value, if no file was found
        int nextID = 1;

        try {

            // try to create an iterator of the generated entrySet of the hashmap entrys
            Iterator<HashMap.Entry<Integer, String>> it = arrayList.get(0).entrySet().iterator();

            // iterate through it until the last entry
            while (it.hasNext()) {

                Map.Entry<Integer, String> pair = it.next();

                // if the last entry has been found
                if (!it.hasNext()) {

                    // set nextID to the last entrys ID value
                    nextID = pair.getKey();
                }
            }
            // add 1 to this value to get the next ID
            nextID += 1;
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {

            // index out ouf bounds exception -> return 1 because no entry was there
        }

        // if the iterator could be created, the value is != 1, if not this value is 1
        return nextID;
    }

    public int getLanguageID(String language) {

        // try rading existing languages from file
        try {

            languages = JsonReader.readExistingLanguagesFileFromSystem();
        } catch (IOException e) {

            // file could not be read
        }

        try {

            // iterate through it until the last entry
            for (Map.Entry<Integer, String> pair : languages.languages.get(0).entrySet()) {

                // if the language entry has been found
                if (pair.getValue().equals(language)) {

                    // return the ID
                    return pair.getKey();
                }
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {

            // failure because no languages file existing
        }

        return -1;
    }

    private int getWordID(String wordToFind) {

        // try read existing words from file
        try {

            words = JsonReader.readExistingWordsFileFromSystem();
        } catch (IOException e) {

            // file could not be read
        }

        try {

            // iterate through it until the last entry
            for (Map.Entry<Integer, String> pair : words.words.get(0).entrySet()) {

                // if the language entry has been found
                if (pair.getValue().equals(wordToFind)) {

                    // return the ID
                    return pair.getKey();
                }
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {

            // failure because word could not be found, no words could be read from file
        }

        return -1;
    }

    public int getAllWordsCount() {

        // try reading existing words from file
        try {

            words = JsonReader.readExistingWordsFileFromSystem();

            return words.words.get(0).size();
        } catch (IOException e) {

            // failure, no size could be found because no data available
        }

        return 0;
    }

    public int getTranslatedWordsCount() {

        // read translator data from file, if not exist return 0
        try {

            translator = JsonReader.readExistingTranslatorFileFromSystem();
        } catch (IOException e) {

            return 0;
        }

        return translator.getWordsTranslated();
    }


    public static boolean checkPassword(String password, boolean isAdmin) {

        if (isAdmin) {

            return password.equals(Admin.getPassword());
        } else {

            return password.equals(Translator.getPassword());
        }
    }


    public boolean addLanguage(Language language) {

        JsonArray languagesToWriteToJSON = new JsonArray();

        try {

            // try reading exsisting file from system, if failing, catch block will be executed
            languages = JsonReader.readExistingLanguagesFileFromSystem();

            // if file did exist, check if the hasmap with the languages contains the to be added language
            if (!languages.languages.get(0).containsValue(language.getLanguage())) {

                // add the language to the hashmap
                languages.languages.get(0).put(getNextID(languages.languages), language.getLanguage());
            }

            // add hashmap to JSONArray Object which will later be added to JSONObject
            languagesToWriteToJSON.add(0, languages.languages.get(0));

        } catch (IOException e) {

            // no exsisting file was found
            // create new instance of Languages object and add an empty arraylist into it
            languages = new Languages();
            languages.languages = new ArrayList<>();

            // add an empty hashmap to arrayList and also add the language to be added
            languagesToWriteToJSON.add(0, new HashMap<Integer, String>() {{
                put(getNextID(languages.languages), language.getLanguage());
            }});
        }

        // write the JSONArray to file system, if success return true, if not return false
        return JsonWriter.writeLanguagesFileToSystem(languagesToWriteToJSON);
    }

    public boolean addLanguageToTranslator(String language) {

        if (language.equals("exit")) {
            return false;
        }

        try {

            // try reading exsisting file
            translator = JsonReader.readExistingTranslatorFileFromSystem();

            // if file exist, check if language exists
            if (getLanguageID(language) != -1) {

                // if exists, add to translator
                translator.languages.add(getLanguageID(language));
            }

            // save file to system
            JsonWriter.writeTranslatorFileToSystem(translator.languages, wordsTranslated);

            return true;
        } catch (IOException e) {

            // init translator object
            translator = new Translator();
            translator.languages = new ArrayList<>();

            if (getLanguageID(language) != -1) {

                // if exists, add to translator
                translator.languages.add(getLanguageID(language));
            }

            // save file to system
            JsonWriter.writeTranslatorFileToSystem(translator.languages, wordsTranslated);

            return true;
        }
    }

    public boolean createWord(String wordToAdd) {

        JsonArrayWord wordsAndWord = getExistingWordsAsJsonArrayAndWord(wordToAdd);

        // write the JSONArray to file system
        if (JsonWriter.writeWordsFileToSystem(wordsAndWord.getWords())) {

            wordsCreated += 1;
            // if success, write word into linked.json with language german
            // return success
            return JsonWriter.writeWordToLinkedFileWithGermanLanguage(wordsAndWord.getWord());
        }

        return false;
    }

    public boolean addTranslatedWord(String germanWord, String translatedLanguage, String translationOfWord) {

        // try reading files
        try {

            words = JsonReader.readExistingWordsFileFromSystem();
            linkedWords = JsonReader.readExistingLinkedWordsFileFromSystem();
            translator = JsonReader.readExistingTranslatorFileFromSystem();
        } catch (IOException ignore) {

            // files could not be read
            return false;
        }

        // add translation to words.json
        addWord(translationOfWord);

        // go through every wordgroup
        for (ArrayList<ArrayList<Integer[]>> wordgroups: linkedWords.linkedWords) {

            // go thorugh every word
            for (ArrayList<Integer[]> word : wordgroups) {

                // check if word is equal to the german word
                if(getWordString(word.get(0)[0]).equals(germanWord)) {

                    // go thorugh the translations
                    for (Integer[] translation : word) {

                        // if translation has only language saved into and no word && language equals the translated language
                        // which translator did translate the word to
                        if (translation[0] == null && translation[1] == getLanguageID(translatedLanguage)) {

                            // remove the object in this array with no word paired to langauge
                            word.remove(translation);
                            // add new object with translated word paired to language
                            word.add(new Integer[]{ getWordID(translationOfWord), getLanguageID(translatedLanguage) });

                            // add translation to translation count of translator and save to file
                            wordsTranslated += 1;
                            JsonWriter.writeTranslatorFileToSystem(translator.languages, wordsTranslated);

                            // write linked words to json file and return if its succeeded or not
                            return JsonWriter.writeLinkedWordsToFile(linkedWords);
                        }
                    }
                }
            }
        }

        // return failure
        return false;
    }

    public boolean translationForLanguageMissing(ArrayList<Integer[]> linkedWord, String enteredLanguage) {

        // go through every translation
        for(Integer[] translation : linkedWord) {

            // check if translation is missing at the entered language
            if(translation[1] == database.getLanguageID(enteredLanguage) && translation[0] == null) {

                return true;
            }
        }

        return false;
    }


    public String getLanguageString(int ID) {

        // try read existing languages
        try {

            languages = JsonReader.readExistingLanguagesFileFromSystem();
        } catch (IOException e) {

            System.err.println(e.getMessage());
        }

        try {

            // iterate through it until the last entry
            for (Map.Entry<Integer, String> pair : languages.languages.get(0).entrySet()) {

                // if the language entry has been found
                if (pair.getKey().equals(ID)) {

                    // return the ID
                    return pair.getValue();
                }
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {

            // failure because of index out of bounds
        }

        return "";
    }

    public String getWordString(int ID) {

        // try reading existing words from file
        try {

            words = JsonReader.readExistingWordsFileFromSystem();
        } catch (IOException e) {

            // no file could be found
        }

        try {

            // iterate through it until the last entry
            for (Map.Entry<Integer, String> pair : words.words.get(0).entrySet()) {

                // if the language entry has been found
                if (pair.getKey().equals(ID)) {

                    // return the ID
                    return pair.getValue();
                }
            }
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {

            // failure because of index out of bounds
        }

        return "";
    }


    private JsonArrayWord getExistingWordsAsJsonArrayAndWord(String wordToAdd) {

        JsonArray wordsToWriteToJSON = new JsonArray();
        Word word;

        try {

            // try reading exsisting file from system, if failing, catch block will be executed
            words = JsonReader.readExistingWordsFileFromSystem();

            word = new Word(wordToAdd, getNextID(words.words));

            // if file did exist, check if the hasmap with the languages contains the to be added language
            if (!words.words.get(0).containsValue(wordToAdd)) {

                // add the language to the hashmap
                words.words.get(0).put(getNextID(words.words), word.getWord());
            }

            // add hashmap to JSONArray Object which will later be added to JSONObject
            wordsToWriteToJSON.add(0, words.words.get(0));

        } catch (IOException e) {

            // no exsisting file was found
            // create new instance of Languages object and add an empty arraylist into it
            words = new Words();
            words.words = new ArrayList<>();

            word = new Word(wordToAdd, getNextID(words.words));

            // add an empty hashmap to arrayList and also add the language to be added
            Word finalWord = word;
            wordsToWriteToJSON.add(0, new HashMap<Integer, String>() {{

                put(getNextID(words.words), finalWord.getWord());
            }});
        }

        return new JsonArrayWord(wordsToWriteToJSON, word);
    }


    public ArrayList<String> getLanguagesFromTranslator() {

        try {

            // try reading existing file from system
            translator = JsonReader.readExistingTranslatorFileFromSystem();

            // return languages from translator object
            return new ArrayList<>() {{

                // add every language to new ArrayList
                translator.languages.forEach((language) -> add(getLanguageString(language)));
            }};
        } catch (IOException e) {

            // no file could be found
        }

        return null;
    }


    public ArrayList<Integer> getTranslatorLanguages() {

        // if translator object has not been initialized, do it
        if(translator == null) {

            // try reading existing translator file
            try {

                translator = JsonReader.readExistingTranslatorFileFromSystem();
            } catch (IOException e) {

                // no file could be found, create new object and write to file
                translator = new Translator();
                translator.languages = new ArrayList<>();
                JsonWriter.writeTranslatorFileToSystem(translator.languages, wordsTranslated);

                return translator.languages;
            }
        }

        return translator.languages;
    }


    public ArrayList<Integer[]> getWord(String wordToFind) {

        // capitalize first letter
        wordToFind = wordToFind.substring(0, 1).toUpperCase() + wordToFind.substring(1).toLowerCase();

        // try reading existing file from system
        try {

            words = JsonReader.readExistingWordsFileFromSystem();

            if (words.words.get(0).containsValue(wordToFind)) {

                // read file with linked words
                linkedWords = JsonReader.readExistingLinkedWordsFileFromSystem();

                // go through every wordgroup
                for (ArrayList<ArrayList<Integer[]>> wordGroup : linkedWords.linkedWords) {

                    // go thorugh every word
                    for (ArrayList<Integer[]> word : wordGroup) {

                        // check if word at first translation pair is equal to wordToFind
                        if(getWordString(word.get(0)[0]).equals(wordToFind)) {

                            // if its equal, return the word object with its translation pairs inside
                            return word;
                        }
                    }
                }

                // word could not be found
                return null;
            }
        } catch (IOException e) {

            // no file found
        }

        // file could not be read, word could not be found
        return null;
    }


    public ArrayList<ArrayList<Integer[]>> getWordsWithMissingTranslations() {

        ArrayList<ArrayList<Integer[]>> wordsWithMissingTranslation = new ArrayList<>();

        // try read existing languages, words and linked words
        try {

            languages = JsonReader.readExistingLanguagesFileFromSystem();
            words = JsonReader.readExistingWordsFileFromSystem();
            linkedWords = JsonReader.readExistingLinkedWordsFileFromSystem();
        } catch(IOException e) {

            // one file could not be read
            // return empty array because there are no words
            return wordsWithMissingTranslation;
        }



        // go through every wordgroup
        for (ArrayList<ArrayList<Integer[]>> wordgroup : linkedWords.linkedWords) {

            // go throug every word
            for (ArrayList<Integer[]> word : wordgroup) {

                // go throug every translation
                for (Integer[] translation : word) {

                    // if translation[word] is null, translation is missing
                    if(translation[0] == null) {

                        // add this word to array
                        wordsWithMissingTranslation.add(word);
                        break;
                    }
                }
            }
        }

        // return the words where are missing translation(s)
        return wordsWithMissingTranslation;
    }


    public Collection<String> getLanguagesAsStrings() {

        // try reading existing languages from file
        try {

            languages = JsonReader.readExistingLanguagesFileFromSystem();

            return languages.languages.get(0).values();
        } catch (IOException e) {

            // no file could be found
        }

        return null;
    }


    public void addWord(String wordToAdd) {   // to be commented

        JsonArray wordsToWriteToJSON = getExistingWordsAsJsonArrayAndWord(wordToAdd).getWords();

        // write the JSONArray to file system
        JsonWriter.writeWordsFileToSystem(wordsToWriteToJSON);
    }

    public void addLanguagesToLinkedWords() {

        try {

            // try reading both files from system
            languages = JsonReader.readExistingLanguagesFileFromSystem();
            linkedWords = JsonReader.readExistingLinkedWordsFileFromSystem();

            // go through every wordgroup
            for (ArrayList<ArrayList<Integer[]>> words : linkedWords.linkedWords) {

                // go through every word
                for (ArrayList<Integer[]> word : words) {

                    // get every language ID
                    for (Integer key : languages.languages.get(0).keySet()) {

                        // puffer to save the keys which are already in json file
                        ArrayList<Integer> keysAlreadyThere = new ArrayList<>();
                        // go through every translation
                        for (Integer[] translation : word) {

                            // and check if the language key does exist
                            if (translation[1].equals(key)) {

                                // if exists, add to puffer
                                keysAlreadyThere.add(key);
                            }
                        }

                        // check if the word does not contain a empty translation with given language key
                        // and the key did not exist in this word (puffer array does not contain the key)
                        if(!word.contains(new Integer[]{null, key}) && !keysAlreadyThere.contains(key)) {

                            // if both are true, the language does not have an (empty)translation
                            word.add(new Integer[]{null, key});
                        }
                    }

                    JsonWriter.writeLinkedWordsToFile(linkedWords);
                }
            }
        } catch (IOException e) {

            // languages or linked words could not be load
        }

    }

}
