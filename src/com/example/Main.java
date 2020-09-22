package com.example;

public class Main {

    /** Default URL to use if no command line arguments are provided. */
    private static String defaultSiebelUrl = "https://courses.engr.illinois.edu/cs126/adventure/siebel.json";

    public static void main(String[] args) {
        PlayAdventure adventure = new PlayAdventure();

        if (args.length == 0) {
            System.out.println("Using default Siebel JSON...");
            // use default
            try {
                adventure.setupGameUsingUrl(defaultSiebelUrl);
            } catch (Exception e) {

            }
        } else {
            // try to make it a url, else see if it's a filepath
            try {
                adventure.setupGameUsingUrl(args[0]);
                System.out.println("Using provided URL to Layout JSON...");
            } catch (Exception e) {
                adventure.setupGameUsingFilepath(args[0]);
                System.out.println("Using provided filepath to Layout JSON...");
            }
        }

        adventure.playGame();
    }

}
