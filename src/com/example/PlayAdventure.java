package com.example;

import java.util.Scanner;

/**
 * Allows user to play Adventure.
 *
 */
public class PlayAdventure {

    // URL to default Layout JSON to use
    private static String defaultSiebelUrl = "https://courses.engr.illinois.edu/cs126/adventure/siebel.json";
    private static Layout layout;

    public static void main(String[] args) {

        try {
            layout = Layout.getLayoutFromURL(defaultSiebelUrl);
        } catch (Exception e) {
            System.out.println("Error reading in default JSON. Honestly if this happens you should just give up.");
        }

        System.out.println("Type yes if you want to use the default layout. Otherwise, enter in a valid url to a layout JSON");
        Scanner sc = new Scanner(System.in);
        String layoutDecision = sc.nextLine();

        if (layoutDecision == null || !layoutDecision.toLowerCase().equals("yes")) {
            boolean validLayoutObtained = false;

            while (!validLayoutObtained && !(layoutDecision.toLowerCase().equals("yes"))) {
                try {
                    layout = Layout.getLayoutFromURL(layoutDecision);
                    validLayoutObtained = true;
                } catch (Exception e) {
                    System.out.println("You have entered an invalid url or command. Please try again.");
                    layoutDecision = sc.nextLine();
                }
            }
        }

        Room currentRoom = layout.getCurrentRoom();
        boolean reachedEndingRoom = false;

        while (!reachedEndingRoom) {
            boolean userInputIsValid = false;
            boolean shouldQuit = false;

            while (!userInputIsValid) {
                System.out.println(currentRoom.getDescription());
                System.out.println("From here, you may go" + formatDirections(currentRoom));

                String userDirections = sc.nextLine();
                String[] decipheredInput = decipherUserInput(userDirections);

                if (decipheredInput == null) {
                    shouldQuit = true;
                    break;
                } else if (decipheredInput[0].equals("go")) {
                    try {
                        currentRoom = layout.changeRooms(decipheredInput[1]);
                        userInputIsValid = true;
                    } catch (Exception e) {
                        System.out.println("I can't " + userDirections);
                    }
                } else {
                    System.out.println("I don't understand '" + userDirections + "'");
                }
            }

            if (shouldQuit == true) {
                break;
            }
            if (currentRoom.getName().equals(layout.getEndingRoom())) {
                reachedEndingRoom = true;
            }
        }

        if (reachedEndingRoom) {
            System.out.println(currentRoom.getDescription());
            System.out.println("You have reached your final destination");
        }
    }

    /**
     * Formats the valid directions for a room so that output for GUI is clean.
     *
     * @param room current room.
     * @return a String listing available directions with appropriate punctuation and grammar.
     */
    private static String formatDirections(Room room) {
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
     * Takes user input and decides what to do with it.
     *
     * @param input what the user prompts the game to do.
     * @return null if the user directs game to quit, otherwise a lowercase array of all words separated by spaces.
     */
    private static String[] decipherUserInput(String input) {
        String userInput = input.toLowerCase();

        if (userInput.equals("quit") || userInput.equals("exit")) {
            return null;
        }

        String[] splitInput = userInput.split(" ");

        return splitInput;
    }

}