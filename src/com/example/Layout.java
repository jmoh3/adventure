package com.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
     * Instantiates and returns a Layout object from a url to Layout JSON.
     *
     * @param url url from which to access the json.
     * @return Layout object.
     * @throws MalformedURLException
     * @throws IOException
     */
    public static Layout getLayoutFromURL(String url) throws MalformedURLException, IOException {
        String jsonString = Layout.readURL(url);

        Gson gson = new Gson();
        Layout layout = gson.fromJson(jsonString, Layout.class);

        return layout;
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
     * @throws NullPointerException thrown if method is given null direction.
     */
    public Room changeRooms(String direction) throws IllegalArgumentException, NullPointerException {
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

    /**
     * Private helper method that reads in a string from URL
     * Used to create Layout object.
     *
     * Code from: https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html
     *
     * @param urlStr url String of a Layout json.
     * @return text contained by URL
     * @throws MalformedURLException thrown when user provides bad URL
     * @throws IOException thrown when there is an error reading in URL
     */
    private static String readURL(String urlStr) throws MalformedURLException, IOException {
        String output = "";
        try {
            URL url = new URL(urlStr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                output = output + inputLine;
            in.close();
        } catch (Exception e) {
            if (e.getClass() == MalformedURLException.class) {
                System.out.println("Bad URL has been provided.");
                throw new MalformedURLException();
            }
            System.out.println("An error has occurred while reading from the URL.");
            throw new IOException();
        }

        return output;
    }

    /**
     * Overrides default equals (for testing purposes).
     *
     * @param other object which we are comparing.
     * @return true if Layouts contain same information, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Layout.class) {
            return false;
        }
        Layout otherLayout =  (Layout) other;
        if (this.startingRoom.equals((otherLayout.getStartingRoom()))
                && this.endingRoom.equals(otherLayout.getEndingRoom())
                && Arrays.deepEquals(this.rooms, otherLayout.getRooms())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates that current layout object has a path from startingRoom to endingRoom.
     *
     * @return true if such a path exists, false otherwise.
     */
    public boolean validateLayout() {
        Room startRoom = this.getRoom(this.getStartingRoom());
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
        if (current.getName().equals(this.getEndingRoom())) {
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
