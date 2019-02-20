package com.example;

import java.util.*;

/**
 * A five player game in which users try to guess the name of the murderer, the weapon, and the location.
 * Users can navigate through rooms, and pickup items.
 */
public class Clue {

    /** Enum describing possible actions a player could take during a turn. */
    enum Actions {
        INVALID_INPUT,
        INVALID_INSTRUCTION,
        MOVE,
        PICKUP,
        DROP,
        GUESS,
        FINAL_GUESS,
        QUIT
    }

    /** Layout being used for game. */
    private Layout layout;

    /** Names of characters in game. */
    private List<String> characterNames;

    /** Names of rooms in game. */
    private List<String> roomNames;

    /** Names of weapons in game. */
    private List<String> weaponNames;

    /** Correct murderer. */
    private String murderer;

    /** Correct murder weapon. */
    private String weapon;

    /** Correct scene of the crime. */
    private String room;

    /** Players of game (1 less than total character names). */
    private Player[] players;

    /** Permitted actions per turn. */
    private final int ALLOWED_ACTIONS_PER_TURN = 3;

    /** String describing user's options for directions. */
    private final String USER_INSTRUCTIONS = "You may change rooms (type 'go <room name>'), pickup an item (type 'pickup <item name>'), drop an item (type 'drop'), guess (type 'guess'), or final guess (type 'GUESS')";


    /**
     * Creates and sets up a Clue object from layout and list of character names.
     *
     * @param setLayout layout to be used.
     * @param charNames names of characters to be used.
     */
    public Clue(Layout setLayout, List<String> charNames) {
        this.layout = setLayout;
        this.roomNames = new ArrayList<String>();
        this.weaponNames = new ArrayList<String>();
        this.characterNames = charNames;

        setUpFromLayout(layout);

        this.players = setUpPlayers(this.characterNames);
    }

    /**
     * Sets up room name list and weapon name list from given layout.
     *
     * @param layout layout to get weapons and rooms from.
     */
    private void setUpFromLayout(Layout layout) {
        List<Room> seen = new ArrayList<Room>();

        GameEngine gameEngine = new GameEngine(layout);

        setUpHelper(gameEngine.getRoom(layout.getStartingRoom()), seen, gameEngine);
    }

    /**
     * Sets up array of Player objects from character names.
     *
     * @param characterNames names of characters.
     * @return array of instantiated Player objects.
     */
    public Player[] setUpPlayers(List<String> characterNames) {
        Player[] createdPlayers = new Player[characterNames.size() - 1];

        for (int i = 0; i < characterNames.size() - 1; i++) {
            GameEngine gameEngine = new GameEngine(layout);
            createdPlayers[i] = new Player(characterNames.get(i), gameEngine);
        }

        dealCards(this.characterNames, this.weaponNames, this.roomNames, createdPlayers);

        return createdPlayers;
    }

    /**
     * Private recursive helper function to initialize room and weapon names.
     *
     * @param current current Room.
     * @param seen Rooms that we have already seen.
     * @return true if the ending room has been reached.
     */
    private void setUpHelper(Room current, List<Room> seen, GameEngine gameEngine) {
        if (current.getName().equals(layout.getEndingRoom())) {
            return;
        }
        seen.add(current);

        this.roomNames.add(current.getName());

        if (current.getItems() != null) {
            for (String item : current.getItems()) {
                this.weaponNames.add(item);
            }
        }

        for (Direction direction : current.getDirections()) {
            Room nextRoom = gameEngine.getRoom(current.getRoomFromDirection(direction.getDirectionName()));
            if (seen.contains(nextRoom)) {
                continue;
            }
            setUpHelper(nextRoom, seen, gameEngine);
        }
    }

    public void playGame() {
        int playerIndex = 0;

        boolean continueGame = true;

        while (continueGame) {
            playerIndex = (playerIndex + 1) % players.length;
            System.out.println("=========================================================");
            System.out.println("It is " + players[playerIndex].getName() + "'s turn!");
            continueGame = playTurn(playerIndex, ALLOWED_ACTIONS_PER_TURN);
        }
    }

    /**
     * Allows player to take one turn. Will recurse until all their allowed actions are exhausted.
     *
     * @param playerIndex index corresponding to player whose turn iti s.
     * @param allowedActionsLeft number of actions the player has.
     */
    public boolean playTurn(int playerIndex, int allowedActionsLeft) {
        if (allowedActionsLeft == 0) {
            return true;
        }

        System.out.println();
        System.out.println("You have " + allowedActionsLeft + " actions left.");

        System.out.println();
        System.out.println(USER_INSTRUCTIONS);

        Room currentRoom = players[playerIndex].getCurrentRoom();

        System.out.println(currentRoom.getDescription());
        System.out.println("From here, you may go" + PlayAdventure.formatDirections(currentRoom));

        if (currentRoom.getItems() != null && currentRoom.getItems().size() > 0) {
            System.out.println("You may pickup" + PlayAdventure.formatItems(currentRoom));
        }

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        Actions actionChosen = handleUserDirections(input, playerIndex);

        if (actionChosen == Actions.INVALID_INSTRUCTION) {
            System.out.println("I can't " + input + ". Please try again.");
            return playTurn(playerIndex, allowedActionsLeft);
        } else if (actionChosen == Actions.INVALID_INPUT) {
            System.out.println("I don't understand '" + input + "'. Please try again.");
            return playTurn(playerIndex, allowedActionsLeft);
        } else if (actionChosen == Actions.FINAL_GUESS) {
            System.out.println("Game Over!");
            return false;
        } else if (actionChosen == Actions.QUIT) {
            System.out.println("Game Over!");
            return false;
        }

        return playTurn(playerIndex, allowedActionsLeft - 1);
    }

