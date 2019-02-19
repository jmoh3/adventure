package com.example;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ClueTest {

    private static Layout layout;
    private static ArrayList<String> characterNames;
    private static ArrayList<String> roomNames;
    private static ArrayList<String> weaponNames;
    private static Clue clueGame;

    @Before
    public void initializeNecessaryFields() {
        layout = Layout.getLayoutFromFilepath("custom.json");

        roomNames = new ArrayList<String>(
                Arrays.asList("Entrance", "Hall", "Lounge", "DiningRoom",
                        "Kitchen", "Ballroom", "Conservatory", "BilliardRoom",
                        "Library", "Study")
        );

        weaponNames = new ArrayList<String>(
                Arrays.asList("candlestick", "leadPipe", "knife", "rope", "wrench", "revolver")
        );

        characterNames = new ArrayList<String>(
                Arrays.asList("ProfessorPlum", "ColonelMustard", "MissScarlett", "MrsWhite", "MrGreen", "MrsPeacock")
        );

        clueGame = new Clue(layout, characterNames);

    }

    @Test
    public void setUpClueTest() {
        List<String> clueRooms = clueGame.getRoomNames();
        List<String> weapons = clueGame.getWeaponNames();

        assertTrue(clueRooms.containsAll(roomNames) && roomNames.containsAll(clueRooms));
        assertTrue(weapons.containsAll(weaponNames) && weaponNames.containsAll(weapons));
    }

    @Test
    public void dealCardsCorrectKnownCharactersTest() {
        List<String> known = new ArrayList<String>();

        for (Player player : clueGame.getPlayers()) {
            known.addAll(player.getKnownCharacters());
        }

        known.add(clueGame.getMurderer());

        assertTrue(characterNames.containsAll(known) && known.containsAll(characterNames));
    }

    @Test
    public void dealCardsCorrectKnownWeaponsTest() {
        List<String> known = new ArrayList<String>();

        for (Player player : clueGame.getPlayers()) {
            known.addAll(player.getKnownWeapons());
        }

        known.add(clueGame.getWeapon());

        assertTrue(weaponNames.containsAll(known) && known.containsAll(weaponNames));
    }

    @Test
    public void dealCardsCorrectKnownRoomsTest() {
        List<String> known = new ArrayList<String>();

        for (Player player : clueGame.getPlayers()) {
            known.addAll(player.getKnownRooms());
        }

        known.add(clueGame.getRoom());

        assertTrue(roomNames.containsAll(known) && known.containsAll(roomNames));
    }

}

