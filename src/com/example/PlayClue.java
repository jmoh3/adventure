package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayClue {

    public static void main(String[] args) {
        Layout layout = Layout.getLayoutFromFilepath("custom.json");
        List<String> characterNames = new ArrayList<String>(
                Arrays.asList("ProfessorPlum", "ColonelMustard", "MissScarlett", "MrsWhite", "MrGreen", "MrsPeacock")
        );

        Clue clueGame = new Clue(layout, characterNames);

        clueGame.playGame();

    }
}
