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

    /**
     * Overrides default equals (for testing purposes).
     *
     * @param other object which we are comparing.
     * @return true if Directions contain same information, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Direction.class) {
            return false;
        }
        Direction otherDirection = (Direction) other;
        if (this.directionName.equals(((Direction) other).getDirectionName())
                && this.room.equals(otherDirection.getRoom())) {
            return true;
        } else {
            return false;
        }
    }
}
