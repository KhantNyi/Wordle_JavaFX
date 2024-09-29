package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class UIManager {
    protected StatisticsManager statisticsManager;
    protected WordleGame wordleGame;
    protected WordValidator wordValidator;
    protected GridPane gridPane;
    protected GridPane keyboardLayout;
    protected Label statsLabel;
    protected VBox root;
    protected final int wordLength = 5;

    public UIManager(StatisticsManager statisticsManager, WordValidator wordValidator) {
        this.statisticsManager = statisticsManager;
        this.wordValidator = wordValidator;
    }

    public void setWordleGame(WordleGame wordleGame) {
        this.wordleGame = wordleGame;
    }

    protected VBox createBaseLayout(String title) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20; -fx-background-color: #ffffff;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Arial Black", 36));
        titleLabel.setTextFill(Color.BLACK);

        statsLabel = new Label();
        statsLabel.setFont(new Font("Arial", 16));

        root.getChildren().addAll(titleLabel);

        return root;
    }

    protected void initializeGridPane() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < wordLength; j++) {
                Label cell = new Label("");
                cell.setMinSize(60, 60);
                cell.setStyle("-fx-border-color: #d3d6da; -fx-border-width: 2px; -fx-background-color: #ffffff;");
                cell.setAlignment(Pos.CENTER);
                cell.setFont(new Font("Arial Black", 20));
                gridPane.add(cell, j, i);
            }
        }
    }

    protected void initializeKeyboard() {
        keyboardLayout = new GridPane();
        keyboardLayout.setAlignment(Pos.CENTER);
        keyboardLayout.setHgap(5);
        keyboardLayout.setVgap(10);

        String[] rows = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        for (int i = 0; i < rows.length; i++) {
            HBox rowBox = new HBox(5);
            rowBox.setAlignment(Pos.CENTER);

            for (int j = 0; j < rows[i].length(); j++) {
                String letter = String.valueOf(rows[i].charAt(j));
                Button key = new Button(letter);
                key.setId(letter);
                key.setMinSize(50, 60);
                key.setStyle("-fx-background-color: #d3d6da; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 16px;");

                rowBox.getChildren().add(key);
            }

            keyboardLayout.add(rowBox, 0, i);
        }
    }

    public void updateGrid(int row, int col, String letter) {
        Label cell = (Label) gridPane.getChildren().get(row * wordLength + col);
        cell.setText(letter);
    }

    public void updateGuessFeedback(int row, int col, char guessChar, char correctChar) {
        Label cell = (Label) gridPane.getChildren().get(row * wordLength + col);
        String style = "-fx-border-color: #d3d6da; -fx-border-width: 2px;";

        if (guessChar == correctChar) {
            style += " -fx-background-color: #538d4e;"; // Green for correct position
        } else if (wordleGame.getSecretWord().indexOf(guessChar) >= 0) {
            style += " -fx-background-color: #b59f3b;"; // Yellow for correct letter, wrong position
        } else {
            style += " -fx-background-color: #3a3a3c;"; // Gray for incorrect letter
        }

        cell.setStyle(style);
        cell.setTextFill(Color.web("#FFFFFF"));
    }

    public void showAlert(String message) {
        // This method can remain as a fallback, but we'll primarily use the fancy alert in WordleGame
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wordle");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateKeyboardFeedback(String guess, String secretWord) {
        for (int i = 0; i < guess.length(); i++) {
            char letter = guess.charAt(i);
            Button key = findKeyButton(letter);
            if (key != null) {
                String style = "-fx-text-fill: white;";
                if (secretWord.charAt(i) == letter) {
                    style += "-fx-background-color: #538d4e;"; // Green
                } else if (secretWord.contains(String.valueOf(letter))) {
                    style += "-fx-background-color: #b59f3b;"; // Yellow
                } else {
                    style += "-fx-background-color: #3a3a3c;"; // Gray
                }
                key.setStyle(style);
            }
        }
    }

    private Button findKeyButton(char letter) {
        for (int i = 0; i < keyboardLayout.getChildren().size(); i++) {
            HBox row = (HBox) keyboardLayout.getChildren().get(i);
            for (javafx.scene.Node node : row.getChildren()) {
                if (node instanceof Button) {
                    Button key = (Button) node;
                    if (key.getText().charAt(0) == letter) {
                        return key;
                    }
                }
            }
        }
        return null;
    }

    public void updateStats(String stats, boolean isVisible) {
        if (statsLabel != null) {
            statsLabel.setText(stats);
            statsLabel.setVisible(isVisible);
            statsLabel.setManaged(isVisible);
            System.out.println("Updated stats: " + stats);
        }
    }

    public void endGame() {
        gridPane.setDisable(true);
        keyboardLayout.setDisable(true);
        showAlert("Game Over! Press OK to continue.");

        statsLabel.setVisible(true);
        statsLabel.setManaged(true);
    }

    protected void resetUI() {
        gridPane.getChildren().clear();
        initializeGridPane();

        keyboardLayout.getChildren().clear();
        initializeKeyboard();

        statsLabel.setVisible(true);
        statsLabel.setManaged(true);

        root.getChildren().clear();
        Label titleLabel = new Label("WORDLE");
        titleLabel.setFont(new Font("Arial Black", 36));
        titleLabel.setTextFill(Color.BLACK);
        root.getChildren().addAll(titleLabel, gridPane, keyboardLayout, statsLabel);
    }
}