package se233.teamnoonkhemii;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterMovementTest {

    @Test
    public void testPlayerShipMovement() {
        PlayerShip playerShip = new PlayerShip(100, 100, 10, 20);
        playerShip.setAngle(45);

        double initialX = playerShip.getX();
        double initialY = playerShip.getY();
        playerShip.move();

        assertNotEquals(initialX, playerShip.getX(), "PlayerShip X coordinate should change after moving");
        assertNotEquals(initialY, playerShip.getY(), "PlayerShip Y coordinate should change after moving");
    }

    @Test
    public void testAsteroidMovement() {
        Asteroid asteroid = new Asteroid(200, 200, 5, 60, 90); // Moves vertically down
        double initialY = asteroid.getY();
        asteroid.move();

        assertEquals(200, asteroid.getX(), "Asteroid X coordinate should remain the same");
        assertEquals(initialY + 5, asteroid.getY(), "Asteroid Y coordinate should increase by speed after moving");
    }


    @Test
    public void testEnemyMovement() {
        Enemy enemy = new Enemy(300, 300, 8, 25);
        double initialX = enemy.getX();
        double initialY = enemy.getY();
        enemy.move();

        assertNotEquals(initialX, enemy.getX(), "Enemy X coordinate should change after moving");
        assertNotEquals(initialY, enemy.getY(), "Enemy Y coordinate should change after moving");
    }
}
