package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class WordValidator {
    private Map<Integer, Set<String>> validWords;
    private Map<Integer, List<String>> wordLists;
    private Random random;

    public WordValidator() {
        validWords = new HashMap<>();
        wordLists = new HashMap<>();
        random = new Random();
        loadValidWords();
    }

    private void loadValidWords() {
        loadWordsFromFile(5);  // Load 5-letter words from the appropriate file
        loadWordsFromFile(6);
    }

    private void loadWordsFromFile(int wordLength) {
        Set<String> wordSet = new HashSet<>();
        List<String> wordList = new ArrayList<>();
        String filename;

        // Determine the filename based on the word length
        if (wordLength == 5) {
            filename = "5-letter-words-list.txt";
        } else if (wordLength == 6) {
            filename = "6-letter-words-list.txt";
        } else {
            System.err.println("Unsupported word length: " + wordLength);
            return;
        }

        // Load words from the appropriate file
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toUpperCase();
                if (word.length() == wordLength) {
                    wordSet.add(word);
                    wordList.add(word);
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading valid words from " + filename + ": " + e.getMessage());
        }

        // Store the loaded words in the maps
        validWords.put(wordLength, wordSet);
        wordLists.put(wordLength, wordList);
    }


    public boolean isValidWord(String word, int wordLength) {
        Set<String> words = validWords.get(wordLength);
        if (words == null) {
            return false;
        }
        return words.contains(word.toUpperCase());
    }

    public String getRandomWord(int wordLength) {
        List<String> wordList = wordLists.get(wordLength);
        if (wordList == null || wordList.isEmpty()) {
            throw new IllegalStateException("No words available for length " + wordLength);
        }
        return wordList.get(random.nextInt(wordList.size()));
    }

    public boolean isWordLengthSupported(int wordLength) {
        return validWords.containsKey(wordLength) && !validWords.get(wordLength).isEmpty();
    }

    public int getWordCount(int wordLength) {
        Set<String> words = validWords.get(wordLength);
        return words != null ? words.size() : 0;
    }
}