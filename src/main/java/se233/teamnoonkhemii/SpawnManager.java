package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SpawnManager {

    private static final Logger logger = Logger.getLogger(SpawnManager.class.getName());

    private List<Asteroid> asteroids;
    private List<Enemy> enemies;
    private List<Boss> bosses; // รายการสำหรับเก็บบอส
    private List<EnemyBullet> enemyBullets; // รายการสำหรับกระสุนศัตรู
    private List<BossBullet> bossBullets; // รายการสำหรับกระสุนบอส

    private static final int MAX_ASTEROIDS = 10;
    private static final int MAX_ENEMIES = 5;
    private static final long SPAWN_INTERVAL = 1_000_000_000L;
    private static final long BOSS_SPAWN_INTERVAL = 40_000_000_000L; // 40 วินาที
    private long lastSpawnTime = 0;
    private long lastBossSpawnTime = System.nanoTime(); // เริ่มนับเวลาการเกิดบอสเมื่อเกมเริ่ม
    private boolean bossActive = false; // ใช้ตรวจสอบว่าบอสกำลังแอคทีฟอยู่หรือไม่
    private Random random = new Random();

    public SpawnManager() {
        asteroids = new ArrayList<>();
        enemies = new ArrayList<>();
        bosses = new ArrayList<>(); // สร้างรายการบอส
        enemyBullets = new ArrayList<>(); // สร้างรายการกระสุนศัตรู
        bossBullets = new ArrayList<>(); // สร้างรายการกระสุนบอส
    }
    public long getNextBossSpawnTime() {
        // ถ้าบอสแอคทีฟอยู่ ให้ส่งคืนเวลาที่เหลือสำหรับการ spawn บอสครั้งถัดไป
        if (bossActive) {
            return lastBossSpawnTime + BOSS_SPAWN_INTERVAL;
        } else {
            // ถ้าไม่มีบอสอยู่ ให้ส่งคืนเวลาที่เหลืออยู่จากเวลาปัจจุบันถึงเวลาที่บอสจะเกิด
            return lastBossSpawnTime + BOSS_SPAWN_INTERVAL;
        }
    }

    public void updateAndSpawn(long currentTime, double screenWidth, double screenHeight) {
        // สร้างบอสทุก ๆ 40 วินาทีถ้าไม่มีบอสอยู่
        if (!bossActive && currentTime - lastBossSpawnTime >= BOSS_SPAWN_INTERVAL) {
            spawnBoss(screenWidth, screenHeight);
            lastBossSpawnTime = currentTime;
        }

        // สร้าง Enemy และ Asteroid เมื่อไม่มีบอสแอคทีฟอยู่
        if (!bossActive && currentTime - lastSpawnTime >= SPAWN_INTERVAL) {
            if (asteroids.size() < MAX_ASTEROIDS) spawnAsteroid(screenWidth, screenHeight);
            if (enemies.size() < MAX_ENEMIES) spawnEnemy(screenWidth, screenHeight);
            lastSpawnTime = currentTime;
        }

        updateAsteroids(screenWidth, screenHeight);
        updateEnemies(screenWidth, screenHeight);
        updateBosses(screenWidth, screenHeight); // อัปเดตสถานะของบอส
        updateEnemyBullets(screenWidth, screenHeight);
        updateBossBullets(screenWidth, screenHeight); // อัปเดตการเคลื่อนที่ของกระสุนบอส
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

    private void spawnBoss(double screenWidth, double screenHeight) {
        double size = 100.0; // ขนาดของบอส
        double speed = 0.0; // ความเร็วเป็น 0 เพื่อให้บอสไม่เคลื่อนที่
        double x = screenWidth / 2 - size / 2 +24; // เริ่มที่ตำแหน่งกลางจอในแนวนอน
        double y = screenHeight / 2 - size / 2; // เริ่มที่ตำแหน่งกลางจอในแนวตั้ง

        // ลบศัตรูและอุกกาบาตทั้งหมดเมื่อบอสเกิด
        enemies.clear();
        asteroids.clear();

        // สร้างบอส
        Boss boss = new Boss(x, y, speed, size, 10); // กำหนดจำนวนชีวิตเป็น 10
        bosses.add(boss);
        bossActive = true; // ตั้งสถานะว่า บอสกำลังแอคทีฟ
        logger.warning("Boss already spawned at the center of the screen!");
    }


    private void updateBosses(double screenWidth, double screenHeight) {
        Iterator<Boss> bossIterator = bosses.iterator();
        while (bossIterator.hasNext()) {
            Boss boss = bossIterator.next();
            boss.move();

            // ตรวจสอบว่าบอสตายหรือไม่
            if (!boss.isAlive()) {
                bossIterator.remove();
                PlayerShip.addScore(2); // เพิ่มคะแนนเมื่อบอสถูกกำจัด
                PlayerShip.addPlayerShipBossEliminated(1);
                bossActive = false; // ตั้งสถานะว่าบอสไม่แอคทีฟแล้ว
                lastBossSpawnTime = System.nanoTime(); // เริ่มนับเวลาสำหรับการเกิดบอสครั้งถัดไป

                // ลบกระสุนบอสทั้งหมดเมื่อบอสถูกกำจัด
                bossBullets.clear();
                logger.warning("Boss already Eliminated");

                continue;
            }

            // บอสยิงกระสุน
            if (boss.isAlive()) {
                BossShoot(boss);
            }
        }
    }

    private void BossShoot(Boss boss) {
        long currentTime = System.nanoTime();
        // ยิงกระสุนออกมาโดยใช้เมธอดของบอส
        boss.BossShoot(currentTime);
        // เพิ่มกระสุนที่ยิงออกมาลงในลิสต์ของกระสุนบอส
        boss.bullets.forEach(bullet -> {
            if (!bossBullets.contains(bullet)) {
                bossBullets.add(bullet);
            }
        });
    }

    private void updateBossBullets(double screenWidth, double screenHeight) {
        Iterator<BossBullet> bulletIterator = bossBullets.iterator();
        while (bulletIterator.hasNext()) {
            BossBullet bullet = bulletIterator.next();
            bullet.move();

            // ลบกระสุนเมื่อมันออกนอกหน้าจอหรือหยุดทำงานแล้ว
            if (bullet.isOutOfScreen(screenWidth, screenHeight)) {
                bulletIterator.remove();
            }
        }
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

        // ตรวจสอบว่ากระสุนของผู้เล่นชนกับบอสหรือไม่
        for (Boss boss : bosses) {
            if (bullet.collidesWith(boss)) {
                boss.BossTakingDamage(1); // ลดชีวิตของบอสเมื่อถูกกระสุนผู้เล่น
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

        // ตรวจสอบว่าการโจมตี Ultimate ชนกับบอสหรือไม่
        for (Boss boss : bosses) {
            if (ultimate.collidesWith(boss)) {
                boss.BossTakingDamage(5); // บอสได้รับความเสียหายจากการโจมตี Ultimate
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
                enemy.EnemyTakingDamage(1); // เพิ่มคะแนนเมื่อกำจัดศัตรู
                playerShip.PlayerShipTakingDamage(1); // ลดชีวิตของ PlayerShip เมื่อชนกับศัตรู
                break;
            }
        }

        // ตรวจสอบว่าผู้เล่นชนกับบอสหรือไม่
        for (Boss boss : bosses) {
            if (playerShip.collidesWith(boss)) {
                playerShip.PlayerShipTakingDamage(2); // บอสทำความเสียหายเมื่อชนกับผู้เล่น
                boss.BossTakingDamage(1); // บอสได้รับความเสียหายเล็กน้อยเมื่อชนกับผู้เล่น
                break;
            }
        }
    }

    public void handleBossBulletCollision(PlayerShip playerShip) {
        Iterator<BossBullet> bulletIterator = bossBullets.iterator();
        while (bulletIterator.hasNext()) {
            BossBullet bullet = bulletIterator.next();

            // ตรวจสอบว่ากระสุนบอสชนกับยานของผู้เล่น
            if (bullet.collidesWith(playerShip)) {
                bulletIterator.remove();
                playerShip.PlayerShipTakingDamage(2); // บอสทำความเสียหายมากกว่าศัตรูปกติ
            }

            // ตรวจสอบว่ากระสุนบอสชนกับบอสเอง (ถ้ามีเหตุการณ์นี้เกิดขึ้น)
            for (Boss boss : bosses) {
                if (bullet.collidesWith(boss)) {
                    bulletIterator.remove();
                    break;
                }
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

    public void drawEntities(GraphicsContext gc) {
        asteroids.forEach(asteroid -> asteroid.draw(gc));
        enemies.forEach(enemy -> enemy.draw(gc));
        enemyBullets.forEach(bullet -> bullet.draw(gc));
        bosses.forEach(boss -> boss.draw(gc)); // วาดบอส
        bossBullets.forEach(bullet -> bullet.draw(gc)); // วาดกระสุนบอส
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Boss> getBosses() {
        return bosses;
    }

    public List<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public List<BossBullet> getBossBullets() {
        return bossBullets;
    }
}
