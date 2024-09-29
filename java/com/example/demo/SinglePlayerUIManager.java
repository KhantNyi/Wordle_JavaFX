package com.example.demo;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class SinglePlayerUIManager extends UIManager {

    public SinglePlayerUIManager(StatisticsManager statisticsManager, WordValidator wordValidator) {
        super(statisticsManager, wordValidator);
    }

    public VBox createSinglePlayerGameLayout() {
        VBox layout = createBaseLayout("WORDLE");

        initializeGridPane();
        initializeKeyboard();

        layout.getChildren().addAll(gridPane, keyboardLayout, statsLabel);

        setupKeyboardActions();

        return layout;
    }

    private void setupKeyboardActions() {
        root.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey()) {
                wordleGame.handleKeyPress(event.getText().toUpperCase());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                wordleGame.handleBackspace();
            } else if (event.getCode() == KeyCode.ENTER) {
                wordleGame.processGuess();
            }
        });
        root.requestFocus();
    }

    @Override
    public void endGame() {
        super.endGame();
        showEndGameOptions();
    }

    private void showEndGameOptions() {
        VBox optionsBox = new VBox(20);  // Increased spacing between elements
        optionsBox.setAlignment(Pos.CENTER);

        Label promptLabel = new Label("Would you like to play again or return to the main menu?");
        promptLabel.setFont(new Font("Arial", 18));
        promptLabel.setWrapText(true);  // Allow text wrapping
        promptLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button playAgainButton = new Button("Play Again");
        Button mainMenuButton = new Button("Main Menu");

        // Apply the same style to both buttons
        String buttonStyle =
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
                        "-fx-alignment: center; ";

        playAgainButton.setStyle(buttonStyle);
        mainMenuButton.setStyle(buttonStyle);

        playAgainButton.setOnAction(e -> playAgain());
        mainMenuButton.setOnAction(e -> returnToMainMenu());

        // Create an HBox for the buttons
        HBox buttonBox = new HBox(20);  // 20 pixels spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playAgainButton, mainMenuButton);

        // Add padding to the top of the button box for space below the text
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        optionsBox.getChildren().addAll(promptLabel, buttonBox);

        root.getChildren().clear();
        root.getChildren().add(optionsBox);
    }

    private void playAgain() {
        resetUI();
        wordleGame.startNewSinglePlayerGame();
        setupKeyboardActions();
        root.requestFocus();
    }

    private void returnToMainMenu() {
        wordleGame.returnToMainScreen();
    }

    @Override
    protected void resetUI() {
        super.resetUI(); // Call the parent class's resetUI method

        // Clear existing children
        root.getChildren().clear();

        // Recreate and add the components
        Label titleLabel = new Label("WORDLE");
        titleLabel.setFont(new Font("Arial Black", 36));
        root.getChildren().addAll(titleLabel, gridPane, keyboardLayout, statsLabel);

        // Ensure the statsLabel is properly managed
        statsLabel.setVisible(true);
        statsLabel.setManaged(true);
    }
}