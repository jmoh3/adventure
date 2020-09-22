package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A class that describes a room.
 */
public class Room {

    /** room name */
    private String name;
    /** room name */
    private List<String> items;
    /** room description */
    private String description;
    /** available directions */
    private Direction[] directions;
    /** hashmap that has directions as keys and corresponding rooms as values */
    private HashMap<String, String> directionToRoomMap;
    /** makes sure the hashmap has been initialized before trying to use it */
    private boolean hashMapLoaded = false;


    /**
     * Constructor that accepts a room name, description, and directions.
     *
     * @param setName the name of the room.
     * @param setDescription the description of the room.
     * @param setDirections the directions in which the user can navigate to from the room.
     */
    public Room(String setName, String setDescription, Direction[] setDirections, List<String> setItems) {
        this.name = setName;
        this.description = setDescription;
        this.directions = setDirections;
        this.directionToRoomMap = new HashMap<String, String>();
        this.items = setItems;

        for (Direction direction : directions) {
            directionToRoomMap.put(direction.getDirectionName(), direction.getRoom());
        }
        this.hashMapLoaded = true;
    }

    /**
     * Returns room name.
     *
     * @return room name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns room description.
     *
     * @return room description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns valid directions from room.
     *
     * @return array of directions.
     */
    public Direction[] getDirections() {
        return this.directions;
    }

    /**
     * Returns items within room.
     *
     * @return List of items in that room.
     */
    public List<String> getItems() {
        return this.items;
    }

    /**
     * Removes an item from a room's item list.
     *
     * @param itemName name of item to remove.
     * @return true if remove was successful, false otherwise.
     */
    public boolean removeItem(String itemName) {
        for (String item : this.items) {
            if (itemName.equalsIgnoreCase(item)) {
                this.items.remove(item);
                return true;
            }
        }

        return false;
    }

    /**
     * Allows user to drop an item in given room.
     *
     * @param itemName item to drop.
     */
    public void dropItem(String itemName) {
        if (this.items == null) {
            this.items = new ArrayList<String>();
        }

        this.items.add(itemName);
    }

    /**
     * Gets the destination room for a given direction (with handling for bad inputs).
     *
     * @param direction direction in which the user wishes to navigate.
     * @return the room that corresponds to given direction.
     * @throws NullPointerException thrown if the method is given a null direction.
     * @throws IllegalArgumentException thrown if the method is given an invalid direction.
     */
    public String getRoomFromDirection(String direction) throws NullPointerException, IllegalArgumentException {
        if (direction == null) {
            throw new NullPointerException();
        }
        if (!hashMapLoaded) {
            this.loadHashMap();
        }
        if (!directionToRoomMap.containsKey(direction.toLowerCase())) {
            throw new IllegalArgumentException();
        }
        return directionToRoomMap.get(direction.toLowerCase());
    }

    /**
     * Private helper method that loads the hashmap to use for other functions if it hasn't been loaded already.
     */
    private void loadHashMap() {
        this.directionToRoomMap = new HashMap<String, String>();

        for (Direction direction : directions) {
            directionToRoomMap.put(direction.getDirectionName().toLowerCase(), direction.getRoom());
        }
        this.hashMapLoaded = true;
    }

    /**
     * Overrides default equals (for testing purposes).
     *
     * @param other object which we are comparing.
     * @return true if Rooms contain same information, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Room.class) {
            return false;
        }
        Room otherRoom = (Room) other;
        if (this.name.equals((otherRoom.getName()))
                && Arrays.deepEquals(this.directions, otherRoom.getDirections())
                && this.description.equals(otherRoom.getDescription())) {
            return true;
        } else {
            return false;
        }
    }
}
