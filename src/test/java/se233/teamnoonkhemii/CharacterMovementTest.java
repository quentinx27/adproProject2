package se233.teamnoonkhemii;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterMovementTest {

    @BeforeAll
    public static void initJFX() {
        JavaFXInitializer.initialize();
    }
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

    @Test
    public void testBossMovement() {
        Boss boss = new Boss(400, 400, 0, 100); // Boss ที่หมุนรอบศูนย์กลาง (400, 400) ด้วยรัศมี 100

        double initialX = boss.getX();
        double initialY = boss.getY();

        // บันทึกค่าตำแหน่งก่อนการเคลื่อนที่
        double centerX = boss.getX();
        double centerY = boss.getY();

        // เรียกใช้การเคลื่อนที่
        boss.move();

        double newX = boss.getX();
        double newY = boss.getY();

        // ตรวจสอบว่าตำแหน่ง X และ Y ของบอสเปลี่ยนแปลงหลังจากการเคลื่อนที่
        assertNotEquals(initialX, newX, "Boss X coordinate should change after moving in circular path");
        assertNotEquals(initialY, newY, "Boss Y coordinate should change after moving in circular path");

        // ตรวจสอบว่าบอสยังคงอยู่ในรัศมีที่กำหนดรอบจุดศูนย์กลาง
        double distanceFromCenter = Math.sqrt(Math.pow(newX - centerX, 2) + Math.pow(newY - centerY, 2));
        assertTrue(distanceFromCenter <= boss.radius + 1, "Boss should move within the defined radius");
    }
}
