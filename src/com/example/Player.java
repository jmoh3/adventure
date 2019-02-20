package com.example;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private GameEngine gameEngine;
    private List<String> knownInnocent;
    private List<String> knownWeapons;
    private List<String> knownRooms;
    private String item;

    /**
     * Creates a Player object.
     *
     * @param setName name of Player.
     * @param setGameEngine game engine for player.
     */
    public Player(String setName,
                  GameEngine setGameEngine) {
        this.name = setName;
        this.gameEngine = setGameEngine;

        this.knownInnocent = new ArrayList<String>();
        this.knownWeapons = new ArrayList<String>();
        this.knownRooms = new ArrayList<String>();
    }

    /**
     * Changes current room of player.
     *
     * @param directionName direction name
     */
    public void changeRooms(String directionName) {
        gameEngine.changeRooms(directionName);
    }

    /**
     * Allows user to pick up an item from their current room.
     *
     * @param toPickup item to pick up.
     * @return true if succeeds, false otherwise.
     */
    public boolean pickupItem(String toPickup) {
        if (this.item != null) {
            return false;
        }
        try {
            boolean success = this.gameEngine.getCurrentRoom().removeItem(toPickup);
            if (success) {
                this.item = toPickup;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Allows user to drop whatever item they are carrying.
     * @return true if succeeds, false otherwise.
     */
    public boolean dropItem() {
        if (this.item == null) {
            return false;
        }
        this.gameEngine.getCurrentRoom().dropItem(this.item);
        this.item = null;
        return true;
    }

    /**
     * Adds a new piece of information about murder suspects to Player object.
     *
     * @param characterName name of innocent character.
     */
    public void addKnownCharacter(String characterName) {
        this.knownInnocent.add(characterName);
    }

    /**
     * Adds a new piece of information about suspected murder weapon to Player object.
     *
     * @param objectName name of incorrect murder weapon.
     */
    public void addKnownObject(String objectName) {
        this.knownWeapons.add(objectName);
    }

    /**
     * Adds a new piece of information about suspected murder scene (room) to Player object.
     *
     * @param roomName name of incorrect room.
     */
    public void addKnownRoom(String roomName) {
        this.knownRooms.add(roomName);
    }

    /**
     * Getter for player name.
     *
     * @return player's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for item that player is holding.
     *
     * @return item.
     */
    public String getItem() {
        return this.item;
    }

    /**
     * Getter for player's current room.
     *
     * @return current room.
     */
    public Room getCurrentRoom() {
        return this.gameEngine.getCurrentRoom();
    }

    /**
     * Returns all Player's information about known characters (should only be used for testing purposes).
     *
     * @return character names known innocent.
     */
    public List<String> getKnownCharacters() {
        return this.knownInnocent;
    }

    /**
     * Returns all Player's information about known weapons (should only be used for testing purposes).
     *
     * @return incorrect weapon names.
     */
    public List<String> getKnownWeapons() {
        return this.knownWeapons;
    }

    /**
     * Returns all Player's information about known rooms (should only be used for testing purposes).
     *
     * @return incorrect room names.
     */
    public List<String> getKnownRooms() {
        return this.knownRooms;
    }

    /**
     * Returns whether given character is known by Player to be innocent or not.
     *
     * @param characterName character name to be asked about.
     * @return true if character is known innocent, false otherwise.
     */
    public boolean characterIsInnocent(String characterName) {
        if (characterName == null) {
            return false;
        }

        for (String character : this.knownInnocent) {
            if (character.equalsIgnoreCase(characterName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether given object is known by Player to be correct or not.
     *
     * @param objectName object name to be asked about.
     * @return true if object is known to be incorrect, false otherwise.
     */
    public boolean hasWeapon(String objectName) {
        if (objectName == null) {
            return false;
        }

        for (String item : this.knownWeapons) {
            if (item.equalsIgnoreCase(objectName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether given room is known by Player to be correct or not.
     *
     * @param roomName room name to be asked about.
     * @return true if roomName is known incorrect, false otherwise.
     */
    public boolean hasRoom(String roomName) {
        if (roomName == null) {
            return false;
        }

        for (String room : this.knownRooms) {
            if (room.equalsIgnoreCase(roomName)) {
                return true;
            }
        }

        return false;
    }

}
