package com.example;

import java.util.HashMap;

public class Layout {

    private Room currentRoom;

    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;
    private HashMap<String, Room> stringRoomHashMap;
    private boolean hashMapLoaded = false;

    public Layout(String setStartingRoom, String setEndingRoom, Room[] setRooms) {
        this.startingRoom = setStartingRoom;
        this.endingRoom = setEndingRoom;
        this.rooms = setRooms;
        this.stringRoomHashMap = new HashMap<String, Room>();

        for (Room room : this.rooms) {
            this.stringRoomHashMap.put(room.getName(), room);
        }
        this.hashMapLoaded = true;
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

    public Room getCurrentRoom()  {
        if (this.currentRoom == null) {
            this.currentRoom = this.getRoom(this.startingRoom);
        }
        return this.currentRoom;
    }

    public Room getRoom(String roomName) throws NullPointerException, IllegalArgumentException {
        if (roomName == null) {
            throw new NullPointerException();
        }
        if (!this.hashMapLoaded) {
            this.loadHashmap();
        }

        if (!stringRoomHashMap.containsKey(roomName)) {
            throw new IllegalArgumentException();
        }
        return stringRoomHashMap.get(roomName);
    }

    public boolean changeRooms(String direction) {
        Room current = this.getCurrentRoom();
        if (!this.hashMapLoaded) {
            this.loadHashmap();
        }
        try {
            String nextRoomName = current.getRoomForDirection(direction);
            this.currentRoom = this.getRoom(nextRoomName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void loadHashmap() {
        this.stringRoomHashMap = new HashMap<String, Room>();

        for (Room room : this.rooms) {
            this.stringRoomHashMap.put(room.getName(), room);
        }
    }
}
