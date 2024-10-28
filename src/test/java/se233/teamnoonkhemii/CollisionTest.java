package se233.teamnoonkhemii;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import java.util.ArrayList;

public class CollisionTest {

    private SpawnManager spawnManager;
    private PlayerShip playerShip;


    @BeforeAll
    public static void initJFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    public void setup() {
        spawnManager = new SpawnManager();
        playerShip = new PlayerShip(100, 100, 5, 20);

        // Mock initial state for asteroids, enemies, and bullets
        SpawnManager.asteroids = new ArrayList<>();
        SpawnManager.enemies = new ArrayList<>();
        SpawnManager.enemyBullets = new ArrayList<>();
        SpawnManager.bossBullets = new ArrayList<>();
        SpawnManager.bosses = new ArrayList<>();
        SpawnManager.heals = new ArrayList<>();
    }

    @Test
    public void testHandleBulletCollisionWithAsteroid() {
        PlayerShipBullet bullet = new PlayerShipBullet(100, 100, 5, 0, 10);
        Asteroid asteroid = new Asteroid(100, 100, 5, 60, 0); // Large asteroid

        SpawnManager.asteroids.add(asteroid);

        spawnManager.handleBulletCollision(bullet);

        assertTrue(SpawnManager.asteroids.size() > 1, "Asteroid should split into smaller asteroids upon being hit by bullet.");
        assertFalse(bullet.isActive(), "Bullet should be deactivated after colliding with an asteroid.");
        assertEquals(2, playerShip.getScore(), "Player should receive 2 points for hitting a large asteroid.");
    }

    @Test
    public void testHandleBulletCollisionWithEnemy() {
        PlayerShipBullet bullet = new PlayerShipBullet(100, 100, 5, 0, 10);
        Enemy enemy = new Enemy(100, 100, 5, 20);

        SpawnManager.enemies.add(enemy);

        spawnManager.handleBulletCollision(bullet);

        assertEquals(1, SpawnManager.enemies.size(), "Enemy should still be in the list after taking damage.");
        assertEquals(1, Enemy.getEnemylives(), "Enemy should have taken damage from the bullet.");
        assertFalse(bullet.isActive(), "Bullet should be deactivated after colliding with an enemy.");
    }

    @Test
    public void testHandleBulletCollisionWithBoss() {
        PlayerShipBullet bullet = new PlayerShipBullet(100, 100, 5, 0, 10);
        Boss boss = new Boss(100, 100, 5, 50);

        SpawnManager.bosses.add(boss);

        spawnManager.handleBulletCollision(bullet);

        assertEquals(9, Boss.getBosslives(), "Boss should take damage from the bullet.");
        assertFalse(bullet.isActive(), "Bullet should be deactivated after colliding with a boss.");
    }

    @Test
    public void testHandlePlayerShipCollisionWithAsteroid() {
        Asteroid asteroid = new Asteroid(100, 100, 5, 60, 0); // Large asteroid
        SpawnManager.asteroids.add(asteroid);

        assertEquals(5, playerShip.getPlayerShipLives(), "PlayerShip should start with 5 lives.");

        spawnManager.handlePlayerShipCollision(playerShip);

        assertTrue(SpawnManager.asteroids.size() > 1, "Asteroid should split into smaller asteroids upon colliding with the PlayerShip.");
        assertEquals(4, playerShip.getPlayerShipLives(), "PlayerShip should lose 1 life when colliding with an asteroid.");
        assertEquals(2, playerShip.getScore(), "Player should receive 2 points for hitting a large asteroid.");
    }

    @Test
    public void testHandlePlayerShipCollisionWithEnemy() {
        Enemy enemy = new Enemy(100, 100, 5, 20);
        SpawnManager.enemies.add(enemy);

        assertEquals(5, playerShip.getPlayerShipLives(), "PlayerShip should start with 5 lives.");

        spawnManager.handlePlayerShipCollision(playerShip);

        assertEquals(4, playerShip.getPlayerShipLives(), "PlayerShip should lose 1 life when colliding with an enemy.");
        assertEquals(1, Enemy.getEnemylives(), "Enemy should have taken damage from the collision.");
    }

    @Test
    public void testHandlePlayerShipCollisionWithBoss() {
        Boss boss = new Boss(100, 100, 5, 50);
        SpawnManager.bosses.add(boss);

        assertEquals(5, playerShip.getPlayerShipLives(), "PlayerShip should start with 5 lives.");

        spawnManager.handlePlayerShipCollision(playerShip);

        assertEquals(3, playerShip.getPlayerShipLives(), "PlayerShip should lose 2 lives when colliding with a boss.");
        assertEquals(9, Boss.getBosslives(), "Boss should take 1 damage from colliding with the PlayerShip.");
    }

