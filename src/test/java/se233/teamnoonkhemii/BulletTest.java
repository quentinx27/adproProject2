package se233.teamnoonkhemii;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BulletTest {

    @Test
    public void testBulletMovement() {
        Bullet bullet = new PlayerShipBullet(100, 100, 5, 0, 10); // Angle 0 means it moves upwards
        double initialX = bullet.getX();
        double initialY = bullet.getY();

        bullet.move();

        assertEquals(initialX, bullet.getX(), "Bullet X coordinate should remain the same when moving upwards");
        assertTrue(bullet.getY() < initialY, "Bullet Y coordinate should decrease when moving upwards");
    }

    @Test
    public void testUltimateMovement() {
        Ultimate ultimate = new Ultimate(100, 100, 5, 0, 20); // Angle 0 means it moves upwards
        double initialX = ultimate.getX();
        double initialY = ultimate.getY();

        ultimate.move();

        assertEquals(initialX, ultimate.getX(), "Ultimate X coordinate should remain the same when moving upwards");
        assertTrue(ultimate.getY() < initialY, "Ultimate Y coordinate should decrease when moving upwards");
    }

    @Test
    public void testEnemyBulletMovement() {
        EnemyBullet enemyBullet = new EnemyBullet(100, 100, 5, 0, 10); // Angle 0 means it moves upwards
        double initialX = enemyBullet.getX();
        double initialY = enemyBullet.getY();

        enemyBullet.move();

        assertEquals(initialX, enemyBullet.getX(), "EnemyBullet X coordinate should remain the same when moving upwards");
        assertTrue(enemyBullet.getY() < initialY, "EnemyBullet Y coordinate should decrease when moving upwards");
    }

    @Test
    public void testBossBulletMovement() {
        BossBullet bossBullet = new BossBullet(100, 100, 5, 0, 10); // Angle 0 means it moves upwards
        double initialX = bossBullet.getX();
        double initialY = bossBullet.getY();

        bossBullet.move();

        assertEquals(initialX, bossBullet.getX(), "BossBullet X coordinate should remain the same when moving upwards");
        assertTrue(bossBullet.getY() < initialY, "BossBullet Y coordinate should decrease when moving upwards");
    }

    @Test
    public void testEnemyBulletCreationFromEnemy() {
        Enemy enemy = new Enemy(200, 200, 5, 20);
        EnemyBullet bullet = EnemyBullet.EnemyShoot(enemy);

        assertEquals(200, bullet.getX(), 10.0, "Enemy bullet should start near Enemy X coordinate");
        assertEquals(200, bullet.getY(), 10.0, "Enemy bullet should start near Enemy Y coordinate");
        assertEquals(enemy.getAngle(), bullet.getAngle(), "Enemy bullet angle should match Enemy angle");
    }

    @Test
    public void testBossBulletCreation() {
        Boss boss = new Boss(300, 300, 5, 50);
        BossBullet bullet = BossBullet.createFromBoss(boss, 45);

        assertEquals(316.6776695296637, bullet.getX(), 10.0, "Boss bullet should start near Boss X coordinate");
        assertEquals(300, bullet.getY(), 10.0, "Boss bullet should start near Boss Y coordinate");
    }

    @Test
    public void testBulletDeactivation() {
        Bullet bullet = new PlayerShipBullet(100, 100, 5, 0, 10);
        assertTrue(bullet.isActive(), "Bullet should be active initially");

        bullet.deactivate();
        assertFalse(bullet.isActive(), "Bullet should be inactive after deactivation");
    }

    @Test
    public void testUltimateDeactivation() {
        Ultimate ultimate = new Ultimate(100, 100, 5, 0, 20);
        assertTrue(ultimate.isActive(), "Ultimate should be active initially");

        ultimate.deactivate();
        assertFalse(ultimate.isActive(), "Ultimate should be inactive after deactivation");
    }
}
