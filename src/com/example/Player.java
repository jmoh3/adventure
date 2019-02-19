package com.example;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private GameEngine gameEngine;
    private List<String> knownInnocent;
    private List<String> knownObjects;
    private List<String> knownRooms;
    private String item;

    public Player(String setName,
                  GameEngine setGameEngine) {
        this.name = setName;
        this.gameEngine = setGameEngine;

        this.knownInnocent = new ArrayList<String>();
        this.knownObjects = new ArrayList<String>();
        this.knownRooms = new ArrayList<String>();
    }

    public void changeRooms(String directionName) {
        gameEngine.changeRooms(directionName);
    }

    public boolean pickupItem(String toPickup) {
        return this.gameEngine.getCurrentRoom().removeItem(toPickup);
    }

    public void dropItem() {
        this.gameEngine.getCurrentRoom().dropItem(this.item);
        this.item = null;
    }

    public void addKnownCharacter(String characterName) {
        this.knownInnocent.add(characterName);
    }

    public void addKnownObject(String objectName) {
        this.knownObjects.add(objectName);
    }

    public void addKnownRoom(String roomName) {
        this.knownRooms.add(roomName);
    }

    public List<String> getKnownCharacters() {
        return this.knownInnocent;
    }

    public List<String> getKnownWeapons() {
        return this.knownObjects;
    }

    public List<String> getKnownRooms() {
        return this.knownRooms;
    }

    public boolean characterIsInnocent(String characterName) {
        return knownInnocent.contains(characterName);
    }

    public boolean hasObject(String objectName) {
        return knownObjects.contains(objectName);
    }

    public boolean hasRoom(String roomName) {
        return knownRooms.contains(roomName);
    }

}
