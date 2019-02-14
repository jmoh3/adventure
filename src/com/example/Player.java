package com.example;

import java.util.List;

public class Player {

    private String name;
    private GameEngine gameEngine;
    private List<String> knownInnocent;
    private String item;

    public Player(String setName, GameEngine setGameEngine, List<String> setKnownInnocent) {
        this.name = setName;
        this.gameEngine = setGameEngine;
        this.knownInnocent = setKnownInnocent;
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

    public boolean characterIsInnocent(String characterName) {
        return knownInnocent.contains(characterName);
    }

}
