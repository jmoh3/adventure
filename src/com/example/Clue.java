package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A five player game in which users try to guess the name of the murderer, the weapon, and the location.
 * Users can navigate through rooms, and pickup items.
 */
public class Clue {

    /** Names of characters in game. */
    private GameEngine gameEngine;
    private Layout layout;
    private List<String> characterNames;
    private List<String> roomNames;
    private List<String> weaponNames;

    private String murderer;
    private String weapon;
    private String room;

    private Player[] players;

    public Clue(Layout setLayout, List<String> charNames) {
        this.layout = setLayout;
        this.roomNames = new ArrayList<String>();
        this.weaponNames = new ArrayList<String>();
        this.gameEngine = new GameEngine(layout);
        this.characterNames = charNames;

        setUpFromLayout(layout);

        this.players = setUpPlayers(this.characterNames);
    }

    private void setUpFromLayout(Layout layout) {
        List<Room> seen = new ArrayList<Room>();

        setUpHelper(gameEngine.getRoom(layout.getStartingRoom()), seen);
    }

    public Player[] setUpPlayers(List<String> characterNames) {
        Player[] createdPlayers = new Player[characterNames.size() - 1];

        for (int i = 0; i < characterNames.size() - 1; i++) {
            createdPlayers[i] = new Player(characterNames.get(i), gameEngine);
        }

        dealCards(this.characterNames, this.weaponNames, this.roomNames, createdPlayers);

        return createdPlayers;
    }

    /**
     * Private recursive helper function to initialize room and weapon names.
     *
     * @param current current Room.
     * @param seen Rooms that we have already seen.
     * @return true if the ending room has been reached.
     */
    private void setUpHelper(Room current, List<Room> seen) {
        if (current.getName().equals(layout.getEndingRoom())) {
            return;
        }
        seen.add(current);

        this.roomNames.add(current.getName());

        if (current.getItems() != null) {
            for (String item : current.getItems()) {
                this.weaponNames.add(item);
            }
        }

        for (Direction direction : current.getDirections()) {
            Room nextRoom = gameEngine.getRoom(current.getRoomFromDirection(direction.getDirectionName()));
            if (seen.contains(nextRoom)) {
                continue;
            }
            setUpHelper(nextRoom, seen);
        }
    }

    public List<String> getRoomNames() {
        return this.roomNames;
    }

    public List<String> getWeaponNames() {
        return this.weaponNames;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public String getMurderer() {
        return this.murderer;
    }

    public String getWeapon() {
        return this.weapon;
    }

    public String getRoom() {
        return this.room;
    }

    public void dealCards(List<String> setCharacterNames, List<String> setWeaponNames, List<String> setRoomNames, Player[] players) {
        List<String> characterNames = new ArrayList<String>(setCharacterNames);
        List<String> weaponNames = new ArrayList<String>(setWeaponNames);
        List<String> roomNames = new ArrayList<String>(setRoomNames);

        Random rand = new Random();

        int randSelection = rand.nextInt(characterNames.size());
        this.murderer = characterNames.remove(randSelection);
        Collections.shuffle(characterNames);

        randSelection = rand.nextInt(weaponNames.size());
        this.weapon = weaponNames.remove(randSelection);
        Collections.shuffle(weaponNames);

        randSelection = rand.nextInt(roomNames.size());
        this.room = roomNames.remove(randSelection);
        Collections.shuffle(roomNames);

        int playerIndex = 0;

        while (!roomNames.isEmpty() || !characterNames.isEmpty() || !weaponNames.isEmpty()) {
            playerIndex = playerIndex % players.length;
            if (!characterNames.isEmpty()) {
                players[playerIndex].addKnownCharacter(characterNames.remove(0));
            }
            if (!weaponNames.isEmpty()) {
                players[playerIndex].addKnownObject(weaponNames.remove(0));
            }
            if (!roomNames.isEmpty()) {
                players[playerIndex].addKnownRoom(roomNames.remove(0));
            }
            playerIndex++;
        }
    }

}
