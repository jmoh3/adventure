package com.example;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Scanner;

/**
 * Allows user to play Adventure.
 *
 */
public class PlayAdventure {

    private static Layout layout;
    private static GameEngine gameEngine;

    /**
     * Sets up Layout and GameEngine objects from url to Layout JSON.
     *
     * @param url url to a layout JSON.
     * @throws MalformedURLException thrown if given a bad URL.
     * @throws IOException thrown if getLayoutFromURL cannot read URL.
     */
    public void setupGameUsingUrl(String url) throws MalformedURLException, IOException {
        layout = Layout.getLayoutFromURL(url);

        gameEngine = new GameEngine(layout);

        if (gameEngine.validateLayout()) {
            System.out.println("VALID PATH FROM START TO END EXISTS");
        } else {
            System.out.println("VALID PATH FROM START TO END DOES NOT EXIST");
        }
    }

    /**
     * Sets up Layout and GameEngine objects from url to Layout JSON.
     *
     * @param filepath filepath to a layout JSON.
     */
    public void setupGameUsingFilepath(String filepath) {
        String jsonString = Data.getFileContentsAsString(filepath);
        layout = Layout.getLayoutFromFilepath(filepath);

        gameEngine = new GameEngine(layout);

        if (gameEngine.validateLayout()) {
            System.out.println("VALID PATH FROM START TO END EXISTS");
        } else {
            System.out.println("VALID PATH FROM START TO END DOES NOT EXIST");
        }
    }

    /**
     * Enables user to play game.
     */
    public void playGame() {
        Room currentRoom = gameEngine.getCurrentRoom();
        boolean shouldContinue = true;

        while (shouldContinue) {
            currentRoom = gameEngine.getCurrentRoom();
            shouldContinue = handleUserDirections(currentRoom);
            currentRoom = gameEngine.getCurrentRoom();

            if (currentRoom.getName().equals(layout.getEndingRoom())) {
                System.out.println(currentRoom.getDescription());
                System.out.println("You have reached your final destination");
                break;
            }
        }
    }

    /**
     * Provides instructions for user and handles their input.
     *
     * @param currentRoom Room that the user is currently in.
     * @return true if user provides valid directions, false if they decide to quit.
     */
    public boolean handleUserDirections(Room currentRoom) {
        Scanner sc = new Scanner(System.in);

        System.out.println(currentRoom.getDescription());
        System.out.println("From here, you may go" + formatDirections(currentRoom));

        String userDirections = sc.nextLine();
        String[] decipheredInput = decipherUserDirections(userDirections);

        if (decipheredInput == null) {
            return false;
        } else if (decipheredInput[0].equals("go")) {
            try {
                gameEngine.changeRooms(decipheredInput[1]);
                return true;
            } catch (Exception e) {
                System.out.println("I can't " + userDirections);
                return handleUserDirections(currentRoom);
            }
        } else {
            System.out.println("I don't understand '" + userDirections + "'");
            return handleUserDirections(currentRoom);
        }
    }

    /**
     * Formats the valid directions for a room so that output for GUI is clean.
     *
     * @param room current room.
     * @return a String listing available directions with appropriate punctuation and grammar.
     */
    public static String formatDirections(Room room) {
        String output = "";
        for (int i = 0; i < room.getDirections().length; i++) {
            if (i > 0 && room.getDirections().length > 2) {
                output = output + ",";
            }
            if (i == room.getDirections().length - 1 && room.getDirections().length > 1) {
                output = output + " and";
            }
            output = output + " " + room.getDirections()[i].getDirectionName();
        }
        return output;
    }

    /**
     * Formats the valid items in a room so that output is clean.
     *
     * @param room current room.
     * @return a String listing items in room with appropriate punctuation and grammar.
     */
    public static String formatItems(Room room) {
        if (room.getItems() == null || room.getItems().size() == 0) {
            return "";
        }

        String output = "";
        for (int i = 0; i < room.getItems().size(); i++) {
            if (i > 0 && room.getItems().size() > 2) {
                output = output + ",";
            }
            if (i == room.getItems().size() - 1 && room.getItems().size() > 1) {
                output = output + " and";
            }
            output = output + " " + room.getItems().get(i);
        }
        return output;
    }

    /**
     * Takes user input and decides what to do with it.
     *
     * @param input what the user prompts the game to do.
     * @return null if the user directs game to quit, otherwise a lowercase array of all words separated by spaces.
     */
    public String[] decipherUserDirections(String input) {
        String userInput = input.toLowerCase();

        if (userInput.equals("quit") || userInput.equals("exit")) {
            return null;
        }

        String[] splitInput = userInput.split(" ");

        return splitInput;
    }

}