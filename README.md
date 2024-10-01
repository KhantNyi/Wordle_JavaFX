# Wordle_JavaFX
Wordle game written in JavaFX featuring singleplayer, multiplayer, scoreboards and how to play tutorial section.

## How To Run

Set up a JavaFX project in IntelliJ and put all these files inside the project.

## Game Features

- Singleplayer (Classic worlde experience where player has to guess a 5 letter word randomly set by the system in 6 tries)
- Multiplayer (Two player mode where player 1 and 2 can take turns setting and guessing the word)
- Singleplayer Scoreboard (Game statistics for singleplayer mode)
- Multiplayer Scoreboard (Game statistics for multiplayer mode including scoring system)
- Tutorial (How to play section with a fun easter egg embedded in the code)

## Version 1.2
- Added a new Singleplayer mode (6 letter word)
- Added a new Mulitplayer mode (6 letter word)

## Other changes
- Smoothing out UI and fixed the inconsistency of dimensions for different screens (windows) by setting a fixed height and width.
- Introduce a new Rounds Played logic in Multiplayer statistics where a round consists of 2 games where Player 1 and Player 2 switch roles. Originally, a game is counted when player 1 sets the word and player 2 guesses the word. I kept the games played counter and added a new rounds played.
- Updated the TutorialManager to include how to play tutorial for new addition (6 letter word) mode.
