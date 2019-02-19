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

    /** Layout being used for game. */
    private Layout layout;

    /** Names of characters in game. */
    private List<String> characterNames;

    /** Names of rooms in game. */
    private List<String> roomNames;

    /** Names of weapons in game. */
    private List<String> weaponNames;

    /** Correct murderer. */
    private String murderer;

    /** Correct murder weapon. */
    private String weapon;

    /** Correct scene of the crime. */
    private String room;

    /** Players of game (1 less than total character names). */
    private Player[] players;

    /**
     * Creates and sets up a Clue object from layout and list of character names.
     *
     * @param setLayout layout to be used.
     * @param charNames names of characters to be used.
     */
    public Clue(Layout setLayout, List<String> charNames) {
        this.layout = setLayout;
        this.roomNames = new ArrayList<String>();
        this.weaponNames = new ArrayList<String>();
        this.characterNames = charNames;

        setUpFromLayout(layout);

        this.players = setUpPlayers(this.characterNames);
    }

    /**
     * Sets up room name list and weapon name list from given layout.
     *
     * @param layout layout to get weapons and rooms from.
     */
    private void setUpFromLayout(Layout layout) {
        List<Room> seen = new ArrayList<Room>();

        GameEngine gameEngine = new GameEngine(layout);

        setUpHelper(gameEngine.getRoom(layout.getStartingRoom()), seen, gameEngine);
    }

    /**
     * Sets up array of Player objects from character names.
     *
     * @param characterNames names of characters.
     * @return array of instantiated Player objects.
     */
    public Player[] setUpPlayers(List<String> characterNames) {
        Player[] createdPlayers = new Player[characterNames.size() - 1];

        for (int i = 0; i < characterNames.size() - 1; i++) {
            GameEngine gameEngine = new GameEngine(layout);
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
    private void setUpHelper(Room current, List<Room> seen, GameEngine gameEngine) {
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
            setUpHelper(nextRoom, seen, gameEngine);
        }
    }

    /**
     * Getter for room names.
     *
     * @return list of room names.
     */
    public List<String> getRoomNames() {
        return this.roomNames;
    }

    /**
     * Getter for weapon names.
     *
     * @return list of weapon names.
     */
    public List<String> getWeaponNames() {
        return this.weaponNames;
    }

    /**
     * Getter for room names.
     *
     * @return list of room names.
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Getter for correct murderer.
     *
     * @return name of murderer.
     */
    public String getMurderer() {
        return this.murderer;
    }

    /**
     * Getter for correct weapon name.
     *
     * @return name of murder weapon.
     */
    public String getWeapon() {
        return this.weapon;
    }

    /**
     * Getter for correct room name.
     *
     * @return name of correct room where murder took place.
     */
    public String getRoom() {
        return this.room;
    }

    /**
     * Deals all the "cards" for clue game - gives out pieces of information on known innocent character names,
     * incorrect weapon names, and incorrect room names.
     *
     * @param setCharacterNames character names.
     * @param setWeaponNames weapon names.
     * @param setRoomNames room names.
     * @param players list of players.
     */
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
