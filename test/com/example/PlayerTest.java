package com.example;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class PlayerTest {

    private static Player player;
    private static GameEngine gameEngine;
    private static Layout layout;

    @Before
    public void initializeNecesaryFields() {
        layout = Layout.getLayoutFromFilepath("custom.json");
    }

    @Test
    public void testCurrentRoom() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        assertEquals(layout.getStartingRoom(), player.getCurrentRoom().getName());
    }

    @Test
    public void testChangeRooms() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.changeRooms("North");
        assertEquals("Hall", player.getCurrentRoom().getName());
    }

    @Test
    public void testPickupItemsTrue() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.changeRooms("North");
        assertTrue(player.pickupItem("candlestick"));
        assertEquals("candlestick", player.getItem());
        assertEquals(0, player.getCurrentRoom().getItems().size());
    }

    @Test
    public void testPickupItemsFalse() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.changeRooms("North");
        assertFalse(player.pickupItem("sadfsadkn"));
    }

    @Test
    public void testPickupItemsFalseAlreadyHasItem() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.changeRooms("North");
        player.pickupItem("candlestick");
        player.changeRooms("West");
        assertFalse(player.pickupItem("revolver"));
    }

    @Test
    public void testHasRoomTrue() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.addKnownRoom("BilliardRoom");

        assertTrue(player.hasRoom("billiardroom"));
    }

    @Test
    public void testHasObjectTrue() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.addKnownObject("revolver");

        assertTrue(player.hasWeapon("REVOLVER"));
    }

    @Test
    public void testKnowsCharacterTrue() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.addKnownCharacter("ColonelMustard");

        assertTrue(player.characterIsInnocent("ColonelMustard"));
    }

    @Test
    public void testHasRoomFalse() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.addKnownRoom("lounge");

        assertFalse(player.hasRoom("billiardroom"));
    }

    @Test
    public void testHasObjectFalse() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.addKnownObject("revolver");

        assertFalse(player.hasWeapon("knife"));
    }

    @Test
    public void testKnowsCharacterFalse() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.addKnownCharacter("ColonelMustard");

        assertFalse(player.characterIsInnocent("MissScarlett"));
    }

    @Test
    public void testHasRoomNull() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);

        assertFalse(player.hasRoom(null));
    }

    @Test
    public void testHasObjectNull() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);

        assertFalse(player.hasWeapon(null));
    }

    @Test
    public void testKnowsCharacterNull() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);

        assertFalse(player.characterIsInnocent(null));
    }

    @Test
    public void testPickupItem() {
        gameEngine = new GameEngine(layout);
        player = new Player("ColonelMustard", gameEngine);
        player.changeRooms("North");
        player.pickupItem("candlestick");
        player.changeRooms("South");
        player.dropItem();

        assertEquals(null, player.getItem());
        assertEquals("candlestick", player.getCurrentRoom().getItems().get(0));
    }
}