    /**
     * Given a player index and that player's directions, returns an enum describing the actions the player took.
     *
     * @param userDirections String input from player describing what actions in game to take.
     * @param playerIndex index corresponding to player whose turn it is.
     * @return Actions enum describing what the player did.
     */
    public Actions handleUserDirections(final String userDirections, int playerIndex) {
        if (userDirections == null || userDirections.length() == 0) {
            return Actions.INVALID_INPUT;
        }

        if (userDirections.equals("GUESS")) {
            handleFinalGuess(playerIndex);
            return Actions.FINAL_GUESS;
        }

        String directions = userDirections.toLowerCase();
        String[] directionSplit = directions.split(" ");

        if (directionSplit.length == 0) {
            return Actions.INVALID_INPUT;
        }

        if (directionSplit[0].equals("go")) {
            try {
                players[playerIndex].changeRooms(directionSplit[1]);
                return Actions.MOVE;
            } catch (Exception e) {
                return Actions.INVALID_INSTRUCTION;
            }
        }

        if (directionSplit[0].equals("drop")) {
            boolean success = players[playerIndex].dropItem();
            if (success) {
                return Actions.DROP;
            } else {
                return Actions.INVALID_INSTRUCTION;
            }
        }

        if (directionSplit[0].equals("pickup")) {
            boolean success = players[playerIndex].pickupItem(directionSplit[1]);
            if (success) {
                return Actions.PICKUP;
            } else {
                return Actions.INVALID_INSTRUCTION;
            }
        }

        if (directionSplit[0].equals("quit")) {
            return Actions.QUIT;
        }

        if (directionSplit[0].equals("guess") && players[playerIndex].getItem() != null) {
            handleGuess(playerIndex);
            return Actions.GUESS;
        } else if (directionSplit[0].equals("guess")) {
            System.out.println("You may not guess unless you're currently carrying an item.");
        }

        return Actions.INVALID_INPUT;
    }

    /**
     * Allows user to type in guesses that will be cross checked with the information of the player to their left.
     */
    public void handleGuess(int playerIndex) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Who do you think is the murderer?");

        String murdererGuess = scanner.nextLine();

        boolean goodInput = handleMurdererGuess(murdererGuess);

        while (!goodInput) {
            System.out.println("Invalid character name. Please try again.");
            murdererGuess = scanner.nextLine();
            goodInput = handleMurdererGuess(murdererGuess);
        }

        String weaponGuess = players[playerIndex].getItem();
        System.out.println("Guessing " + weaponGuess  + " as the murder weapon.");

        String roomGuess = players[playerIndex].getCurrentRoom().getName();
        System.out.println("Guessing " + roomGuess + " as the scene of the crime.");

        boolean[] response = guess(murdererGuess, weaponGuess, roomGuess, playerIndex);

