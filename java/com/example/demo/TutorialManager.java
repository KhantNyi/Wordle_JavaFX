package com.example.demo;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

public class TutorialManager {
    private Stage primaryStage;
    private Scene previousScene;
    private Main mainApplication;

    public TutorialManager(Stage primaryStage, Main mainApplication) {
        this.primaryStage = primaryStage;
        this.mainApplication = mainApplication;
    }

    public void showTutorial() {
        previousScene = primaryStage.getScene();

        ScrollPane scrollPane = new ScrollPane();
        VBox tutorialLayout = new VBox(30);
        tutorialLayout.setAlignment(Pos.TOP_CENTER);
        tutorialLayout.setPadding(new Insets(40));
        tutorialLayout.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = createStyledLabel("How To Play Wordle", 36, FontWeight.BOLD, "#2C3E50");

        Label subtitleLabel = createStyledLabel("Guess the Wordle in 6 tries.", 24, FontWeight.NORMAL, "#34495E");

        VBox instructionsBox = createInstructionsBox();
        HBox examplesBox = createExamplesBox();
        VBox scoringSystemBox = createScoringSystemBox();

        Button backButton = createStyledButton("Back to Main Menu");
        backButton.setOnAction(e -> returnToMainMenu());

        tutorialLayout.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                instructionsBox,
                examplesBox,
                scoringSystemBox,
                backButton
        );

        scrollPane.setContent(tutorialLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f0f0f0;");

        Scene tutorialScene = new Scene(scrollPane, 1200, 1000);
        primaryStage.setScene(tutorialScene);
        primaryStage.setFullScreen(false);
    }

    private VBox createInstructionsBox() {
        VBox instructionsBox = new VBox(15);
        instructionsBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        instructionsBox.setEffect(new DropShadow(5, Color.LIGHTGRAY));

        Label instructionsTitle = createStyledLabel("Instructions", 22, FontWeight.BOLD, "#2980B9");

        instructionsBox.getChildren().addAll(
                instructionsTitle,
                createBulletPoint("Each guess must be a valid word (5 or 6 letters)."),
                createBulletPoint("The color of the tiles will change to show how close your guess was to the word."),
                createBulletPoint("You have 6 attempts to guess the word correctly.")
        );
        return instructionsBox;
    }

    private HBox createBulletPoint(String text) {
        HBox bulletPoint = new HBox(10);
        bulletPoint.setAlignment(Pos.CENTER_LEFT);
        Label bullet = createStyledLabel("•", 18, FontWeight.BOLD, "#E74C3C");
        Label content = createStyledLabel(text, 16, FontWeight.NORMAL, "#2C3E50");
        content.setWrapText(true);
        bulletPoint.getChildren().addAll(bullet, content);
        return bulletPoint;
    }

    private HBox createExamplesBox() {
        HBox examplesBox = new HBox(20);
        examplesBox.setAlignment(Pos.CENTER);
        examplesBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        examplesBox.setEffect(new DropShadow(5, Color.LIGHTGRAY));

        VBox fiveLetterExamples = createExampleColumn("5-Letter Words",
                new String[]{"W", "O", "R", "D", "S"},
                new String[]{"P", "L", "A", "C", "E"},
                new String[]{"V", "A", "G", "U", "E"});

        VBox sixLetterExamples = createExampleColumn("6-Letter Words",
                new String[]{"S", "E", "C", "R", "E", "T"},
                new String[]{"W", "O", "N", "D", "E", "R"},
                new String[]{"F", "A", "M", "O", "U", "S"});

        examplesBox.getChildren().addAll(fiveLetterExamples, sixLetterExamples);
        return examplesBox;
    }

    private VBox createExampleColumn(String title, String[] word1, String[] word2, String[] word3) {
        VBox column = new VBox(15);
        column.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = createStyledLabel(title, 20, FontWeight.BOLD, "#2980B9");

        VBox examples = new VBox(10);
        examples.getChildren().addAll(
                createExampleRow(word1[0], word1[1], word1[2], word1[3], word1[4], word1.length > 5 ? word1[5] : null,
                        word1[0] + " is in the word and in the correct spot.", Color.GREEN),
                createExampleRow(word2[0], word2[1], word2[2], word2[3], word2[4], word2.length > 5 ? word2[5] : null,
                        word2[1] + " is in the word but in the wrong spot.", Color.GOLD),
                createExampleRow(word3[0], word3[1], word3[2], word3[3], word3[4], word3.length > 5 ? word3[5] : null,
                        word3[4] + " is not in the word in any spot.", Color.GRAY)
        );

        column.getChildren().addAll(titleLabel, examples);
        return column;
    }

