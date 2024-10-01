package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.Map;

public class StatisticsManager {

    private Map<Integer, SinglePlayerStats> singlePlayerStatsByLength;
    private Map<Integer, MultiPlayerStats> multiPlayerStatsByLength;

    public StatisticsManager() {
        resetStats();
    }

    private void resetStats() {
        singlePlayerStatsByLength = new HashMap<>();
        singlePlayerStatsByLength.put(5, new SinglePlayerStats());
        singlePlayerStatsByLength.put(6, new SinglePlayerStats());

        multiPlayerStatsByLength = new HashMap<>();
        multiPlayerStatsByLength.put(5, new MultiPlayerStats());
        multiPlayerStatsByLength.put(6, new MultiPlayerStats());
    }

    // Single-player methods
    public void incrementSinglePlayerGamesPlayed(int wordLength) {
        singlePlayerStatsByLength.get(wordLength).gamesPlayed++;
    }

    public void incrementSinglePlayerWins(int wordLength) {
        singlePlayerStatsByLength.get(wordLength).wins++;
    }

    public void incrementSinglePlayerLosses(int wordLength) {
        singlePlayerStatsByLength.get(wordLength).losses++;
    }

    public void addSinglePlayerGuessesForWin(int guesses, int wordLength) {
        singlePlayerStatsByLength.get(wordLength).totalGuessesForWins += guesses;
    }

    public void addScore(int score, int wordLength) {
        singlePlayerStatsByLength.get(wordLength).totalScore += score;
    }

    // Multiplayer methods
    public void incrementMultiPlayerGamesPlayed(int wordLength) {
        MultiPlayerStats stats = multiPlayerStatsByLength.get(wordLength);
        stats.gamesPlayed++;
        if (stats.gamesPlayed % 2 == 0) {
            stats.roundsPlayed++;
        }
    }

    public void incrementPlayer1Wins(int wordLength) {
        multiPlayerStatsByLength.get(wordLength).player1Wins++;
    }

    public void incrementPlayer2Wins(int wordLength) {
        multiPlayerStatsByLength.get(wordLength).player2Wins++;
    }

    public void incrementPlayer1Losses(int wordLength) {
        multiPlayerStatsByLength.get(wordLength).player1Losses++;
    }

    public void incrementPlayer2Losses(int wordLength) {
        multiPlayerStatsByLength.get(wordLength).player2Losses++;
    }

    public void addMultiPlayerGuessesForWin(int guesses, int wordLength) {
        multiPlayerStatsByLength.get(wordLength).totalGuessesForWins += guesses;
    }

    public void addMultiPlayerScore(int score, int player, int wordLength) {
        MultiPlayerStats stats = multiPlayerStatsByLength.get(wordLength);
        if (player == 1) {
            stats.player1TotalScore += score;
        } else if (player == 2) {
            stats.player2TotalScore += score;
        }
        stats.totalScore += score;
    }

    public String getSinglePlayerStatistics(int wordLength) {
        SinglePlayerStats stats = singlePlayerStatsByLength.get(wordLength);
        double averageGuesses = stats.wins > 0 ? (double) stats.totalGuessesForWins / stats.wins : 0;
        double averageScore = stats.gamesPlayed > 0 ? (double) stats.totalScore / stats.gamesPlayed : 0;
        double winRate = stats.gamesPlayed > 0 ? (double) stats.wins / stats.gamesPlayed * 100 : 0;

        return String.format(
                "%d-Letter Mode:\n" +
                        "Games Played: %d\n" +
                        "Wins: %d\n" +
                        "Losses: %d\n" +
                        "\n" +
                        "Win Rate: %.2f%%\n" +
                        "\n" +
                        "Average Guesses per Win: %.2f\n" +
                        "Total Score: %d",
                wordLength,
                stats.gamesPlayed,
                stats.wins,
                stats.losses,
                winRate,
                averageGuesses,
                stats.totalScore
        );
    }

