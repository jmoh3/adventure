package com.example;

import java.util.HashMap;

public class Room {
    private String name;
    private String description;
    private Direction[] directions;
    private HashMap<String, String> directionToRoomMap;
    private boolean hashMapLoaded = false;

    public Room(String setName, String setDescription, Direction[] setDirections) {
        this.name = setName;
        this.description = setDescription;
        this.directions = setDirections;
        this.directionToRoomMap = new HashMap<String, String>();

        for (Direction direction : directions) {
            directionToRoomMap.put(direction.getDirectionName(), direction.getRoom());
        }
        this.hashMapLoaded = true;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Direction[] getDirections() {
        return this.directions;
    }

    public String getRoomForDirection(String direction) throws NullPointerException, IllegalArgumentException {
        if (direction == null) {
            throw new NullPointerException();
        }
        if (!hashMapLoaded) {
            this.loadHashMap();
        }
        if (!directionToRoomMap.containsKey(direction)) {
            throw new IllegalArgumentException();
        }
        return directionToRoomMap.get(direction);
    }

    private void loadHashMap() {
        this.directionToRoomMap = new HashMap<String, String>();

        for (Direction direction : directions) {
            directionToRoomMap.put(direction.getDirectionName(), direction.getRoom());
        }
        this.hashMapLoaded = true;
    }
}
