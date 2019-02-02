package com.example;

public class Room {
    private String name;
    private String description;
    private Direction[] directions;

    public Room(String setName, String setDescription, Direction[] setDirections) {
        this.name = setName;
        this.description = setDescription;
        this.directions = setDirections;
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
}
