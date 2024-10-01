package com.example.demo;

public class WordleGame {

    private boolean isGameWon;
    private String secretWord;
    private int attempts;
    private int currentRow;
    private StringBuilder currentGuess;
    private SinglePlayerUIManager singlePlayerUIManager;
    private MultiPlayerUIManager multiPlayerUIManager;
    private StatisticsManager statisticsManager;
    private WordValidator wordValidator;
    private boolean gameCompleted;
    private boolean isMultiplayerMode;
    private boolean gameOver;
    private Main mainApplication;
    private static final int[] SCORES = {100, 90, 80, 70, 60, 50};
    private int score;
    private MultiplayerRoleManager roleManager;
    private int wordLength;

    public WordleGame(StatisticsManager statisticsManager, WordValidator wordValidator) {
        this.statisticsManager = statisticsManager;
        this.wordValidator = wordValidator;
        this.wordLength = 5; // Default to 5-letter mode
        this.currentGuess = new StringBuilder();
    }

    public WordValidator getWordValidator() {
        return this.wordValidator;
    }

    public void setMainApplication(Main mainApplication) {
        this.mainApplication = mainApplication;
    }

    public void setMultiplayerRoleManager(MultiplayerRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setSinglePlayerUIManager(SinglePlayerUIManager manager) {
        this.singlePlayerUIManager = manager;
    }

    public void setMultiPlayerUIManager(MultiPlayerUIManager manager) {
        this.multiPlayerUIManager = manager;
    }

    public void setWordLength(int length) {
        this.wordLength = length;
    }

    public int getWordLength() {
        return this.wordLength;
    }

    public void startNewSinglePlayerGame() {
        isMultiplayerMode = false;
        resetGameState();
        setRandomSecretWord();
        System.out.println("Secret word: " + secretWord); // debug
        statisticsManager.incrementSinglePlayerGamesPlayed(wordLength);
    }

    public void resetGameState() {
        this.attempts = 0;
        this.currentRow = 0;
        this.currentGuess.setLength(0);
        this.gameCompleted = false;
        this.gameOver = false;
        this.isGameWon = false;
        this.secretWord = null;
    }

    public void startNewMultiPlayerGame(String secretWord) {
        isMultiplayerMode = true;
        resetGameState();
        this.secretWord = secretWord.toUpperCase();
        System.out.println("Secret word: " + secretWord); // debug
        statisticsManager.incrementMultiPlayerGamesPlayed(wordLength);
    }

    private void setRandomSecretWord() {
        setSecretWord(wordValidator.getRandomWord(wordLength));
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord.toUpperCase();
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void handleKeyPress(String letter) {
        if (!gameCompleted && currentGuess.length() < wordLength) {
            currentGuess.append(letter);
            updateUI(currentRow, currentGuess.length() - 1, letter);
        }
    }

    public void handleBackspace() {
        if (!gameCompleted && currentGuess.length() > 0) {
            currentGuess.setLength(currentGuess.length() - 1);
            updateUI(currentRow, currentGuess.length(), "");
        }
    }

    public void processGuess(String guess) {
        if (!gameCompleted && guess.length() == wordLength) {
            if (wordValidator.isValidWord(guess, wordLength)) {
                attempts++;

                for (int i = 0; i < wordLength; i++) {
                    char guessChar = guess.charAt(i);
                    char correctChar = secretWord.charAt(i);
                    updateGuessFeedback(currentRow, i, guessChar, correctChar);
                }
                updateKeyboardFeedback(guess, secretWord);

                if (guess.equals(secretWord)) {
                    isGameWon = true;
                    score = SCORES[attempts - 1];
                    handleGameEnd(true);
                } else if (attempts >= 6) {
                    isGameWon = false;
                    score = 0;
                    handleGameEnd(false);
                } else {
                    currentRow++;
                    currentGuess.setLength(0);
                }
            } else {
                showAlert("Invalid word! Please enter a valid " + wordLength + "-letter word from the dictionary.");
                for (int i = 0; i < wordLength; i++) {
                    updateUI(currentRow, i, "");
                }
                currentGuess.setLength(0);
            }
        }
    }

    private void handleGameEnd(boolean isWin) {
        if (!gameCompleted) {
            gameCompleted = true;
            if (isMultiplayerMode) {
                handleMultiplayerGameEnd(isWin);
            } else {
                handleSinglePlayerGameEnd(isWin);
            }
        }
    }

    private void handleSinglePlayerGameEnd(boolean isWin) {
        if (isWin) {
            statisticsManager.incrementSinglePlayerWins(wordLength);
            statisticsManager.addSinglePlayerGuessesForWin(attempts, wordLength);
            statisticsManager.addScore(score, wordLength);
            showAlert("Congratulations! You've guessed the word in " + attempts + " attempts. Score: " + score);
        } else {
            statisticsManager.incrementSinglePlayerLosses(wordLength);
            statisticsManager.addScore(0, wordLength);
            showAlert("Game Over! The word was: " + secretWord + ". Score: 0");
        }
        updateStats(statisticsManager.getSinglePlayerStatistics(wordLength), true);
        singlePlayerUIManager.endGame();
    }

    private void handleMultiplayerGameEnd(boolean isWin) {
        boolean isPlayer1Guessing = !roleManager.isPlayer1SettingWord();
        if (isWin) {
            if (isPlayer1Guessing) {
                statisticsManager.incrementPlayer1Wins(wordLength);
                statisticsManager.addMultiPlayerScore(score, 1, wordLength);
                showAlert("Player 1 wins! They guessed the word in " + attempts + " attempts.\nScore: " + score);
            } else {
                statisticsManager.incrementPlayer2Wins(wordLength);
                statisticsManager.addMultiPlayerScore(score, 2, wordLength);
                showAlert("Player 2 wins! They guessed the word in " + attempts + " attempts.\nScore: " + score);
            }
            statisticsManager.addMultiPlayerGuessesForWin(attempts, wordLength);
        } else {
            if (isPlayer1Guessing) {
                statisticsManager.incrementPlayer1Losses(wordLength);
                showAlert("Round over. Player 1 couldn't guess the word: " + secretWord + "\nNo points awarded.");
            } else {
                statisticsManager.incrementPlayer2Losses(wordLength);
                showAlert("Round over. Player 2 couldn't guess the word: " + secretWord + "\nNo points awarded.");
            }
        }
        updateStats(statisticsManager.getMultiPlayerStatistics(wordLength), true);
        multiPlayerUIManager.endGame();
    }

    public int getScore() {
        return score;
    }

    public boolean isMultiplayerMode() {
        return isMultiplayerMode;
    }

    public void switchRolesMultiplayer() {
        resetGameState();
        if (roleManager != null) {
            roleManager.switchRoles();
            if (multiPlayerUIManager != null) {
                multiPlayerUIManager.resetForNewRound(roleManager.isPlayer1SettingWord());
            } else {
                System.err.println("Error: MultiPlayerUIManager is null in switchRolesMultiplayer");
            }
        } else {
            System.err.println("Error: MultiplayerRoleManager is null in switchRolesMultiplayer");
        }
    }

    private void updateUI(int row, int col, String letter) {
        if (isMultiplayerMode) {
            if (multiPlayerUIManager != null) {
                multiPlayerUIManager.updateGrid(row, col, letter);
            } else {
                System.err.println("Error: MultiPlayerUIManager is null in updateUI");
            }
        } else {
            if (singlePlayerUIManager != null) {
                singlePlayerUIManager.updateGrid(row, col, letter);
            } else {
                System.err.println("Error: SinglePlayerUIManager is null in updateUI");
            }
        }
    }

    private void updateGuessFeedback(int row, int col, char guessChar, char correctChar) {
        if (isMultiplayerMode) {
            if (multiPlayerUIManager != null) {
                multiPlayerUIManager.updateGuessFeedback(row, col, guessChar, correctChar);
            } else {
                System.err.println("Error: MultiPlayerUIManager is null in updateGuessFeedback");
            }
        } else {
            if (singlePlayerUIManager != null) {
                singlePlayerUIManager.updateGuessFeedback(row, col, guessChar, correctChar);
            } else {
                System.err.println("Error: SinglePlayerUIManager is null in updateGuessFeedback");
            }
        }
    }

    private void updateKeyboardFeedback(String guess, String secretWord) {
        if (isMultiplayerMode) {
            if (multiPlayerUIManager != null) {
                multiPlayerUIManager.updateKeyboardFeedback(guess, secretWord);
            } else {
                System.err.println("Error: MultiPlayerUIManager is null in updateKeyboardFeedback");
            }
        } else {
            if (singlePlayerUIManager != null) {
                singlePlayerUIManager.updateKeyboardFeedback(guess, secretWord);
            } else {
                System.err.println("Error: SinglePlayerUIManager is null in updateKeyboardFeedback");
            }
        }
    }

    private void showAlert(String message) {
        if (isMultiplayerMode) {
            if (multiPlayerUIManager != null) {
                multiPlayerUIManager.showAlert(message);
            } else {
                System.err.println("Error: MultiPlayerUIManager is null in showAlert");
            }
        } else {
            if (singlePlayerUIManager != null) {
                singlePlayerUIManager.showAlert(message);
            } else {
                System.err.println("Error: SinglePlayerUIManager is null in showAlert");
            }
        }
    }

    public int getCurrentRow() {
        return currentRow;
    }

    private void updateStats(String stats, boolean isVisible) {
        if (isMultiplayerMode) {
            if (multiPlayerUIManager != null) {
                multiPlayerUIManager.updateStats(stats, isVisible);
            } else {
                System.err.println("Error: MultiPlayerUIManager is null in updateStats");
            }
        } else {
            if (singlePlayerUIManager != null) {
                singlePlayerUIManager.updateStats(stats, isVisible);
            } else {
                System.err.println("Error: SinglePlayerUIManager is null in updateStats");
            }
        }
    }

    public void returnToMainScreen() {
        if (mainApplication != null) {
            mainApplication.returnToMainScreen();
        } else {
            System.err.println("Error: Main application reference is null");
        }
    }
}