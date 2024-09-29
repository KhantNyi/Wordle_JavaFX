package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatisticsManager {

    // Single-player statistics
    private int singlePlayerGamesPlayed;
    private int singlePlayerWins;
    private int singlePlayerLosses;
    private int singlePlayerTotalGuessesForWins;
    private int singlePlayerTotalScore;

    // Multiplayer statistics
    private int multiPlayerGamesPlayed;
    private int player1Wins;
    private int player2Wins;
    private int player1Losses;
    private int player2Losses;
    private int multiPlayerTotalGuessesForWins;
    private int player1TotalScore;
    private int player2TotalScore;
    private int multiPlayerTotalScore;

    public StatisticsManager() {
        resetStats();
    }

    private void resetStats() {
        singlePlayerGamesPlayed = 0;
        singlePlayerWins = 0;
        singlePlayerLosses = 0;
        singlePlayerTotalGuessesForWins = 0;
        singlePlayerTotalScore = 0;
        multiPlayerGamesPlayed = 0;
        player1Wins = 0;
        player2Wins = 0;
        player1Losses = 0;
        player2Losses = 0;
        multiPlayerTotalGuessesForWins = 0;
        player1TotalScore = 0;
        player2TotalScore = 0;
        multiPlayerTotalScore = 0;
    }

    // Single-player methods
    public void incrementSinglePlayerGamesPlayed() {
        singlePlayerGamesPlayed++;
    }

    public void incrementSinglePlayerWins() {
        singlePlayerWins++;
    }

    public void incrementSinglePlayerLosses() {
        singlePlayerLosses++;
    }

    public void addSinglePlayerGuessesForWin(int guesses) {
        singlePlayerTotalGuessesForWins += guesses;
    }

    public void addScore(int score) {
        singlePlayerTotalScore += score;
    }

    // Multiplayer methods
    public void incrementMultiPlayerGamesPlayed() {
        multiPlayerGamesPlayed++;
    }

    public void incrementPlayer1Wins() {
        player1Wins++;
    }

    public void incrementPlayer2Wins() {
        player2Wins++;
    }

    public void incrementPlayer1Losses() {
        player1Losses++;
    }

    public void incrementPlayer2Losses() {
        player2Losses++;
    }

    public void addMultiPlayerGuessesForWin(int guesses) {
        multiPlayerTotalGuessesForWins += guesses;
    }

    public void addMultiPlayerScore(int score, int player) {
        if (player == 1) {
            player1TotalScore += score;
        } else if (player == 2) {
            player2TotalScore += score;
        }
        multiPlayerTotalScore += score;
    }

    public String getSinglePlayerStatistics() {
        double averageGuesses = singlePlayerWins > 0 ? (double) singlePlayerTotalGuessesForWins / singlePlayerWins : 0;
        double averageScore = singlePlayerGamesPlayed > 0 ? (double) singlePlayerTotalScore / singlePlayerGamesPlayed : 0;
        double winRate = singlePlayerGamesPlayed > 0 ? (double) singlePlayerWins / singlePlayerGamesPlayed * 100 : 0;

        return String.format(
                "Games Played: %d\n" +
                        "Wins: %d\n" +
                        "Losses: %d\n" +
                        "\n" +
                        "Win Rate: %.2f%%\n" +
                        "\n" +
                        "Average Guesses per Win: %.2f\n" +
                        "Total Score: %d",
                singlePlayerGamesPlayed,
                singlePlayerWins,
                singlePlayerLosses,
                winRate,
                averageGuesses,
                singlePlayerTotalScore
        );
    }

    public String getMultiPlayerStatistics() {
        return String.format(
                "Player 1 Wins: %d                          Player 2 Wins: %d\n" +
                        "Player 1 Losses: %d                         Player 2 Losses: %d\n" +
                        "Player 1 Total Score: %d                Player 2 Total Score: %d\n" +
                        "\n" +
                        "Total Games Played: %d\n",
                player1Wins, player2Wins,
                player1Losses, player2Losses,
                player1TotalScore, player2TotalScore,
                multiPlayerGamesPlayed
        );
    }

    public String getAllStatistics() {
        return getSinglePlayerStatistics() + "\n\n" + getMultiPlayerStatistics();
    }

    public VBox getSinglePlayerStatisticsNode() {
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20));
        statsBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);"
        );

        Label titleLabel = new Label("Single Player Statistics");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.BLACK);

        double winRate = singlePlayerGamesPlayed > 0 ? (double) singlePlayerWins / singlePlayerGamesPlayed * 100 : 0;
        double averageGuesses = singlePlayerWins > 0 ? (double) singlePlayerTotalGuessesForWins / singlePlayerWins : 0;

        HBox gamesPlayedBox = createStatRow("Games Played", String.valueOf(singlePlayerGamesPlayed));
        HBox winsBox = createStatRow("Wins", String.valueOf(singlePlayerWins));
        HBox lossesBox = createStatRow("Losses", String.valueOf(singlePlayerLosses));
        HBox winRateBox = createStatRow("Win Rate", String.format("%.2f%%", winRate));
        HBox avgGuessesBox = createStatRow("Avg Guesses per Win", String.format("%.2f", averageGuesses));
        HBox totalScoreBox = createStatRow("Total Score", String.valueOf(singlePlayerTotalScore));

        statsBox.getChildren().addAll(titleLabel, gamesPlayedBox, winsBox, lossesBox, winRateBox, avgGuessesBox, totalScoreBox);
        return statsBox;
    }

    public VBox getMultiPlayerStatisticsNode() {
        VBox statsBox = new VBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20));
        statsBox.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);"
        );

        Label titleLabel = new Label("Multiplayer Statistics");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.BLACK);

        HBox playersBox = new HBox(50);
        playersBox.setAlignment(Pos.CENTER);

        VBox player1Stats = createPlayerStatsBox("Player 1", player1Wins, player1Losses, player1TotalScore);
        VBox player2Stats = createPlayerStatsBox("Player 2", player2Wins, player2Losses, player2TotalScore);

        playersBox.getChildren().addAll(player1Stats, player2Stats);

        HBox gamesPlayedBox = createStatRow("Total Games Played", String.valueOf(multiPlayerGamesPlayed));

        statsBox.getChildren().addAll(titleLabel, playersBox, gamesPlayedBox);
        return statsBox;
    }

    private VBox createPlayerStatsBox(String playerName, int wins, int losses, int totalScore) {
        VBox playerBox = new VBox(10);
        playerBox.setAlignment(Pos.TOP_CENTER);
        playerBox.setStyle(
                "-fx-padding: 10; " +
                        "-fx-border-color: #e0e0e0; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;"
        );

        Label nameLabel = new Label(playerName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.BLACK);

        VBox winsBox = createStatColumn("Wins", String.valueOf(wins));
        VBox lossesBox = createStatColumn("Losses", String.valueOf(losses));
        VBox scoreBox = createStatColumn("Total Score", String.valueOf(totalScore));

        playerBox.getChildren().addAll(nameLabel, winsBox, lossesBox, scoreBox);
        return playerBox;
    }

    private VBox createStatColumn(String label, String value) {
        VBox column = new VBox(5);
        column.setAlignment(Pos.CENTER);

        Label labelNode = new Label(label);
        labelNode.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        labelNode.setTextFill(Color.BLACK);

        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("Arial", 14));
        valueNode.setTextFill(Color.GRAY);

        column.getChildren().addAll(labelNode, valueNode);
        return column;
    }

    private HBox createStatRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label + ":");
        labelNode.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        labelNode.setTextFill(Color.BLACK);

        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("Arial", 14));
        valueNode.setTextFill(Color.GRAY);

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }
}