package com.example.demo;

public class MultiplayerRoleManager {
    private boolean isPlayer1SettingWord;
    private WordleGame wordleGame;
    private MultiPlayerUIManager uiManager;
    private int wordLength;

    public MultiplayerRoleManager(WordleGame wordleGame, MultiPlayerUIManager uiManager) {
        this.wordleGame = wordleGame;
        this.uiManager = uiManager;
        this.isPlayer1SettingWord = true;
        this.wordLength = wordleGame.getWordLength();
    }

    public void initializeGame() {
        isPlayer1SettingWord = true;
        wordleGame.resetGameState();
        uiManager.updateUIForNewGame(isPlayer1SettingWord);
    }

    public void switchRoles() {
        isPlayer1SettingWord = !isPlayer1SettingWord;
        wordleGame.resetGameState();
        uiManager.updateUIForRoleSwitch(isPlayer1SettingWord);
    }

    public boolean isPlayer1SettingWord() {
        return isPlayer1SettingWord;
    }

    public void handleWordSet(String word) {
        if (word.length() != wordLength) {
            uiManager.showAlert("Invalid word length. Please enter a " + wordLength + "-letter word.");
            return;
        }

        // Use the WordValidator from WordleGame
        if (!wordleGame.getWordValidator().isValidWord(word, wordLength)) {
            uiManager.showAlert("Invalid word. Please enter a valid " + wordLength + "-letter word.");
            return;
        }

        wordleGame.startNewMultiPlayerGame(word);
        uiManager.transitionToGuessingPhase(!isPlayer1SettingWord);
    }

    public void setWordLength(int length) {
        this.wordLength = length;
    }

    public int getWordLength() {
        return this.wordLength;
    }
}