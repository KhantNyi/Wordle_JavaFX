package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MultiPlayerUIManager extends UIManager {

    private static final int MULTIPLAYER_WORD_LENGTH = 5;
    private MultiplayerRoleManager roleManager;
    private Label playerTurnLabel;
    private GridPane wordSetterGrid;
    private VBox wordGuesserBox;
    private int currentWordSetterCol;
    public Stage primaryStage;
    private int currentGuessRow = 0;
    private int currentGuessCol = 0;

    public MultiPlayerUIManager(StatisticsManager statisticsManager, WordValidator wordValidator, Stage primaryStage) {
        super(statisticsManager, wordValidator);
        this.primaryStage = primaryStage;
    }

    @Override
    public void setWordleGame(WordleGame wordleGame) {
        super.setWordleGame(wordleGame);
        this.roleManager = new MultiplayerRoleManager(wordleGame, this);
        wordleGame.setMultiplayerRoleManager(this.roleManager);
    }

    @Override
    protected VBox createBaseLayout(String title) {
        VBox layout = super.createBaseLayout(title);

        playerTurnLabel = createPlayerTurnLabel();
        wordSetterGrid = createWordSetterGrid();
        wordGuesserBox = createWordGuesserBox();
        wordGuesserBox.setVisible(false);

        VBox headerPane = new VBox(20);
        headerPane.setAlignment(Pos.CENTER);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        headerPane.getChildren().addAll(titleLabel, playerTurnLabel);

        layout.getChildren().clear();
        layout.getChildren().addAll(headerPane, wordSetterGrid, wordGuesserBox);

        setupKeyboardActions();

        layout.setPrefSize(1200, 1000);

        return layout;
    }

    public void initializeMultiplayerGame() {
        roleManager.initializeGame();
    }

    public void updateUIForNewGame(boolean isPlayer1SettingWord) {
        playerTurnLabel.setText(isPlayer1SettingWord ? "Player 1's Turn: Set a 5-letter word" : "Player 2's Turn: Set a 5-letter word");
        wordSetterGrid.setVisible(true);
        wordGuesserBox.setVisible(false);
        currentWordSetterCol = 0;
        clearWordSetterGrid();
        setupKeyboardActions();
    }

    public void updateUIForRoleSwitch(boolean isPlayer1SettingWord) {
        updateUIForNewGame(isPlayer1SettingWord);
        primaryStage.setFullScreen(false);
    }

    public void transitionToGuessingPhase(boolean isPlayer1Guessing) {
        playerTurnLabel.setText(isPlayer1Guessing ? "Player 1's Turn: Guess the 5-letter word" : "Player 2's Turn: Guess the 5-letter word");
        wordSetterGrid.setVisible(false);
        wordGuesserBox.setVisible(true);
        resetGuessingState();
        setupKeyboardActions();
    }

    private Label createPlayerTurnLabel() {
        Label label = new Label("Player 1's Turn: Set a 5-letter word");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        label.setTextFill(Color.BLACK);
        return label;
    }

    private GridPane createWordSetterGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < MULTIPLAYER_WORD_LENGTH; i++) {
            Label cell = createWordSetterCell();
            grid.add(cell, i, 0);
        }

        return grid;
    }

    private Label createWordSetterCell() {
        Label cell = new Label("");
        cell.setMinSize(60, 60);
        cell.setStyle("-fx-border-color: #d3d6da; -fx-border-width: 2px; -fx-background-color: #ffffff;");
        cell.setAlignment(Pos.CENTER);
        cell.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        return cell;
    }

    private VBox createWordGuesserBox() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        initializeGridPane();
        initializeKeyboard();

        box.getChildren().addAll(gridPane, keyboardLayout);
        return box;
    }

    private void setupKeyboardActions() {
        root.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey()) {
                handleKeyPress(event.getText().toUpperCase());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleBackspace();
            } else if (event.getCode() == KeyCode.ENTER) {
                handleEnter();
            }
        });
        root.requestFocus();
    }

    private void handleKeyPress(String letter) {
        if (wordSetterGrid.isVisible()) {
            if (currentWordSetterCol < MULTIPLAYER_WORD_LENGTH) {
                updateWordSetterGrid(currentWordSetterCol, letter);
                currentWordSetterCol++;
            }
        } else {
            if (currentGuessCol < MULTIPLAYER_WORD_LENGTH) {
                updateGrid(currentGuessRow, currentGuessCol, letter);
                currentGuessCol++;
                System.out.println("Updated grid: row " + currentGuessRow + ", col " + currentGuessCol + ", letter " + letter);
            }
        }
    }

    private void handleBackspace() {
        if (wordSetterGrid.isVisible()) {
            if (currentWordSetterCol > 0) {
                currentWordSetterCol--;
                updateWordSetterGrid(currentWordSetterCol, "");
            }
        } else {
            if (currentGuessCol > 0) {
                currentGuessCol--;
                updateGrid(currentGuessRow, currentGuessCol, "");
                System.out.println("Backspace: row " + currentGuessRow + ", col " + currentGuessCol);
            }
        }
    }

    private void handleEnter() {
        if (wordSetterGrid.isVisible()) {
            handleWordSubmission();
        } else {
            if (currentGuessCol == MULTIPLAYER_WORD_LENGTH) {
                String guess = getGuessFromGrid();
                System.out.println("Submitting guess: " + guess);

                // If guess is invalid, reset the column index without moving to the next row
                if (!wordValidator.isValidWord(guess, MULTIPLAYER_WORD_LENGTH)) {
                    showAlert("Invalid word! Please enter a valid " + MULTIPLAYER_WORD_LENGTH + "-letter word.");
                    for (int i = 0; i < MULTIPLAYER_WORD_LENGTH; i++) {
                        updateGrid(currentGuessRow, i, ""); // Clear the row in the UI
                    }
                    currentGuessCol = 0; // Reset column index
                    return; // Do not process the guess or move to next row
                }
                wordleGame.processGuess(guess);

                currentGuessRow++;
                currentGuessCol = 0;
            } else {
                showAlert("Please enter a complete word before submitting.");
            }
        }
    }

    private void handleWordSubmission() {
        String secretWord = getWordFromSetterGrid();
        if (secretWord.length() == MULTIPLAYER_WORD_LENGTH && wordValidator.isValidWord(secretWord, MULTIPLAYER_WORD_LENGTH)) {
            roleManager.handleWordSet(secretWord);
        } else {
            showAlert("Invalid word! Please enter a valid " + MULTIPLAYER_WORD_LENGTH + "-letter word.");
            clearWordSetterGrid();
            currentWordSetterCol = 0;
        }
    }

    private void resetGuessingState() {
        currentGuessRow = 0;
        currentGuessCol = 0;
        clearGuessingGrid();
    }

    private void updateWordSetterGrid(int col, String letter) {
        Label cell = (Label) wordSetterGrid.getChildren().get(col);
        cell.setText(letter);
    }

    private String getWordFromSetterGrid() {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < MULTIPLAYER_WORD_LENGTH; i++) {
            Label cell = (Label) wordSetterGrid.getChildren().get(i);
            word.append(cell.getText());
        }
        return word.toString();
    }

    private String getGuessFromGrid() {
        StringBuilder guess = new StringBuilder();
        for (int i = 0; i < MULTIPLAYER_WORD_LENGTH; i++) {
            Label cell = (Label) gridPane.getChildren().get(currentGuessRow * MULTIPLAYER_WORD_LENGTH + i);
            guess.append(cell.getText());
        }
        return guess.toString();
    }

    private void clearWordSetterGrid() {
        for (int i = 0; i < MULTIPLAYER_WORD_LENGTH; i++) {
            updateWordSetterGrid(i, "");
        }
    }

    public void resetForNewRound(boolean isPlayer1SettingWord) {
        root = createBaseLayout("WORDLE WITH FRIENDS");
        updateUIForNewGame(isPlayer1SettingWord);
        resetGuessingState();

        Scene scene = new Scene(root, 1200, 1000);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(false);

        setupKeyboardActions();
    }

    private void clearGuessingGrid() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < MULTIPLAYER_WORD_LENGTH; j++) {
                updateGrid(i, j, "");
            }
        }
    }

    @Override
    public void endGame() {
        super.endGame();
        showEndGameOptions();
    }

    public void showEndGameOptions() {
        VBox optionsBox = new VBox(20);
        optionsBox.setAlignment(Pos.CENTER);
        optionsBox.setStyle("-fx-padding: 20; -fx-background-color: #f5f5f5;");

        Label promptLabel = new Label("Round Over! Would you like to switch roles or return to the main menu?");
        promptLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        promptLabel.setTextAlignment(TextAlignment.CENTER);
        promptLabel.setWrapText(true);

        Button switchRolesButton = createStyledButton("Switch Roles");
        Button mainMenuButton = createStyledButton("Main Menu");

        switchRolesButton.setOnAction(e -> wordleGame.switchRolesMultiplayer());
        mainMenuButton.setOnAction(e -> wordleGame.returnToMainScreen());

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(switchRolesButton, mainMenuButton);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        optionsBox.getChildren().addAll(promptLabel, buttonBox);

        Scene endGameScene = new Scene(optionsBox, 1200, 1000);
        primaryStage.setScene(endGameScene);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #0095ff; " +
                        "-fx-border-color: transparent; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 3px; " +
                        "-fx-background-radius: 3px; " +
                        "-fx-effect: innershadow(one-pass-box, rgba(255, 255, 255, 0.4), 0, 0, 0, 1); " +
                        "-fx-text-fill: #fff; " +
                        "-fx-font-family: -apple-system, system-ui, 'Segoe UI', 'Liberation Sans', sans-serif; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: 400; " +
                        "-fx-padding: 8px 0.8em; " +
                        "-fx-cursor: hand; " +
                        "-fx-alignment: center; "
        );
        return button;
    }

    @Override
    protected void initializeGridPane() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < MULTIPLAYER_WORD_LENGTH; j++) {
                Label cell = new Label("");
                cell.setMinSize(60, 60);
                cell.setStyle("-fx-border-color: #d3d6da; -fx-border-width: 2px; -fx-background-color: #ffffff;");
                cell.setAlignment(Pos.CENTER);
                cell.setFont(new Font("Arial Black", 20));
                gridPane.add(cell, j, i);
            }
        }
    }

    @Override
    public void updateGrid(int row, int col, String letter) {
        Label cell = (Label) gridPane.getChildren().get(row * MULTIPLAYER_WORD_LENGTH + col);
        cell.setText(letter);
    }

    @Override
    public void updateGuessFeedback(int row, int col, char guessChar, char correctChar) {
        Label cell = (Label) gridPane.getChildren().get(row * MULTIPLAYER_WORD_LENGTH + col);
        String style = "-fx-border-color: #d3d6da; -fx-border-width: 2px;";

        if (Character.toUpperCase(guessChar) == Character.toUpperCase(correctChar)) {
            style += " -fx-background-color: #538d4e;"; // Green for correct position
        } else if (wordleGame.getSecretWord().indexOf(guessChar) >= 0) {
            style += " -fx-background-color: #b59f3b;"; // Yellow for correct letter, wrong position
        } else {
            style += " -fx-background-color: #3a3a3c;"; // Gray for incorrect letter
        }

        cell.setStyle(style);
        cell.setTextFill(Color.WHITE);
    }
}