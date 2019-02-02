package com.example;

public class Direction {
    private String directionName;
    private String room;

    public Direction(String setDirectionName, String setRoom) {
        this.directionName = setDirectionName;
        this.room = setRoom;
    }

    public String getDirectionName() {
        return this.directionName;
    }

    public String getRoom() {
        return this.room;
    }
}
