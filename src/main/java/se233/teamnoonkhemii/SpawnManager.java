package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SpawnManager {

    private static final Logger logger = Logger.getLogger(SpawnManager.class.getName());

    public static List<Asteroid> asteroids;
    public static List<Enemy> enemies;
    public static List<Boss> bosses; // รายการสำหรับเก็บบอส
    public static List<EnemyBullet> enemyBullets; // รายการสำหรับกระสุนศัตรู
    public static List<BossBullet> bossBullets; // รายการสำหรับกระสุนบอส
    public static List<Heal> heals;
    public static List<Explosion> explosions;

    private static final int MAX_ASTEROIDS = 10;
    private static final int MAX_ENEMIES = 5;
    private static final int MAX_HEALS = 2; // จำกัดจำนวน Heal ที่มีบนหน้าจอ
    private static final long SPAWN_INTERVAL = 1_000_000_000L;
    private static final long BOSS_SPAWN_INTERVAL = 40_000_000_000L; // 40 วินาที
    private long lastSpawnTime = 0;
    private static long lastBossSpawnTime = System.nanoTime(); // เริ่มนับเวลาการเกิดบอสเมื่อเกมเริ่ม
    private static boolean bossActive = false; // ใช้ตรวจสอบว่าบอสกำลังแอคทีฟอยู่หรือไม่
    private final Random random = new Random();


    public SpawnManager() {
        asteroids = new ArrayList<>();
        enemies = new ArrayList<>();
        bosses = new ArrayList<>(); // สร้างรายการบอส
        enemyBullets = new ArrayList<>(); // สร้างรายการกระสุนศัตรู
        bossBullets = new ArrayList<>(); // สร้างรายการกระสุนบอส
        heals = new ArrayList<>();
        explosions = new ArrayList<>();
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
            if (heals.size() < MAX_HEALS) spawnHeal(screenWidth, screenHeight);
            lastSpawnTime = currentTime;
        }

        updateAsteroids(screenWidth, screenHeight);
        updateEnemies(screenWidth, screenHeight);
        updateBosses(); // อัปเดตสถานะของบอส
        updateEnemyBullets(screenWidth, screenHeight);
        updateBossBullets(screenWidth, screenHeight); // อัปเดตการเคลื่อนที่ของกระสุนบอส
        updateHeals(screenWidth, screenHeight);
        updateExplosions();
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
        Enemy enemy = new Enemy(x, y, speed, size);

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
        heals.clear();

        // สร้างบอส
        Boss boss = new Boss(x, y, speed, size); // กำหนดจำนวนชีวิตเป็น 10
        bosses.add(boss);
        bossActive = true; // ตั้งสถานะว่า บอสกำลังแอคทีฟ
        logger.warning("Boss already spawned!");
    }

    private void spawnHeal(double screenWidth, double screenHeight) {
        double size = 25.0;

        // สร้าง Heal ทีละ 2 ตัวในตำแหน่งสุ่ม
        for (int i = 0; i < 2; i++) {
            double x = random.nextDouble() * screenWidth;
            double y = random.nextDouble() * screenHeight;
            Heal heal = new Heal(x, y, size);
            heals.add(heal);
        }
    }

    private void updateExplosions() {
        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            explosion.move();
            // Remove the explosion if it has finished its animation
            if (explosion.isAnimationFinished()) {
                explosionIterator.remove();
            }
        }
    }

    private void spawnExplosion(double x, double y, double size) {
        Explosion explosion = new Explosion(x, y, size);
        explosions.add(explosion);
        AsteroidGame.playSound("/Sounds/Explosion.mp3");
    }

    private void updateHeals(double screenWidth, double screenHeight) {
        Iterator<Heal> healIterator = heals.iterator();
        while (healIterator.hasNext()) {
            Heal heal = healIterator.next();
            heal.move();

            // ลบ Heal เมื่ออยู่นอกหน้าจอ (หากต้องการ)
            if (heal.isOutOfScreen(screenWidth, screenHeight)) {
                healIterator.remove();
            }
        }
    }

    private void updateBosses() {
        Iterator<Boss> bossIterator = bosses.iterator();
        while (bossIterator.hasNext()) {
            Boss boss = bossIterator.next();
            boss.move();

            // ตรวจสอบว่าบอสตายหรือไม่
            if (!boss.isAlive()) {
                // Store the position for explosion
                double explosionX = boss.getX();
                double explosionY = boss.getY();
                double explosionSize = boss.getSize();

                bossIterator.remove();
                PlayerShip.addScore(10); // เพิ่มคะแนนเมื่อบอสถูกกำจัด
                PlayerShip.addPlayerShipBossEliminated(1);
                bossActive = false; // ตั้งสถานะว่าบอสไม่แอคทีฟแล้ว
                lastBossSpawnTime = System.nanoTime(); // เริ่มนับเวลาสำหรับการเกิดบอสครั้งถัดไป

                spawnExplosion(explosionX, explosionY, explosionSize);
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
                // Store the position for explosion
                double explosionX = enemy.getX();
                double explosionY = enemy.getY();
                double explosionSize = enemy.getSize();

                enemyIterator.remove();
                PlayerShip.addScore(1);

                spawnExplosion(explosionX, explosionY, explosionSize);
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
                // Store the position for explosion
                double explosionX = asteroid.getX();
                double explosionY = asteroid.getY();
                double explosionSize = asteroid.getSize(); // Example size for explosion

                asteroidIterator.remove();
                asteroids.addAll(asteroid.split());
                bullet.deactivate();

                // Spawn an explosion at the position of the destroyed asteroid
                spawnExplosion(explosionX, explosionY, explosionSize);

                // Add score based on size
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
                bullet.deactivate();// เพิ่มคะแนนเมื่อกำจัดศัตรู
                break;
            }
        }

        // Check collision between player bullets and bosses
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
                // Store the position for explosion
                double explosionX = asteroid.getX();
                double explosionY = asteroid.getY();
                double explosionSize = asteroid.getSize(); // Example size for explosion

                asteroidIterator.remove();
                asteroids.addAll(asteroid.split());
                ultimate.deactivate();

                spawnExplosion(explosionX, explosionY, explosionSize);

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
                // Store the position for explosion
                double explosionX = asteroid.getX();
                double explosionY = asteroid.getY();
                double explosionSize = asteroid.getSize(); // Example size for explosion
                asteroidIterator.remove();
                asteroids.addAll(asteroid.split());
                playerShip.PlayerShipTakingDamage(1);

                spawnExplosion(explosionX, explosionY, explosionSize);
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

        // ตรวจสอบการชนกับ Heal
        Iterator<Heal> healIterator = heals.iterator();
        while (healIterator.hasNext()) {
            Heal heal = healIterator.next();
            if (playerShip.collidesWith(heal)) {
                playerShip.addLives(1); // เพิ่มชีวิตให้กับผู้เล่นเมื่อเก็บ Heal
                AsteroidGame.playSound("/Sounds/GetItem.mp3");
                healIterator.remove(); // ลบ Heal ออกจากลิสต์เมื่อถูกเก็บ
                logger.info("Player collected a Heal! Lives increased.");
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
        heals.forEach(heal -> heal.draw(gc)); // วาด Heal
        explosions.forEach(exp -> exp.draw(gc));
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

    public static void resetBossCooldown() {
        lastBossSpawnTime = System.nanoTime();
        bossActive = false; // ตั้งสถานะว่าไม่มีบอสแอคทีฟอยู่
    }

    public List<Boss> getBosses() {
        return bosses;
    }
}
