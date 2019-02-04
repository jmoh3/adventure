package com.example;

import com.google.gson.Gson;

import java.util.Scanner;

public class AdventureGUI {

    private static final String SIEBEL_JSON  = "{\n" +
            "  \"startingRoom\": \"MatthewsStreet\",\n" +
            "  \"endingRoom\": \"Siebel1314\",\n" +
            "  \"rooms\": [\n" +
            "    {\n" +
            "      \"name\": \"MatthewsStreet\",\n" +
            "      \"description\": \"You are on Matthews, outside the Siebel Center\",\n" +
            "      \"items\": [\"coin\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"East\",\n" +
            "          \"room\": \"SiebelEntry\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"SiebelEntry\",\n" +
            "      \"description\": \"You are in the west entry of Siebel Center.  You can see the elevator, the ACM office, and hallways to the north and east.\",\n" +
            "\t  \"items\": [\"sweatshirt\", \"key\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"West\",\n" +
            "          \"room\": \"MatthewsStreet\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"Northeast\",\n" +
            "          \"room\": \"AcmOffice\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"North\",\n" +
            "          \"room\": \"SiebelNorthHallway\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"East\",\n" +
            "          \"room\": \"SiebelEastHallway\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"AcmOffice\",\n" +
            "      \"description\": \"You are in the ACM office.  There are lots of friendly ACM people.\",\n" +
            "      \"items\": [\"pizza\", \"swag\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"South\",\n" +
            "          \"room\": \"SiebelEntry\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"SiebelNorthHallway\",\n" +
            "      \"description\": \"You are in the north hallway.  You can see Siebel 1112 and the door toward NCSA.\",\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"South\",\n" +
            "          \"room\": \"SiebelEntry\"\n" +
            "        }, \n" +
            "        {\n" +
            "          \"directionName\": \"NorthEast\",\n" +
            "          \"room\": \"Siebel1112\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Siebel1112\",\n" +
            "      \"description\": \"You are in Siebel 1112.  There is space for two code reviews in this room.\",\n" +
            "      \"items\": [\"USB-C connector\", \"grading rubric\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"West\",\n" +
            "          \"room\": \"SiebelNorthHallway\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"SiebelEastHallway\",\n" +
            "      \"description\": \"You are in the east hallway.  You can see Einstein Bros' Bagels and a stairway.\",\n" +
            "      \"items\": [\"bagel\", \"coffee\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"West\",\n" +
            "          \"room\": \"SiebelEntry\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"South\",\n" +
            "          \"room\": \"Siebel1314\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"directionName\": \"Down\",\n" +
            "          \"room\": \"SiebelBasement\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Siebel1314\",\n" +
            "      \"description\": \"You are in Siebel 1314.  There are happy CS 126 students doing a code review.\",\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"North\",\n" +
            "          \"room\": \"SiebelEastHallway\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"SiebelBasement\",\n" +
            "      \"description\": \"You are in the basement of Siebel.  You see tables with students working and door to computer labs.\",\n" +
            "      \"items\": [\"pencil\"],\n" +
            "      \"directions\": [\n" +
            "        {\n" +
            "          \"directionName\": \"Up\",\n" +
            "          \"room\": \"SiebelEastHallway\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    public static void main(String[] args) {
        Gson gson = new Gson();
        Layout layout = gson.fromJson(SIEBEL_JSON, Layout.class);

        Room currentRoom = layout.getCurrentRoom();
        boolean reachedEndingRoom = false;

        while (!reachedEndingRoom) {
            System.out.println(currentRoom.getDescription());
            System.out.println("From here, you may go" + formatDirections(currentRoom));
            Scanner sc = new Scanner(System.in);
            boolean userInputIsValid = false;
            boolean shouldQuit = false;

            while (!userInputIsValid) {
                String userDirections = sc.nextLine();
                String[] decipheredInput = decipherUserInput(userDirections);

                if (decipheredInput == null) {
                    shouldQuit = true;
                    break;
                } else if (decipheredInput[0].equals("go")) {
                    try {
                        currentRoom = layout.changeRooms(decipheredInput[1]);
                        userInputIsValid = true;
                    } catch (Exception e) {
                        System.out.println("I can't " + userDirections);
                    }
                } else {
                    System.out.println("I don't understand '" + userDirections + "'");
                }
            }

            if (shouldQuit == true) {
                break;
            }
            if (currentRoom.getName().equals(layout.getEndingRoom())) {
                reachedEndingRoom = true;
            }
        }

        if (reachedEndingRoom) {
            System.out.println("You have reached your final destination");
        }
    }

    /**
     * Formats the valid directions for a room so that output for GUI is clean.
     *
     * @param room current room.
     * @return a String listing available directions with appropriate punctuation and grammar.
     */
    private static String formatDirections(Room room) {
        String output = " ";
        for (int i = 0; i < room.getDirections().length; i++) {
            if (i > 0 && room.getDirections().length > 2) {
                output = output + ", ";
            }
            if (i == room.getDirections().length - 1 && room.getDirections().length > 1) {
                output = output + "and ";
            }
            output = output + room.getDirections()[i].getDirectionName();
        }
        return output;
    }

    /**
     * Takes user input and decides what to do with it.
     *
     * @param input what the user prompts the game to do.
     * @return null if the user directs game to quit, otherwise a lowercase array of all words separated by spaces.
     */
    private static String[] decipherUserInput(String input) {
        String userInput = input.toLowerCase();

        if (userInput.equals("quit") || userInput.equals("exit")) {
            return null;
        }

        String[] splitInput = userInput.split(" ");

        return splitInput;
    }

}