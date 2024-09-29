package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordValidator {
    private Set<String> validWords;
    private List<String> wordList;  // New list to store words for random selection
    private Random random;  // Random object for selecting random words

    public WordValidator() {
        validWords = new HashSet<>();
        wordList = new ArrayList<>();  // Initialize the word list
        random = new Random();  // Initialize the random object
        loadValidWords();
    }

    private void loadValidWords() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("5-letter-words-list.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toUpperCase();
                validWords.add(word);
                wordList.add(word);  // Add the word to the list as well
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error loading valid words: " + e.getMessage());
        }
    }

    public boolean isValidWord(String word) {
        return validWords.contains(word.toUpperCase());
    }

    // New method to get a random word
    public String getRandomWord() {
        if (wordList.isEmpty()) {
            throw new IllegalStateException("No words available");
        }
        return wordList.get(random.nextInt(wordList.size()));
    }
}