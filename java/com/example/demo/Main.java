package com.example.demo;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.paint.Color;

public class Main extends Application {
    private Stage primaryStage;
    private SinglePlayerUIManager singlePlayerUIManager;
    private MultiPlayerUIManager multiPlayerUIManager;
    private WordleGame wordleGame;
    private StatisticsManager statisticsManager;
    private WordValidator wordValidator;
    private TutorialManager tutorialManager;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Wordle");

        statisticsManager = new StatisticsManager();
        wordValidator = new WordValidator();
        wordleGame = new WordleGame(statisticsManager, wordValidator);
        wordleGame.setMainApplication(this);
        tutorialManager = new TutorialManager(primaryStage, this);

        showMainScreen();
    }

    private void showMainScreen() {
        VBox modeSelectionLayout = createModeSelectionLayout();
        Scene modeSelectionScene = new Scene(modeSelectionLayout, 1200, 1000);
        primaryStage.setScene(modeSelectionScene);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    private VBox createModeSelectionLayout() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("Wordle");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setPadding(new Insets(0, 0, 20, 0));

        HBox singlePlayer5LetterBox = createModeBox("5-Letter Singleplayer", "Classic Wordle experience",
                "M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0z",
                Color.web("#FF9800"), e -> startSinglePlayerGame(5));

        HBox singlePlayer6LetterBox = createModeBox("6-Letter Singleplayer", "Extended Wordle challenge",
                "M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0z",
                Color.web("#4CAF50"), e -> startSinglePlayerGame(6));

        HBox multiPlayerBox = createModeBox("Multiplayer", "New 2 player mode",
                "M8 0 L16 16 L0 16 Z",
                Color.web("#2196F3"), e -> startMultiPlayerGame());

        HBox tutorialBox = createModeBox("How to Play", "Learn the rules",
                "M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm-1-9h2v4H7V7zm0-3h2v2H7V4z",
                Color.web("#795548"), e -> showTutorial());

        HBox singlePlayerScoreboardBox = createModeBox("SinglePlayer Scoreboard", "View singleplayer statistics",
                "M0 0h16v16H0z",
                Color.web("#4CAF50"), e -> showSinglePlayerScoreboard());

        HBox multiPlayerScoreboardBox = createModeBox("MultiPlayer Scoreboard", "View multiplayer statistics",
                "M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8 2.146 2.854Z",
                Color.web("#9C27B0"), e -> showMultiPlayerScoreboard());

        layout.getChildren().addAll(titleLabel, singlePlayer5LetterBox, singlePlayer6LetterBox, multiPlayerBox,
                singlePlayerScoreboardBox, multiPlayerScoreboardBox, tutorialBox);
        return layout;
    }

    private HBox createModeBox(String title, String description, String iconPath, Color bgColor, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10, 20, 10, 20));
        box.setPrefSize(400, 60);
        box.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1); " +
                        "-fx-cursor: hand; "
        );

        SVGPath icon = new SVGPath();
        icon.setContent(iconPath);
        icon.setFill(Color.WHITE);
        icon.setScaleX(1.2);
        icon.setScaleY(1.2);

        StackPane iconBox = new StackPane(icon);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setPrefSize(40, 40);
        iconBox.setStyle("-fx-background-color: " + toRGBCode(bgColor) + "; -fx-background-radius: 5;");

        VBox textBox = new VBox(2);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.BLACK);
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.GRAY);

        textBox.getChildren().addAll(titleLabel, descLabel);

        box.getChildren().addAll(iconBox, textBox);
        box.setOnMouseClicked(e -> action.handle(new javafx.event.ActionEvent()));

        return box;
    }

    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void startSinglePlayerGame(int wordLength) {
        if (singlePlayerUIManager == null) {
            singlePlayerUIManager = new SinglePlayerUIManager(statisticsManager, wordValidator);
        }
        wordleGame.setWordLength(wordLength);
        wordleGame.setSinglePlayerUIManager(singlePlayerUIManager);  // Set the SinglePlayerUIManager
        singlePlayerUIManager.setWordLength(wordLength);
        singlePlayerUIManager.setWordleGame(wordleGame);
        wordleGame.startNewSinglePlayerGame();
        VBox gameLayout = singlePlayerUIManager.createSinglePlayerGameLayout();
        Scene gameScene = new Scene(gameLayout, 1200, 1000);
        primaryStage.setScene(gameScene);
        primaryStage.setFullScreen(false);
        gameLayout.requestFocus();
    }

    private void startMultiPlayerGame() {
        if (multiPlayerUIManager == null) {
            multiPlayerUIManager = new MultiPlayerUIManager(statisticsManager, wordValidator, primaryStage);
            multiPlayerUIManager.setWordleGame(wordleGame);
        }
        wordleGame.setMultiPlayerUIManager(multiPlayerUIManager);
        VBox gameLayout = multiPlayerUIManager.createBaseLayout("Multiplayer Wordle");
        Scene gameScene = new Scene(gameLayout, 1200, 1000);
        primaryStage.setScene(gameScene);
        primaryStage.setFullScreen(false);
        multiPlayerUIManager.initializeMultiplayerGame();
        gameLayout.requestFocus();
    }

    private void showSinglePlayerScoreboard() {
        VBox scoreboardLayout = new VBox(20);
        scoreboardLayout.setAlignment(Pos.CENTER);
        scoreboardLayout.setPadding(new Insets(20));
        scoreboardLayout.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("Single Player Scoreboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);

        VBox stats5LetterNode = statisticsManager.getSinglePlayerStatisticsNode(5);
        VBox stats6LetterNode = statisticsManager.getSinglePlayerStatisticsNode(6);

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> showMainScreen());
        backButton.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );

        scoreboardLayout.getChildren().addAll(titleLabel, stats5LetterNode, stats6LetterNode, backButton);

        Scene scoreboardScene = new Scene(scoreboardLayout, 1200, 1000);
        primaryStage.setScene(scoreboardScene);
        primaryStage.setFullScreen(false);
    }

    private void showMultiPlayerScoreboard() {
        VBox scoreboardLayout = new VBox(20);
        scoreboardLayout.setAlignment(Pos.CENTER);
        scoreboardLayout.setPadding(new Insets(20));
        scoreboardLayout.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("Multiplayer Scoreboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);

        VBox statsNode = statisticsManager.getMultiPlayerStatisticsNode();

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> showMainScreen());
        backButton.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );

        scoreboardLayout.getChildren().addAll(titleLabel, statsNode, backButton);

        Scene scoreboardScene = new Scene(scoreboardLayout, 1200, 1000);
        primaryStage.setScene(scoreboardScene);
        primaryStage.setFullScreen(false);
    }

    private void showTutorial() {
        tutorialManager.showTutorial();
    }

    public void returnToMainScreen() {
        javafx.application.Platform.runLater(() -> {
            showMainScreen();
            primaryStage.setFullScreen(false);
        });
    }

    public HostServices getAppHostServices() {
        return getHostServices();
    }

    public static void main(String[] args) {
        launch(args);
    }
}