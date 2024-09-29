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

    private MultiplayerRoleManager roleManager;

    private Label playerTurnLabel;
    private GridPane wordSetterGrid;
    private VBox wordGuesserBox;
    private int currentWordSetterCol;
    private Stage primaryStage;

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

    public VBox createMultiPlayerGameLayout() {
        VBox layout = createBaseLayout("WORDLE WITH FRIENDS");

        playerTurnLabel = createPlayerTurnLabel();
        wordSetterGrid = createWordSetterGrid();
        wordGuesserBox = createWordGuesserBox();
        wordGuesserBox.setVisible(false);

        layout.getChildren().addAll(playerTurnLabel, wordSetterGrid, wordGuesserBox);

        setupKeyboardActions();

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
    }

    public void transitionToGuessingPhase(boolean isPlayer1Guessing) {
        playerTurnLabel.setText(isPlayer1Guessing ? "Player 1's Turn: Guess the 5-letter word" : "Player 2's Turn: Guess the 5-letter word");
        wordSetterGrid.setVisible(false);
        wordGuesserBox.setVisible(true);
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

        for (int i = 0; i < 5; i++) {
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
            if (currentWordSetterCol < 5) {
                updateWordSetterGrid(currentWordSetterCol, letter);
                currentWordSetterCol++;
            }
        } else {
            wordleGame.handleKeyPress(letter);
        }
    }

    private void handleBackspace() {
        if (wordSetterGrid.isVisible()) {
            if (currentWordSetterCol > 0) {
                currentWordSetterCol--;
                updateWordSetterGrid(currentWordSetterCol, "");
            }
        } else {
            wordleGame.handleBackspace();
        }
    }

    private void handleEnter() {
        if (wordSetterGrid.isVisible()) {
            handleWordSubmission();
        } else {
            wordleGame.processGuess();
        }
    }

    private void handleWordSubmission() {
        String secretWord = getWordFromSetterGrid();
        if (secretWord.length() == 5 && wordValidator.isValidWord(secretWord)) {
            roleManager.handleWordSet(secretWord);
        } else {
            showAlert("Invalid word! Please enter a valid 5-letter word.");
            clearWordSetterGrid();
            currentWordSetterCol = 0;
        }
    }

    private void updateWordSetterGrid(int col, String letter) {
        Label cell = (Label) wordSetterGrid.getChildren().get(col);
        cell.setText(letter);
    }

    private String getWordFromSetterGrid() {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            Label cell = (Label) wordSetterGrid.getChildren().get(i);
            word.append(cell.getText());
        }
        return word.toString();
    }

    private void clearWordSetterGrid() {
        for (int i = 0; i < 5; i++) {
            updateWordSetterGrid(i, "");
        }
    }

    public void resetForNewRound(boolean isPlayer1SettingWord) {
        resetUI();
        updateUIForNewGame(isPlayer1SettingWord);
        setupKeyboardActions();
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

        Scene endGameScene = new Scene(optionsBox, 500, 800);
        primaryStage.setScene(endGameScene);
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
    protected void resetUI() {
        super.resetUI();

        VBox newRoot = new VBox(20);
        newRoot.setAlignment(Pos.CENTER);
        newRoot.setStyle("-fx-padding: 20; -fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("WORDLE WITH FRIENDS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.BLACK);

        playerTurnLabel = createPlayerTurnLabel();
        wordSetterGrid = createWordSetterGrid();
        wordGuesserBox = createWordGuesserBox();
        wordGuesserBox.setVisible(false);

        newRoot.getChildren().addAll(titleLabel, playerTurnLabel, wordSetterGrid, wordGuesserBox);

        // Remove statsLabel from the UI
        if (newRoot.getChildren().contains(statsLabel)) {
            newRoot.getChildren().remove(statsLabel);
        }

        this.root = newRoot;

        Scene scene = new Scene(root, 500, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        setupKeyboardActions();
    }
}