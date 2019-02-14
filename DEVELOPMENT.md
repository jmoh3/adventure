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
* Adding another class to keep track of game state and input handling. I will create a GameEngine class in order to accomplish this and separate my main function from the PlayAdventure class so that my methods are better organized.
* Decreasing size of main function :ballot_box_with_check:

## Additions to assignment:
* Enabling main function to accept arguments from the command line :ballot_box_with_check:
* Enable Layout to be created from filepath :ballot_box_with_check:
* Support modifications to JSON schema (using items, keys, etc)
* Extending game in a meaningful and interesting way