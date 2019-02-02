package com.example;

import java.util.HashMap;

public class Layout {
    private String startingRoom;
    private String endingRoom;
    private Room[] rooms;
    private HashMap<String, Room> stringRoomHashMap;

    public Layout(String setStartingRoom, String setEndingRoom, Room[] setRooms) {
        this.startingRoom = setStartingRoom;
        this.endingRoom = setEndingRoom;
        this.rooms = setRooms;
        this.stringRoomHashMap = new HashMap<String, Room>();

        for (Room room : rooms) {
            stringRoomHashMap.put(room.getName(), room);
        }
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

    public Room getRoom(String roomName) throws NullPointerException, IllegalArgumentException {
        if (roomName == null) {
            throw new NullPointerException();
        }
        if (!stringRoomHashMap.containsKey(roomName)) {
            throw new IllegalArgumentException();
        }
        return stringRoomHashMap.get(roomName);
    }
}