    @Test
    public void testHandlePlayerShipCollisionWithHeal() {
        Heal heal = new Heal(100, 100, 10);
        SpawnManager.heals.add(heal);

        assertEquals(5, playerShip.getPlayerShipLives(), "PlayerShip should start with 5 lives.");

        spawnManager.handlePlayerShipCollision(playerShip);

        assertEquals(6, playerShip.getPlayerShipLives(), "PlayerShip should gain 1 life when collecting a Heal.");
        assertTrue(SpawnManager.heals.isEmpty(), "Heal should be removed after being collected by the PlayerShip.");
    }
    @Test
    public void testHandleUltimateCollisionWithAsteroid() {
        Ultimate ultimate = new Ultimate(100, 100, 5, 0, 10);
        Asteroid asteroid = new Asteroid(100, 100, 5, 60, 0); // Large asteroid

        SpawnManager.asteroids.add(asteroid);

        spawnManager.handleUltimateCollision(ultimate);

        assertTrue(SpawnManager.asteroids.size() > 1, "Asteroid should split into smaller asteroids upon being hit by Ultimate.");
        assertFalse(ultimate.isActive(), "Ultimate should be deactivated after colliding with an asteroid.");
        assertEquals(2, playerShip.getScore(), "Player should receive 2 points for hitting a large asteroid.");
    }

    @Test
    public void testHandleUltimateCollisionWithEnemy() {
        Ultimate ultimate = new Ultimate(100, 100, 5, 0, 10);
        Enemy enemy = new Enemy(100, 100, 5, 20);

        SpawnManager.enemies.add(enemy);

        spawnManager.handleUltimateCollision(ultimate);

        assertEquals(1, SpawnManager.enemies.size(), "Enemy should still be in the list after taking damage.");
        assertEquals(-3, Enemy.getEnemylives(), "Enemy should have taken damage from the Ultimate.");
        assertFalse(ultimate.isActive(), "Ultimate should be deactivated after colliding with an enemy.");
    }

    @Test
    public void testHandleUltimateCollisionWithBoss() {
        Ultimate ultimate = new Ultimate(100, 100, 5, 0, 10);
        Boss boss = new Boss(100, 100, 5, 50);

        SpawnManager.bosses.add(boss);

        spawnManager.handleUltimateCollision(ultimate);

        assertEquals(5, Boss.getBosslives(), "Boss should take damage from the Ultimate.");
        assertFalse(ultimate.isActive(), "Ultimate should be deactivated after colliding with a boss.");
    }

    @Test
    public void testHandleEnemyBulletCollision() {
        EnemyBullet bullet = new EnemyBullet(100, 100, 5, 0, 5);
        SpawnManager.enemyBullets.add(bullet);

        assertEquals(5, playerShip.getPlayerShipLives(), "PlayerShip should start with 5 lives.");

        spawnManager.handleEnemyBulletCollision(playerShip);

        assertTrue(SpawnManager.enemyBullets.isEmpty(), "Enemy bullet should be removed after hitting the player.");
        assertEquals(4, playerShip.getPlayerShipLives(), "PlayerShip should lose 1 life when hit by an enemy bullet.");
    }

    @Test
    public void testHandleBossBulletCollision() {
        BossBullet bossBullet = new BossBullet(100, 100, 5, 0, 5);
        Boss boss = new Boss(200, 200, 5, 50);
        SpawnManager.bosses.add(boss);
        SpawnManager.bossBullets.add(bossBullet);

        assertEquals(5, playerShip.getPlayerShipLives(), "PlayerShip should start with 5 lives.");

        spawnManager.handleBossBulletCollision(playerShip);

        assertTrue(SpawnManager.bossBullets.isEmpty(), "Boss bullet should be removed after hitting the player.");
        assertEquals(3, playerShip.getPlayerShipLives(), "PlayerShip should lose 2 lives when hit by a boss bullet.");
    }

    @Test
    public void testHandleBossBulletCollisionWithBoss() {
        BossBullet bossBullet = new BossBullet(200, 200, 5, 0, 5);
        Boss boss = new Boss(200, 200, 5, 50);
        SpawnManager.bosses.add(boss);
        SpawnManager.bossBullets.add(bossBullet);

        spawnManager.handleBossBulletCollision(playerShip);

        assertTrue(SpawnManager.bossBullets.isEmpty(), "Boss bullet should be removed if it collides with a boss.");
    }
}
