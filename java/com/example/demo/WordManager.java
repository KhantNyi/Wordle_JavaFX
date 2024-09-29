package com.example.demo;

public class WordManager {

    private String secretWord;

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord.toUpperCase();
    }

    public boolean processGuess(String guess) {
        return guess.equals(secretWord);
    }

    public char getSecretCharAt(int index) {
        return secretWord.charAt(index);
    }

    public boolean containsChar(char letter) {
        return secretWord.contains(String.valueOf(letter));
    }

    public String getSecretWord() {
        return secretWord;
    }
}