    private VBox createScoringSystemBox() {
        VBox scoringSystemBox = new VBox(15);
        scoringSystemBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");
        scoringSystemBox.setEffect(new DropShadow(5, Color.LIGHTGRAY));

        Label scoringTitle = createStyledLabel("Scoring System", 22, FontWeight.BOLD, "#2980B9");

        HBox scoringColumns = new HBox(30);
        scoringColumns.setAlignment(Pos.CENTER);

        VBox attemptsColumn = new VBox(5);
        VBox pointsColumn = new VBox(5);

        String[] attempts = {"1st try", "2nd try", "3rd try", "4th try", "5th try", "6th try"};
        String[] points = {"100 points", "90 points", "80 points", "70 points", "60 points", "50 points"};

        for (int i = 0; i < attempts.length; i++) {
            attemptsColumn.getChildren().add(createStyledLabel(attempts[i], 16, FontWeight.NORMAL, "#2C3E50"));
            pointsColumn.getChildren().add(createStyledLabel(points[i], 16, FontWeight.BOLD, "#27AE60"));
        }

        scoringColumns.getChildren().addAll(attemptsColumn, pointsColumn);
        scoringSystemBox.getChildren().addAll(scoringTitle, scoringColumns);
        return scoringSystemBox;
    }

    private VBox createExampleRow(String l1, String l2, String l3, String l4, String l5, String l6, String explanation, Color highlightColor) {
        HBox letterBox = new HBox(5);
        letterBox.setAlignment(Pos.CENTER_LEFT);
        letterBox.getChildren().addAll(
                createLetterBox(l1, explanation.contains(l1) ? highlightColor : Color.WHITE),
                createLetterBox(l2, explanation.contains(l2) ? highlightColor : Color.WHITE),
                createLetterBox(l3, explanation.contains(l3) ? highlightColor : Color.WHITE),
                createLetterBox(l4, explanation.contains(l4) ? highlightColor : Color.WHITE),
                createLetterBox(l5, explanation.contains(l5) ? highlightColor : Color.WHITE)
        );
        if (l6 != null) {
            letterBox.getChildren().add(createLetterBox(l6, explanation.contains(l6) ? highlightColor : Color.WHITE));
        }

        Label explanationLabel = createStyledLabel(explanation, 14, FontWeight.NORMAL, "#2C3E50");
        explanationLabel.setWrapText(true);

        VBox exampleRow = new VBox(5);
        exampleRow.getChildren().addAll(letterBox, explanationLabel);
        return exampleRow;
    }

    private StackPane createLetterBox(String letter, Color bgColor) {
        StackPane letterBox = new StackPane();
        letterBox.setMinSize(40, 40);
        letterBox.setMaxSize(40, 40);
        letterBox.setStyle(
                "-fx-border-color: #d3d6da; " +
                        "-fx-border-width: 2px; " +
                        "-fx-background-color: " + toRGBCode(bgColor) + ";" +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5;"
        );

        Label letterLabel = createStyledLabel(letter, 18, FontWeight.BOLD, bgColor == Color.WHITE ? "#2C3E50" : "white");
        letterBox.getChildren().add(letterLabel);

        // Add the easter egg to the "U" in "VAGUE"
        if (letter.equals("U")) {
            letterBox.setOnMouseClicked(e -> openEasterEgg());
            letterBox.setStyle(letterBox.getStyle() + "-fx-cursor: hand;");
        }

        return letterBox;
    }

    private void openEasterEgg() {
        HostServices hostServices = mainApplication.getAppHostServices();
        if (hostServices != null) {
            hostServices.showDocument("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        } else {
            System.err.println("HostServices is null. Unable to open URL.");
        }
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private Label createStyledLabel(String text, int fontSize, FontWeight fontWeight, String colorCode) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", fontWeight, fontSize));
        label.setTextFill(Color.web(colorCode));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #3498DB; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5;"
        );
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle() + "-fx-background-color: #2980B9;"));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle() + "-fx-background-color: #3498DB;"));
        return button;
    }

    private void returnToMainMenu() {
        mainApplication.returnToMainScreen();
    }
}