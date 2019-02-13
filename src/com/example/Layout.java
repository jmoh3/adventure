package com.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


/**
 * A class that describes a layout of Rooms.
 */
public class Layout {

    /** The current room the user is in. */
    private Room currentRoom;

    /** Starting room of the layout. */
    private String startingRoom;
    /** Ending room of the layout. */
    private String endingRoom;
    /** Array of all Room objects contained in layout. */
    private Room[] rooms;

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
        String jsonString = Layout.readUrl(url);

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
    private static String readUrl(String urlStr) throws MalformedURLException, IOException {
        String output = "";

        try {
            URL url = new URL(urlStr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                output = output + inputLine;
            }

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

}
