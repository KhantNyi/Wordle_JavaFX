package com.example.demo;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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

        VBox tutorialLayout = new VBox(20);
        tutorialLayout.setAlignment(Pos.TOP_LEFT);
        tutorialLayout.setPadding(new Insets(30));

        Label titleLabel = new Label("How To Play");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label subtitleLabel = new Label("Guess the Wordle in 6 tries.");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        VBox instructionsBox = createInstructionsBox();
        VBox examplesBox = createExamplesBox();
        VBox scoringSystemBox = createScoringSystemBox();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button backButton = new Button("Back to Main Menu");
        backButton.setStyle(
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

        backButton.setOnAction(e -> returnToMainMenu());

        tutorialLayout.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                instructionsBox,
                examplesBox,
                scoringSystemBox,
                spacer,
                backButton
        );

        Scene tutorialScene = new Scene(tutorialLayout, 500, 800);
        primaryStage.setScene(tutorialScene);
    }

    private VBox createInstructionsBox() {
        VBox instructionsBox = new VBox(10);
        instructionsBox.getChildren().addAll(
                createBulletPoint("Each guess must be a valid 5-letter word."),
                createBulletPoint("The color of the tiles will change to show how close your guess was to the word.")
        );
        return instructionsBox;
    }

    private HBox createBulletPoint(String text) {
        HBox bulletPoint = new HBox(10);
        bulletPoint.setAlignment(Pos.CENTER_LEFT);
        Label bullet = new Label("•");
        Label content = new Label(text);
        content.setWrapText(true);
        bulletPoint.getChildren().addAll(bullet, content);
        return bulletPoint;
    }

    private VBox createExamplesBox() {
        VBox examplesBox = new VBox(20);
        Label examplesLabel = new Label("Examples");
        examplesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        examplesBox.getChildren().addAll(
                examplesLabel,
                createExampleRow("W", "O", "R", "D", "Y", "W is in the word and in the correct spot.", Color.GREEN),
                createExampleRow("L", "I", "G", "H", "T", "I is in the word but in the wrong spot.", Color.GOLDENROD),
                createExampleRow("R", "O", "G", "U", "E", "U is not in the word in any spot.", Color.GRAY)
        );
        return examplesBox;
    }

    private VBox createScoringSystemBox() {
        VBox scoringSystemBox = new VBox(5);
        scoringSystemBox.setPadding(new Insets(10, 0, 0, 0));
        Label scoringTitle = new Label("Scoring System");
        scoringTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label score1 = new Label("1st try correct = 100 points");
        Label score2 = new Label("2nd try correct = 90 points");
        Label score3 = new Label("3rd try correct = 80 points");
        Label score4 = new Label("4th try correct = 70 points");
        Label score5 = new Label("5th try correct = 60 points");
        Label score6 = new Label("6th try correct = 50 points");

        score1.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        score2.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        score3.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        score4.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        score5.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        score6.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

        scoringSystemBox.getChildren().addAll(scoringTitle, score1, score2, score3, score4, score5, score6);
        return scoringSystemBox;
    }

    private VBox createExampleRow(String l1, String l2, String l3, String l4, String l5, String explanation, Color highlightColor) {
        HBox letterBox = new HBox(5);
        letterBox.setAlignment(Pos.CENTER_LEFT);
        letterBox.getChildren().addAll(
                createLetterBox(l1, explanation.contains(l1) && highlightColor == Color.GREEN ? highlightColor : Color.WHITE),
                createLetterBox(l2, explanation.contains(l2) && highlightColor == Color.GOLDENROD ? highlightColor : Color.WHITE),
                createLetterBox(l3, explanation.contains(l3) && highlightColor == Color.GOLDENROD ? highlightColor : Color.WHITE),
                createLetterBox(l4, explanation.contains(l4) && highlightColor == Color.GRAY ? highlightColor : Color.WHITE),
                createLetterBox(l5, explanation.contains(l5) && highlightColor == Color.GRAY ? highlightColor : Color.WHITE)
        );

        Label explanationLabel = new Label(explanation);
        explanationLabel.setWrapText(true);

        VBox exampleRow = new VBox(5);
        exampleRow.getChildren().addAll(letterBox, explanationLabel);
        return exampleRow;
    }

    private Label createLetterBox(String letter, Color bgColor) {
        Label letterLabel = new Label(letter);
        letterLabel.setAlignment(Pos.CENTER);
        letterLabel.setMinSize(40, 40);
        letterLabel.setMaxSize(40, 40);
        letterLabel.setStyle(
                "-fx-border-color: #d3d6da; " +
                        "-fx-border-width: 2px; " +
                        "-fx-background-color: " + toRGBCode(bgColor) + ";" +
                        "-fx-text-fill: " + (bgColor == Color.WHITE ? "black" : "white") + ";" +
                        "-fx-font-weight: bold;"
        );

        // Add the easter egg to the "U" in "ROGUE"
        if (letter.equals("U")) {
            letterLabel.setOnMouseClicked(e -> openEasterEgg());
            letterLabel.setStyle(letterLabel.getStyle() + "-fx-cursor: hand;");
        }

        return letterLabel;
    }

    private void openEasterEgg() {
        HostServices hostServices = mainApplication.getHostServices();
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

    private void returnToMainMenu() {
        mainApplication.returnToMainScreen();
    }
}