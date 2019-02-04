package com.example;

import java.util.HashMap;

/**
 * A class that describes a layout of Rooms.
 */
public class Layout {

    // The current room the user is in.
    private Room currentRoom;

    // Starting room of the layout.
    private String startingRoom;
    // Ending room of the layout.
    private String endingRoom;
    // Array of all Room objects contained in layout.
    private Room[] rooms;
    // Hashmap that maps room name to Room object.
    private HashMap<String, Room> stringRoomHashMap;
    // Boolean that checks that hashmap has been loaded before attempts to access it.
    private boolean hashMapLoaded = false;

    /**
     * Constructor for layout.
     *
     * @param setStartingRoom starting room of the layout.
     * @param setEndingRoom ending room of the layout.
     * @param setRooms array of Room objects contained in layout.
     */
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

    /**
     * Returns start room.
     *
     * @return starting room.
     */
    public String getStartingRoom() {
        return this.startingRoom;
    }

    /**
     * Returns ending room.
     *
     * @return ending room.
     */
    public String getEndingRoom() {
        return this.endingRoom;
    }

    /**
     * Returns all rooms within Layout.
     *
     * @return array of Room objects.
     */
    public Room[] getRooms() {
        return this.rooms;
    }

    /**
     * Gets the room the user is currently in.
     *
     * @return current room.
     */
    public Room getCurrentRoom()  {
        if (this.currentRoom == null) {
            this.currentRoom = this.getRoom(this.startingRoom);
        }
        return this.currentRoom;
    }

    /**
     * Gets a room contained in layout by its name.
     *
     * @param roomName name of room.
     * @return Room object corresponding to given row name.
     * @throws NullPointerException thrown if method is given a null string.
     * @throws IllegalArgumentException thrown if method is given an invalid room name.
     */
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

    /**
     * Changes the user's current room.
     *
     * @param direction direction in which the user wants to move
     * @return current room object.
     * @throws IllegalArgumentException thrown if user tries to move in an invalid direction.
     */
    public Room changeRooms(String direction) throws IllegalArgumentException {
        Room current = this.getCurrentRoom();
        if (!this.hashMapLoaded) {
            this.loadHashmap();
        }
        String nextRoomName = current.getRoomForDirection(direction);
        this.currentRoom = this.getRoom(nextRoomName);
        return this.currentRoom;
    }

    /**
     * Private helper function that loads hashmap before attempting to access it (if it hasn't been done already).
     */
    private void loadHashmap() {
        this.stringRoomHashMap = new HashMap<String, Room>();

        for (Room room : this.rooms) {
            this.stringRoomHashMap.put(room.getName(), room);
        }
    }
}
