package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SpawnManager {
    private List<Asteroid> asteroids;
    private List<Enemy> enemies;
    private List<EnemyBullet> enemyBullets; // รายการสำหรับกระสุนศัตรู

    private static final int MAX_ASTEROIDS = 10;
    private static final int MAX_ENEMIES = 5;
    private static final long SPAWN_INTERVAL = 1_000_000_000L;
    private long lastSpawnTime = 0;
    private Random random = new Random();

    public SpawnManager() {
        asteroids = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>(); // สร้างรายการกระสุนศัตรู
    }

    public void updateAndSpawn(long currentTime, double screenWidth, double screenHeight) {
        if (currentTime - lastSpawnTime >= SPAWN_INTERVAL) {
            if (asteroids.size() < MAX_ASTEROIDS) spawnAsteroid(screenWidth, screenHeight);
            if (enemies.size() < MAX_ENEMIES) spawnEnemy(screenWidth, screenHeight);
            lastSpawnTime = currentTime;
        }

        updateAsteroids(screenWidth, screenHeight);
        updateEnemies(screenWidth, screenHeight);
        updateEnemyBullets(screenWidth, screenHeight); // อัปเดตการเคลื่อนที่กระสุนศัตรู
    }

    private void spawnAsteroid(double screenWidth, double screenHeight) {
        double size = 60.0;
        double speed = 1 + random.nextDouble() * 2;
        double x = random.nextDouble() * screenWidth;
        double y = random.nextDouble() * screenHeight;
        double direction = random.nextDouble() * 360;
        asteroids.add(new Asteroid(x, y, speed, size, direction));
    }

    private void spawnEnemy(double screenWidth, double screenHeight) {
        double size = 40.0;
        double speed = 1.5;
        double x = random.nextDouble() * screenWidth;
        double y = random.nextDouble() * screenHeight;

        // สร้างวัตถุ Enemy
        Enemy enemy = new Enemy(x, y, speed, size, "/Sprite Asset/EnemySheet.png");

        // เพิ่ม Enemy ลงในลิสต์ของ enemies
        enemies.add(enemy);

        // สร้างกระสุนจากวัตถุ Enemy โดยใช้เมธอด shoot
        EnemyBullet bullet = EnemyBullet.EnemyShoot(enemy);

        // เพิ่มกระสุนลงในลิสต์ของ enemyBullets
        enemyBullets.add(bullet);
    }


    private void updateAsteroids(double screenWidth, double screenHeight) {
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            asteroid.move();
            if (asteroid.isOutOfScreen(screenWidth, screenHeight)) {
                asteroidIterator.remove();
            }
        }
    }

    private void updateEnemies(double screenWidth, double screenHeight) {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move();
            // Remove the enemy if it is dead
            if (enemy.EnemyIsEliminated()) {
                enemyIterator.remove();
                PlayerShip.addScore(1);
                continue;
            }
            if (enemy.isOutOfScreen(screenWidth, screenHeight)) {
                enemyIterator.remove();
            }
        }
    }

    private void updateEnemyBullets(double screenWidth, double screenHeight) {
        Iterator<EnemyBullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            EnemyBullet bullet = bulletIterator.next();
            bullet.move();

            // ลบกระสุนเมื่อมันออกนอกหน้าจอหรือหยุดทำงานแล้ว
            if (bullet.isOutOfScreen(screenWidth, screenHeight)) {
                bulletIterator.remove();
            }
        }
    }

    public void handleEnemyBulletCollision(PlayerShip playerShip) {
        Iterator<EnemyBullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            EnemyBullet bullet = bulletIterator.next();
            if (bullet.collidesWith(playerShip)) {
                bulletIterator.remove();
                playerShip.PlayerShipTakingDamage(1);
            }
        }
    }

    public void handleBulletCollision(PlayerShipBullet bullet) {
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (bullet.collidesWith(asteroid)) {
                asteroidIterator.remove();
                asteroids.addAll(asteroid.split());
                bullet.deactivate();

                // เพิ่มคะแนนตามขนาดของ Asteroid
                if (asteroid.getSize() >= Asteroid.LARGE_SIZE_THRESHOLD) {
                    PlayerShip.addScore(2); // Asteroid ใหญ่ได้ 2 คะแนน
                } else {
                    PlayerShip.addScore(1); // Asteroid เล็กได้ 1 คะแนน
                }
                break;
            }
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (bullet.collidesWith(enemy)) {
                enemy.EnemyTakingDamage(1);
                bullet.deactivate();
                break;
            }
        }
    }

    public void handleUltimateCollision(Ultimate ultimate) {
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (ultimate.collidesWith(asteroid)) {
                asteroidIterator.remove();
                asteroids.addAll(asteroid.split());
                ultimate.deactivate();

                // เพิ่มคะแนนตามขนาดของ Asteroid
                if (asteroid.getSize() >= Asteroid.LARGE_SIZE_THRESHOLD) {
                    PlayerShip.addScore(2); // Asteroid ใหญ่ได้ 2 คะแนน
                } else {
                    PlayerShip.addScore(1); // Asteroid เล็กได้ 1 คะแนน
                }
                break;
            }
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (ultimate.collidesWith(enemy)) {
                enemy.EnemyTakingDamage(5);
                ultimate.deactivate();
                break;
            }
        }
    }

    public void handlePlayerShipCollision(PlayerShip playerShip) {
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (playerShip.collidesWith(asteroid)) {
                asteroidIterator.remove();
                asteroids.addAll(asteroid.split());
                playerShip.PlayerShipTakingDamage(1);

                // เพิ่มคะแนนตามขนาดของ Asteroid
                if (asteroid.getSize() >= Asteroid.LARGE_SIZE_THRESHOLD) {
                    PlayerShip.addScore(2); // Asteroid ใหญ่ได้ 2 คะแนน
                } else {
                    PlayerShip.addScore(1); // Asteroid เล็กได้ 1 คะแนน
                }
                break;
            }
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (playerShip.collidesWith(enemy)) {
                enemy.EnemyTakingDamage(1);// เพิ่มคะแนนเมื่อกำจัดศัตรู
                playerShip.PlayerShipTakingDamage(1); // ลดชีวิตของ PlayerShip เมื่อชนกับศัตรู
                break;
            }
        }
    }


    public void drawEntities(GraphicsContext gc) {
        asteroids.forEach(asteroid -> asteroid.draw(gc));
        enemies.forEach(enemy -> enemy.draw(gc));
        enemyBullets.forEach(bullet -> bullet.draw(gc)); // วาดกระสุนศัตรูทั้งหมด
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }
}