        System.out.println(formatGuessResponse(murdererGuess, weaponGuess, roomGuess, playerIndex, response));
    }

    /**
     * Allows user to make final guess.
     */
    public void handleFinalGuess(int playerIndex) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Who do you think is the murderer?");

        String murdererGuess = scanner.nextLine();

        boolean goodInput = handleMurdererGuess(murdererGuess);

        while (!goodInput) {
            System.out.println("Invalid character name. Please try again.");
            murdererGuess = scanner.nextLine();
            goodInput = handleMurdererGuess(murdererGuess);
        }

        String weaponGuess = players[playerIndex].getItem();
        System.out.println("Guessing " + weaponGuess  + " as the murder weapon.");

        String roomGuess = players[playerIndex].getCurrentRoom().getName();
        System.out.println("Guessing " + roomGuess + " as the scene of the crime.");

        boolean guessCorrect = finalGuess(murdererGuess, weaponGuess, roomGuess);

        if (guessCorrect) {
            System.out.println(players[playerIndex].getName() + " is correct! It was " + this.murderer + " who committed the murder with the " + this.weapon + " in the " + this.room);
        } else {
            System.out.println(players[playerIndex].getName() + " was wrong! It was " + this.murderer + " who committed the murder with the " + this.weapon + " in the " + this.room);
        }
    }

    /**
     * Determines whether input for murderer guess is valid or not.
     *
     * @param guess player's guess.
     * @return true if valid character name, false otherwise.
     */
    public boolean handleMurdererGuess(String guess) {
        if (guess == null) {
            return false;
        }

        for (String character : this.characterNames) {
            if (character.equalsIgnoreCase(guess)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns information gained from cross check of guess by player to left.
     *
     * @param murdererGuess current player's murderer guess.
     * @param weaponGuess current player's weapon guess.
     * @param roomGuess current player's room guess.
     * @param playerIndex index corresponding to player whose turn it is.
     * @return boolean array corresponding to whether murderer guess is innocent, weapon guess is wrong, and room guess is wrong.
     */
    public boolean[] guess(String murdererGuess, String weaponGuess, String roomGuess, int playerIndex) {
        boolean[] disprovedGuesses = new boolean[3];

        Player playerToLeft = players[(playerIndex + 1) % players.length];

        disprovedGuesses[0] = playerToLeft.characterIsInnocent(murdererGuess);
        disprovedGuesses[1] = playerToLeft.hasObject(weaponGuess);
        disprovedGuesses[2] = playerToLeft.hasRoom(roomGuess);

        return disprovedGuesses;
    }

    /**
     * Formats the response to a player's guess.
     *
     * @param murdererGuess current player's murderer guess.
     * @param weaponGuess current player's weapon guess.
     * @param roomGuess current player's room guess.
     * @param playerIndex index corresponding to player whose turn it is.
     * @param response boolean array of responses to guess.
     * @return String displaying feedback on guesses.
     */
    public String formatGuessResponse(String murdererGuess, String weaponGuess, String roomGuess, int playerIndex, boolean[] response) {
        String output = "";

        if (response[0]) {
            output += murdererGuess + " is innocent!";
        } else {
            output += "No new character information.";
        }

        if (response[1]) {
            output += "\n" + weaponGuess + " is not the murder weapon.";
        } else {
            output += "\n" + "No new weapon information.";
        }

        if (response[2]) {
            output += "\n" + roomGuess + " is not the scene of the crime.";
        } else {
            output += "\n" + "No new room information.";
        }

        return output;
    }

    /**
     * Big final guess for correct murder information, will terminate game.
     *
     * @param murdererGuess current player's murderer guess.
     * @param weaponGuess current player's weapon guess.
     * @param roomGuess current player's room guess.
     * @return true if guess was correct, false otherwise.
     */
    public boolean finalGuess(String murdererGuess, String weaponGuess, String roomGuess) {
        return (murdererGuess.equals(murderer) && weaponGuess.equals(weapon) && roomGuess.equals(room));
    }

    /**
     * Getter for room names.
     *
     * @return list of room names.
     */
    public List<String> getRoomNames() {
        return this.roomNames;
    }

    /**
     * Getter for weapon names.
     *
     * @return list of weapon names.
     */
    public List<String> getWeaponNames() {
        return this.weaponNames;
    }

    /**
     * Getter for room names.
     *
     * @return list of room names.
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Getter for correct murderer.
     *
     * @return name of murderer.
     */
    public String getMurderer() {
        return this.murderer;
    }

    /**
     * Getter for correct weapon name.
     *
     * @return name of murder weapon.
     */
    public String getWeapon() {
        return this.weapon;
    }

    /**
     * Getter for correct room name.
     *
     * @return name of correct room where murder took place.
     */
    public String getRoom() {
        return this.room;
    }

    /**
     * Deals all the "cards" for clue game - gives out pieces of information on known innocent character names,
     * incorrect weapon names, and incorrect room names.
     *
     * @param setCharacterNames character names.
     * @param setWeaponNames weapon names.
     * @param setRoomNames room names.
     * @param players list of players.
     */
    public void dealCards(List<String> setCharacterNames, List<String> setWeaponNames, List<String> setRoomNames, Player[] players) {
        List<String> characterNames = new ArrayList<String>(setCharacterNames);
        List<String> weaponNames = new ArrayList<String>(setWeaponNames);
        List<String> roomNames = new ArrayList<String>(setRoomNames);

        Random rand = new Random();

        int randSelection = rand.nextInt(characterNames.size());
        this.murderer = characterNames.remove(randSelection);
        Collections.shuffle(characterNames);

        randSelection = rand.nextInt(weaponNames.size());
        this.weapon = weaponNames.remove(randSelection);
        Collections.shuffle(weaponNames);

        randSelection = rand.nextInt(roomNames.size());
        this.room = roomNames.remove(randSelection);
        Collections.shuffle(roomNames);

        int playerIndex = 0;

        while (!roomNames.isEmpty() || !characterNames.isEmpty() || !weaponNames.isEmpty()) {
            playerIndex = playerIndex % players.length;
            if (!characterNames.isEmpty()) {
                players[playerIndex].addKnownCharacter(characterNames.remove(0));
            }
            if (!weaponNames.isEmpty()) {
                players[playerIndex].addKnownObject(weaponNames.remove(0));
            }
            if (!roomNames.isEmpty()) {
                players[playerIndex].addKnownRoom(roomNames.remove(0));
            }
            playerIndex++;
        }
    }

}
