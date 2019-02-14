# Extending the Adventure Assignment

I was directed by my code moderator to make the following changes.

## Smaller changes:
* Using return (condition) in Direction instead of if else statement :ballot_box_with_check:
* Renaming readURL to readUrl in Layout :ballot_box_with_check:
* Creating more new lines in readURL :ballot_box_with_check:
* Renaming getRoomForDirection to getRoomFromDirection :ballot_box_with_check:
* Using /** */ for comments regarding descriptions rather than // :ballot_box_with_check:
* Magic number in formatDirections

## Changes that require more restructuring:
* Adding another class to keep track of game state and input handling. I will create a GameEngine class in order to accomplish this and separate my main function from the PlayAdventure class so that my methods are better organized. :ballot_box_with_check:
* Decreasing size of main function :ballot_box_with_check:

## Additions to assignment:
* Enabling main function to accept arguments from the command line :ballot_box_with_check:
* Enable Layout to be created from filepath :ballot_box_with_check:
* Support modifications to JSON schema (using items, keys, etc)
* Extending game in a meaningful and interesting way

# My plan to extend this assignment

I'm going to make this game into a version of the online board game, Clue. The goal of this game is for the users to guess who committed a murder, in what location, and with what weapon.

In one turn, each of the five players can change rooms up to three times, pick up and drop objects (can hold at most one at a time), and make guesses for the correct object, room, and murder weapon.

Each player has pieces of information regarding innocent players, incorrect locations, and incorrect murder weapons. Each time they make a guess each of their guesses are validated by the knowledge of the player next to them.

In order to implement this game, I will:

* Create a custom Layout JSON :ballot_box_with_check:
* Create a Player class :ballot_box_with_check:
    * Store name, Player's information on murder, item
    * Provide ability to change rooms, pick up, and drop items.
* Create a Clue class that handles the internals of the game
    * Store correct information about the murder
    * Prompt and handle user input