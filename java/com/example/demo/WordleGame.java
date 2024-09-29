package com.example.demo;

import java.util.Random;

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
    private int currentCol;
    private int player1CurrentCol;
    private int player2CurrentCol;
    private static final int[] SCORES = {100, 90, 80, 70, 60, 50};
    private int score;
    private MultiplayerRoleManager roleManager;

    public WordleGame(StatisticsManager statisticsManager, WordValidator wordValidator) {
        this.statisticsManager = statisticsManager;
        this.wordValidator = wordValidator;
    }

    public void setMainApplication(Main mainApplication) {
        this.mainApplication = mainApplication;
    }

    public void setMultiplayerRoleManager(MultiplayerRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void returnToMainScreen() {
        if (mainApplication != null) {
            mainApplication.returnToMainScreen();
        } else {
            System.err.println("Error: Main application reference is null");
        }
    }

    public void setSinglePlayerUIManager(SinglePlayerUIManager manager) {
        this.singlePlayerUIManager = manager;
        System.out.println("SinglePlayerUIManager set in WordleGame");
    }

    public void setMultiPlayerUIManager(MultiPlayerUIManager manager) {
        this.multiPlayerUIManager = manager;
        System.out.println("MultiPlayerUIManager set in WordleGame");
    }

    public void startNewSinglePlayerGame() {
        isMultiplayerMode = false;
        resetGameState();
        setRandomSecretWord();
        statisticsManager.incrementSinglePlayerGamesPlayed();
        System.out.println("Started new single-player game with secret word: " + this.secretWord);
    }

    public void resetGameState() {
        this.attempts = 0;
        this.currentRow = 0;
        this.currentCol = 0;
        this.currentGuess = new StringBuilder();
        this.gameCompleted = false;
        this.gameOver = false;
        this.isGameWon = false;
        this.secretWord = null;
    }

    public void startNewMultiPlayerGame(String secretWord) {
        isMultiplayerMode = true;
        resetGameState();
        this.secretWord = secretWord.toUpperCase();
        statisticsManager.incrementMultiPlayerGamesPlayed();
        System.out.println("Started new multiplayer game with secret word: " + this.secretWord);
    }

    private void setRandomSecretWord() {
        setSecretWord(wordValidator.getRandomWord());
    }

    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord.toUpperCase();
    }

    public String getSecretWord() {
        return secretWord;
    }

    public void handleKeyPress(String letter) {
        if (!gameCompleted && currentCol < 5) {
            currentGuess.append(letter);
            updateUI(currentRow, currentCol, letter);
            currentCol++;
        }
    }

    public void handleBackspace() {
        if (!gameCompleted && currentCol > 0) {
            currentCol--;
            updateUI(currentRow, currentCol, "");
            currentGuess.deleteCharAt(currentGuess.length() - 1);
        }
    }

    public void processGuess() {
        if (!gameCompleted && currentGuess.length() == 5) {
            String guessWord = currentGuess.toString();
            if (wordValidator.isValidWord(guessWord)) {
                attempts++;

                for (int i = 0; i < 5; i++) {
                    char guessChar = currentGuess.charAt(i);
                    char correctChar = secretWord.charAt(i);
                    updateGuessFeedback(currentRow, i, guessChar, correctChar);
                }
                updateKeyboardFeedback(currentGuess.toString(), secretWord);

                if (currentGuess.toString().equals(secretWord)) {
                    isGameWon = true;
                    score = SCORES[attempts - 1];  // Set score based on number of attempts
                    handleGameEnd(true); // Player wins
                } else if (attempts >= 6) {
                    isGameWon = false;
                    score = 0;  // No score for not solving
                    handleGameEnd(false); // Player loses
                } else {
                    currentRow++;
                    currentCol = 0;
                    currentGuess.setLength(0);
                }
            } else {
                showAlert("Invalid word! Please enter a valid 5-letter word from the dictionary.");
                // Clear the current guess
                for (int i = 0; i < 5; i++) {
                    updateUI(currentRow, i, "");
                }
                currentGuess.setLength(0);
                currentCol = 0;
            }
        }
    }

    private void handleGameEnd(boolean isWin) {
        if (!gameCompleted) {
            gameCompleted = true;
            if (isMultiplayerMode) {
                boolean isPlayer1Guessing = !roleManager.isPlayer1SettingWord();
                if (isWin) {
                    // Guessing player wins, gets points, and increases win count
                    if (isPlayer1Guessing) {
                        statisticsManager.incrementPlayer1Wins();
                        statisticsManager.addMultiPlayerScore(score, 1);
                        showAlert("Player 1 wins! They guessed the word in " + attempts + " attempts.\nScore: " + score);
                    } else {
                        statisticsManager.incrementPlayer2Wins();
                        statisticsManager.addMultiPlayerScore(score, 2);
                        showAlert("Player 2 wins! They guessed the word in " + attempts + " attempts.\nScore: " + score);
                    }
                    statisticsManager.addMultiPlayerGuessesForWin(attempts);
                } else {
                    // Word not guessed, no points awarded, loss count increased for guessing player
                    if (isPlayer1Guessing) {
                        statisticsManager.incrementPlayer1Losses();
                        showAlert("Round over. Player 1 couldn't guess the word: " + secretWord + "\nNo points awarded.");
                    } else {
                        statisticsManager.incrementPlayer2Losses();
                        showAlert("Round over. Player 2 couldn't guess the word: " + secretWord + "\nNo points awarded.");
                    }
                }
                updateStats(statisticsManager.getMultiPlayerStatistics(), true);
                multiPlayerUIManager.endGame();
            } else {
                // Single player mode
                if (isWin) {
                    statisticsManager.incrementSinglePlayerWins();
                    statisticsManager.addSinglePlayerGuessesForWin(attempts);
                    statisticsManager.addScore(score);
                    showAlert("Congratulations! You've guessed the word in " + attempts + " attempts. Score: " + score);
                } else {
                    statisticsManager.incrementSinglePlayerLosses();
                    statisticsManager.addScore(0);
                    showAlert("Game Over! The word was: " + secretWord + ". Score: 0");
                }
                updateStats(statisticsManager.getSinglePlayerStatistics(), true);
                singlePlayerUIManager.endGame();
            }
        }
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
            // Update the UI after switching roles
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

    public void checkGameState() {
        System.out.println("Current game state:");
        System.out.println("Secret word: " + secretWord);
        System.out.println("Is multiplayer mode: " + isMultiplayerMode);
        System.out.println("Current row: " + currentRow);
        System.out.println("Current col: " + currentCol);
        System.out.println("Game completed: " + gameCompleted);
        System.out.println("Game over: " + gameOver);
        System.out.println("SinglePlayerUIManager is " + (singlePlayerUIManager != null ? "set" : "null"));
        System.out.println("MultiPlayerUIManager is " + (multiPlayerUIManager != null ? "set" : "null"));
    }
}