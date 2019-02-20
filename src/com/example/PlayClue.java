package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayClue {

    public static void main(String[] args) {
        Layout layout = Layout.getLayoutFromFilepath("custom.json");

        if (args.length != 0) {
            // try to make it a url, else see if it's a filepath
            try {
                layout = Layout.getLayoutFromURL(args[0]);
                System.out.println("Using provided URL to Layout JSON...");
            } catch (Exception e) {
                layout = Layout.getLayoutFromFilepath(args[0]);
                System.out.println("Using provided filepath to Layout JSON...");
            }
        }


        List<String> characterNames = new ArrayList<String>(
                Arrays.asList("ProfessorPlum", "ColonelMustard", "MissScarlett")
        );

        Clue clueGame = new Clue(layout, characterNames);

        clueGame.playGame();

    }
}
