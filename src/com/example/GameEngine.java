package com.example;

import java.util.ArrayList;
import java.util.HashMap;

public class GameEngine {

    /** Layout we are using for GameEngine */
    private Layout layout;

    /** Room the user is currently in. */
    private Room currentRoom;

    /** Hashmap that maps room name to Room object. */
    private HashMap<String, Room> stringRoomHashMap;

    public GameEngine(Layout setLayout) {
        this.layout = setLayout;
        this.stringRoomHashMap = this.loadHashmap(layout.getRooms());
        this.currentRoom = getRoom(layout.getStartingRoom());
    }

    /**
     * Gets the room the user is currently in.
     *
     * @return current room.
     */
    public Room getCurrentRoom()  {
        return this.currentRoom;
    }

    /**
     * Changes the user's current room.
     *
     * @param direction direction in which the user wants to move
     * @return current room object.
     * @throws IllegalArgumentException thrown if user tries to move in an invalid direction.
     * @throws NullPointerException thrown if method is given null direction.
     */
    public Room changeRooms(String direction) throws IllegalArgumentException, NullPointerException {
        Room current = this.currentRoom;

        String nextRoomName = current.getRoomForDirection(direction);
        this.currentRoom = getRoom(nextRoomName);
        return this.currentRoom;
    }

    /**
     * Private helper function that loads hashmap before attempting to access it (if it hasn't been done already).
     */
    public HashMap<String, Room> loadHashmap(Room[] roomList) {
        HashMap<String, Room> roomHashMap = new HashMap<String, Room>();

        for (Room room : layout.getRooms()) {
            roomHashMap.put(room.getName(), room);
        }

        return roomHashMap;
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

        if (!stringRoomHashMap.containsKey(roomName)) {
            throw new IllegalArgumentException();
        }
        return stringRoomHashMap.get(roomName);
    }

    /**
     * Validates that current layout object has a path from startingRoom to endingRoom.
     *
     * @return true if such a path exists, false otherwise.
     */
    public boolean validateLayout() {
        Room startRoom = this.getRoom(layout.getStartingRoom());
        boolean reachedEnd = false;
        ArrayList<Room> seen = new ArrayList<Room>();

        return reachedEnd(startRoom, seen);
    }

    /**
     * Private recursive helper function to determine whether a path has reached an end.
     *
     * @param current current Room.
     * @param seen Rooms that we have already seen.
     * @return true if the ending room has been reached.
     */
    private boolean reachedEnd(Room current, ArrayList<Room> seen) {
        if (current.getName().equals(layout.getEndingRoom())) {
            return true;
        }
        seen.add(current);
        boolean ended = false;
        for (Direction direction : current.getDirections()) {
            Room nextRoom = this.getRoom(current.getRoomForDirection(direction.getDirectionName()));
            if (seen.contains(nextRoom)) {
                continue;
            }
            if (reachedEnd(nextRoom, seen)) {
                return true;
            }
        }
        return false;
    }

}
