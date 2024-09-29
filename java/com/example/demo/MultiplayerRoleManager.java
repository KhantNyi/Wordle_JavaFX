package com.example.demo;

public class MultiplayerRoleManager {
    private boolean isPlayer1SettingWord;
    private WordleGame wordleGame;
    private MultiPlayerUIManager uiManager;

    public MultiplayerRoleManager(WordleGame wordleGame, MultiPlayerUIManager uiManager) {
        this.wordleGame = wordleGame;
        this.uiManager = uiManager;
        this.isPlayer1SettingWord = true;
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
        wordleGame.startNewMultiPlayerGame(word);
        uiManager.transitionToGuessingPhase(!isPlayer1SettingWord);
    }
}