    public String getMultiPlayerStatistics(int wordLength) {
        MultiPlayerStats stats = multiPlayerStatsByLength.get(wordLength);
        return String.format(
                "%d-Letter Mode:\n" +
                        "Total Games Played: %d\n" +
                        "Rounds Played: %d\n" +
                        "Player 1 Wins: %d                          Player 2 Wins: %d\n" +
                        "Player 1 Losses: %d                         Player 2 Losses: %d\n" +
                        "Player 1 Total Score: %d                Player 2 Total Score: %d\n",
                wordLength,
                stats.gamesPlayed,
                stats.roundsPlayed,
                stats.player1Wins, stats.player2Wins,
                stats.player1Losses, stats.player2Losses,
                stats.player1TotalScore, stats.player2TotalScore
        );
    }

    public String getAllStatistics() {
        return getSinglePlayerStatistics(5) + "\n\n" +
                getSinglePlayerStatistics(6) + "\n\n" +
                getMultiPlayerStatistics(5) + "\n\n" +
                getMultiPlayerStatistics(6);
    }

    public VBox getSinglePlayerStatisticsNode(int wordLength) {
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

        Label titleLabel = new Label(wordLength + "-Letter Mode Statistics");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.BLACK);

        SinglePlayerStats stats = singlePlayerStatsByLength.get(wordLength);
        double winRate = stats.gamesPlayed > 0 ? (double) stats.wins / stats.gamesPlayed * 100 : 0;
        double averageGuesses = stats.wins > 0 ? (double) stats.totalGuessesForWins / stats.wins : 0;

        HBox gamesPlayedBox = createStatRow("Games Played", String.valueOf(stats.gamesPlayed));
        HBox winsBox = createStatRow("Wins", String.valueOf(stats.wins));
        HBox lossesBox = createStatRow("Losses", String.valueOf(stats.losses));
        HBox winRateBox = createStatRow("Win Rate", String.format("%.2f%%", winRate));
        HBox avgGuessesBox = createStatRow("Avg Guesses per Win", String.format("%.2f", averageGuesses));
        HBox totalScoreBox = createStatRow("Total Score", String.valueOf(stats.totalScore));

        statsBox.getChildren().addAll(titleLabel, gamesPlayedBox, winsBox, lossesBox, winRateBox, avgGuessesBox, totalScoreBox);
        return statsBox;
    }

    public VBox getMultiPlayerStatisticsNode(int wordLength) {
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

        Label titleLabel = new Label(wordLength + "-Letter Multiplayer Statistics");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.BLACK);

        MultiPlayerStats stats = multiPlayerStatsByLength.get(wordLength);

        HBox playersBox = new HBox(50);
        playersBox.setAlignment(Pos.CENTER);

        VBox player1Stats = createPlayerStatsBox("Player 1", stats.player1Wins, stats.player1Losses, stats.player1TotalScore);
        VBox player2Stats = createPlayerStatsBox("Player 2", stats.player2Wins, stats.player2Losses, stats.player2TotalScore);

        playersBox.getChildren().addAll(player1Stats, player2Stats);

        VBox gamesPlayedBox = new VBox(5);
        gamesPlayedBox.setAlignment(Pos.CENTER);
        Label gamesPlayedLabel = new Label("Total Games Played: " + stats.gamesPlayed);
        gamesPlayedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label roundsPlayedLabel = new Label("Rounds Played: " + stats.roundsPlayed);
        roundsPlayedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label roundeEplanation = new Label("(A round consists of 2 games where Player 1 and Player 2 switch roles.)");
        roundeEplanation.setFont(Font.font("Arial", FontWeight.LIGHT, 14));
        gamesPlayedBox.getChildren().addAll(gamesPlayedLabel, roundsPlayedLabel, roundeEplanation);

        statsBox.getChildren().addAll(titleLabel, playersBox, gamesPlayedBox);
        return statsBox;
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

    private class SinglePlayerStats {
        int gamesPlayed;
        int wins;
        int losses;
        int totalGuessesForWins;
        int totalScore;
    }

    private class MultiPlayerStats {
        int roundsPlayed;
        int gamesPlayed;
        int player1Wins;
        int player2Wins;
        int player1Losses;
        int player2Losses;
        int totalGuessesForWins;
        int player1TotalScore;
        int player2TotalScore;
        int totalScore;
    }
}