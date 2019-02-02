package com.example;

public class Layout {
    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;

    public Layout(String setStartingRoom, String setEndingRoom, Room[] setRooms) {
        this.startingRoom = setStartingRoom;
        this.endingRoom = setEndingRoom;
        this.rooms = setRooms;
    }

    public String getStartingRoom() {
        return this.startingRoom;
    }

    public String getEndingRoom() {
        return this.endingRoom;
    }

    public Room[] getRooms() {
        return this.rooms;
    }
}
