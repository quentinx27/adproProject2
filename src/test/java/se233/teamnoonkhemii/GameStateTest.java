package se233.teamnoonkhemii;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    public void testPlayerLives() {
        PlayerShip playerShip = new PlayerShip(100, 100, 10, 20);

        assertEquals(5, playerShip.getPlayerShipLives(), "Initial lives should be 5");

        playerShip.PlayerShipTakingDamage(2);
        assertEquals(3, playerShip.getPlayerShipLives(), "Lives after taking 2 damage should be 3");

        playerShip.addLives(2);
        assertEquals(5, playerShip.getPlayerShipLives(), "Lives after adding 2 should be 5");

        playerShip.PlayerShipTakingDamage(5);
        assertEquals(0, playerShip.getPlayerShipLives(), "Lives after taking 5 damage should be 0");

        PlayerShip.resetPlayerShipLives();
        assertEquals(5, playerShip.getPlayerShipLives(), "Lives after reset should be 5");
    }

    @Test
    public void testPlayerScore() {
        PlayerShip playerShip = new PlayerShip(100, 100, 10, 20);

        assertEquals(0, playerShip.getScore(), "Initial score should be 0");

        PlayerShip.addScore(100);
        assertEquals(100, playerShip.getScore(), "Score after adding 100 should be 100");

        PlayerShip.addScore(50);
        assertEquals(150, playerShip.getScore(), "Score after adding 50 should be 150");

        PlayerShip.resetScore();
        assertEquals(0, playerShip.getScore(), "Score after reset should be 0");
    }

    @Test
    public void testGameOver() {
        PlayerShip playerShip = new PlayerShip(100, 100, 10, 20);

        assertTrue(playerShip.isAlive(), "Player should be alive initially");

        playerShip.PlayerShipTakingDamage(5);
        assertTrue(playerShip.isGameOver(), "Player should be in game over state after taking 5 damage");

        PlayerShip.resetPlayerShipLives();
        assertFalse(playerShip.isGameOver(), "Player should not be in game over state after resetting lives");
    }

    @Test
    public void testPlayerShipBossEliminated() {
        PlayerShip playerShip = new PlayerShip(100, 100, 10, 20);

        assertEquals(0, playerShip.getPlayerShipBossEliminated(), "Initial boss eliminations should be 0");

        PlayerShip.addPlayerShipBossEliminated(1);
        assertEquals(1, playerShip.getPlayerShipBossEliminated(), "Boss eliminations after adding 1 should be 1");

        PlayerShip.addPlayerShipBossEliminated(2);
        assertEquals(3, playerShip.getPlayerShipBossEliminated(), "Boss eliminations after adding 2 should be 3");

        PlayerShip.resetPlayerShipBossEliminated();
        assertEquals(0, playerShip.getPlayerShipBossEliminated(), "Boss eliminations after reset should be 0");
    }
}
