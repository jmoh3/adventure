package com.example;

/**
 * A class that represents a direction.
 */
public class Direction {
    // Name of direction ("East", "West", etc).
    private String directionName;
    // Name of room it points to.
    private String room;

    /**
     * Constructor for Direction object.
     *
     * @param setDirectionName name of direction.
     * @param setRoom name of room it points to.
     */
    public Direction(String setDirectionName, String setRoom) {
        this.directionName = setDirectionName;
        this.room = setRoom;
    }

    /**
     * Returns name of direction.
     *
     * @return direction name.
     */
    public String getDirectionName() {
        return this.directionName;
    }

    /**
     * Returns name of the room direction points to.
     *
     * @return room name.
     */
    public String getRoom() {
        return this.room;
    }
}
