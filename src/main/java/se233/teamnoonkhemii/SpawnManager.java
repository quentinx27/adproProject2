package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SpawnManager {

    private static final Logger logger = Logger.getLogger(SpawnManager.class.getName());

    // ลิสต์สำหรับเก็บอ็อบเจ็กต์ต่างๆ เช่น อุกกาบาต, ศัตรู, บอส, กระสุน, Heal, การระเบิด
    public static List<Asteroid> asteroids;
    public static List<Enemy> enemies;
    public static List<Boss> bosses;
    public static List<EnemyBullet> enemyBullets;
    public static List<BossBullet> bossBullets;
    public static List<Heal> heals;
    public static List<Explosion> explosions;

    private static final int MAX_ASTEROIDS = 20; // จำนวนสูงสุดของอุกกาบาตที่สามารถแสดงบนหน้าจอ
    private static final int MAX_ENEMIES = 10; // จำนวนสูงสุดของศัตรูที่สามารถแสดงบนหน้าจอ
    private static final int MAX_HEALS = 2; // จำนวนสูงสุดของ Heal ที่สามารถแสดงบนหน้าจอ
    private static final long SPAWN_INTERVAL = 500_000_000L; // ช่วงเวลาการเกิดใหม่ของศัตรูและอุกกาบาต (0.5 วินาที)
    private static final long BOSS_SPAWN_INTERVAL = 25_000_000_000L; // ช่วงเวลาการเกิดใหม่ของบอส (25 วินาที)
    private long lastSpawnTime = 0; // เก็บเวลาครั้งสุดท้ายที่เกิดศัตรู/อุกกาบาต
    private static long lastBossSpawnTime = System.nanoTime(); // เก็บเวลาครั้งสุดท้ายที่เกิดบอส
    private static boolean bossActive = false; // ใช้ตรวจสอบว่าบอสกำลังแอคทีฟอยู่หรือไม่
    private final Random random = new Random(); // ใช้สำหรับสุ่มตำแหน่งและคุณสมบัติของอ็อบเจ็กต์

    // Constructor สำหรับสร้างลิสต์ที่เก็บอ็อบเจ็กต์
    public SpawnManager() {
        asteroids = new ArrayList<>();
        enemies = new ArrayList<>();
        bosses = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        bossBullets = new ArrayList<>();
        heals = new ArrayList<>();
        explosions = new ArrayList<>();
    }

    // อัปเดตสถานะและเกิดอ็อบเจ็กต์ใหม่บนหน้าจอ
    public void updateAndSpawn(long currentTime, double screenWidth, double screenHeight) {
        // สร้างบอสทุก ๆ 25 วินาทีถ้าไม่มีบอสอยู่ในตอนนี้
        if (!bossActive && currentTime - lastBossSpawnTime >= BOSS_SPAWN_INTERVAL) {
            spawnBoss(screenWidth, screenHeight);
            lastBossSpawnTime = currentTime;
        }

        // สร้างศัตรูและอุกกาบาตเมื่อไม่มีบอสแอคทีฟอยู่และถึงเวลาการเกิดใหม่
        if (!bossActive && currentTime - lastSpawnTime >= SPAWN_INTERVAL) {
            if (asteroids.size() < MAX_ASTEROIDS) spawnAsteroid(screenWidth, screenHeight);
            if (enemies.size() < MAX_ENEMIES) spawnEnemy(screenWidth, screenHeight);
            if (heals.size() < MAX_HEALS) spawnHeal(screenWidth, screenHeight);
            lastSpawnTime = currentTime;
        }

        // อัปเดตสถานะของอ็อบเจ็กต์ต่างๆ
        updateAsteroids(screenWidth, screenHeight);
        updateEnemies(screenWidth, screenHeight);
        updateBosses();
        updateEnemyBullets(screenWidth, screenHeight);
        updateBossBullets(screenWidth, screenHeight);
        updateHeals(screenWidth, screenHeight);
        updateExplosions();
    }

    // สร้างอุกกาบาตใหม่
    private void spawnAsteroid(double screenWidth, double screenHeight) {
        double size = 60.0; // ขนาดของอุกกาบาต
        double speed = 2 + random.nextDouble() * 5; // สุ่มความเร็ว
        double x = random.nextDouble() * screenWidth; // สุ่มตำแหน่ง X
        double y = random.nextDouble() * screenHeight; // สุ่มตำแหน่ง Y
        double direction = random.nextDouble() * 360; // สุ่มทิศทางการเคลื่อนที่
        asteroids.add(new Asteroid(x, y, speed, size, direction)); // เพิ่มอุกกาบาตลงในลิสต์
    }

    // สร้างศัตรูใหม่
    private void spawnEnemy(double screenWidth, double screenHeight) {
        double size = 40.0; // ขนาดของศัตรู
        double speed = 3.0; // ความเร็วของศัตรู
        double x = random.nextDouble() * screenWidth; // สุ่มตำแหน่ง X
        double y = random.nextDouble() * screenHeight; // สุ่มตำแหน่ง Y
        Enemy enemy = new Enemy(x, y, speed, size); // สร้างวัตถุ Enemy
        enemies.add(enemy); // เพิ่ม Enemy ลงในลิสต์
        EnemyBullet bullet = EnemyBullet.EnemyShoot(enemy); // สร้างกระสุนจากศัตรู
        enemyBullets.add(bullet); // เพิ่มกระสุนลงในลิสต์ของกระสุนศัตรู
    }

    // สร้างบอสใหม่
    private void spawnBoss(double screenWidth, double screenHeight) {
        double size = 100.0; // ขนาดของบอส
        double speed = 0.0; // บอสจะไม่เคลื่อนที่
        double x = screenWidth / 2 - size / 2 + 24; // เริ่มที่ตำแหน่งกลางจอในแนวนอน
        double y = screenHeight / 2 - size / 2; // เริ่มที่ตำแหน่งกลางจอในแนวตั้ง

        // ลบศัตรูและอุกกาบาตทั้งหมดเมื่อบอสเกิด
        enemies.clear();
        asteroids.clear();
        heals.clear();

        // สร้างบอสและเพิ่มลงในลิสต์ของบอส
        Boss boss = new Boss(x, y, speed, size);
        bosses.add(boss);
        bossActive = true; // ตั้งสถานะว่าบอสกำลังแอคทีฟอยู่
        logger.warning("Boss already spawned!");
    }

    // สร้าง Heal ใหม่
    private void spawnHeal(double screenWidth, double screenHeight) {
        double size = 25.0; // ขนาดของ Heal
        for (int i = 0; i < 2; i++) {
            double x = random.nextDouble() * screenWidth; // สุ่มตำแหน่ง X
            double y = random.nextDouble() * screenHeight; // สุ่มตำแหน่ง Y
            Heal heal = new Heal(x, y, size); // สร้างวัตถุ Heal
            heals.add(heal); // เพิ่ม Heal ลงในลิสต์
        }
    }

    // อัปเดตสถานะของการระเบิด
    private void updateExplosions() {
        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            explosion.move(); // อัปเดตแอนิเมชันของการระเบิด
            // ลบการระเบิดออกจากลิสต์หากแอนิเมชันสิ้นสุด
            if (explosion.isAnimationFinished()) {
                explosionIterator.remove();
            }
        }
    }

    // สร้างการระเบิด
    private void spawnExplosion(double x, double y, double size) {
        Explosion explosion = new Explosion(x, y, size); // สร้างวัตถุ Explosion
        explosions.add(explosion); // เพิ่มลงในลิสต์
        AsteroidGame.playSound("/Sounds/Explosion.mp3"); // เล่นเสียงระเบิด
    }

    // อัปเดตสถานะของ Heal
    private void updateHeals(double screenWidth, double screenHeight) {
        Iterator<Heal> healIterator = heals.iterator();
        while (healIterator.hasNext()) {
            Heal heal = healIterator.next();
            heal.move(); // อัปเดตแอนิเมชันของ Heal
            // ลบ Heal เมื่อออกนอกหน้าจอ
            if (heal.isOutOfScreen(screenWidth, screenHeight)) {
                healIterator.remove();
            }
        }
    }

    // อัปเดตสถานะของบอส
    private void updateBosses() {
        Iterator<Boss> bossIterator = bosses.iterator();
        while (bossIterator.hasNext()) {
            Boss boss = bossIterator.next();
            boss.move(); // อัปเดตแอนิเมชันของบอส
            // ตรวจสอบว่าบอสตายหรือไม่
            if (!boss.isAlive()) {
                double explosionX = boss.getX();
                double explosionY = boss.getY();
                double explosionSize = boss.getSize();
                bossIterator.remove(); // ลบบอสที่ตายแล้วออกจากลิสต์
                PlayerShip.addScore(10); // เพิ่มคะแนนเมื่อบอสถูกกำจัด
                PlayerShip.addPlayerShipBossEliminated(1); // บันทึกจำนวนบอสที่ถูกกำจัด
                bossActive = false; // ตั้งสถานะว่าบอสไม่แอคทีฟแล้ว
                lastBossSpawnTime = System.nanoTime(); // เริ่มนับเวลาสำหรับการเกิดบอสครั้งถัดไป
                spawnExplosion(explosionX, explosionY, explosionSize); // สร้างการระเบิดที่ตำแหน่งของบอส
                bossBullets.clear(); // ลบกระสุนบอสทั้งหมด
                logger.warning("Boss already Eliminated");
                continue;
            }
            if (boss.isAlive()) {
                BossShoot(boss); // ให้บอสยิงกระสุน
            }
        }
    }
    // เมธอดที่ให้บอสยิงกระสุน
    private void BossShoot(Boss boss) {
        long currentTime = System.nanoTime();
        boss.BossShoot(currentTime); // ให้บอสทำการยิงกระสุน
        boss.bullets.forEach(bullet -> {
            if (!bossBullets.contains(bullet)) {
                bossBullets.add(bullet); // เพิ่มกระสุนบอสลงในลิสต์
            }
        });
    }

    // อัปเดตสถานะของกระสุนบอส
    private void updateBossBullets(double screenWidth, double screenHeight) {
        Iterator<BossBullet> bulletIterator = bossBullets.iterator();
        while (bulletIterator.hasNext()) {
            BossBullet bullet = bulletIterator.next();
            bullet.move(); // อัปเดตตำแหน่งของกระสุน
            // ลบกระสุนเมื่อออกนอกหน้าจอ
            if (bullet.isOutOfScreen(screenWidth, screenHeight)) {
                bulletIterator.remove();
            }
        }
    }

    // อัปเดตสถานะของอุกกาบาต
    private void updateAsteroids(double screenWidth, double screenHeight) {
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            asteroid.move(); // อัปเดตตำแหน่งของอุกกาบาต
            // ลบอุกกาบาตเมื่อออกนอกหน้าจอ
            if (asteroid.isOutOfScreen(screenWidth, screenHeight)) {
                asteroidIterator.remove();
            }
        }
    }

    // อัปเดตสถานะของศัตรู
    private void updateEnemies(double screenWidth, double screenHeight) {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move(); // อัปเดตตำแหน่งของศัตรู
            // ลบศัตรูที่ถูกกำจัด
            if (enemy.EnemyIsEliminated()) {
                double explosionX = enemy.getX();
                double explosionY = enemy.getY();
                double explosionSize = enemy.getSize();
                enemyIterator.remove();
                PlayerShip.addScore(2); // เพิ่มคะแนนเมื่อกำจัดศัตรู
                spawnExplosion(explosionX, explosionY, explosionSize);
                continue;
            }
            // ลบศัตรูเมื่อออกนอกหน้าจอ
            if (enemy.isOutOfScreen(screenWidth, screenHeight)) {
                enemyIterator.remove();
            }
        }
    }

    // อัปเดตสถานะของกระสุนศัตรู
    private void updateEnemyBullets(double screenWidth, double screenHeight) {
        Iterator<EnemyBullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            EnemyBullet bullet = bulletIterator.next();
            bullet.move(); // อัปเดตตำแหน่งของกระสุน
            // ลบกระสุนเมื่อออกนอกหน้าจอ
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
                playerShip.PlayerShipTakingDamage(5); // บอสทำความเสียหายเมื่อชนกับผู้เล่น
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
                playerShip.PlayerShipTakingDamage(3); // บอสทำความเสียหายมากกว่าศัตรูปกติ
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


    // เมธอดสำหรับวาดอ็อบเจ็กต์ทั้งหมดลงใน canvas
    public void drawEntities(GraphicsContext gc) {
        asteroids.forEach(asteroid -> asteroid.draw(gc));
        enemies.forEach(enemy -> enemy.draw(gc));
        enemyBullets.forEach(bullet -> bullet.draw(gc));
        bosses.forEach(boss -> boss.draw(gc));
        bossBullets.forEach(bullet -> bullet.draw(gc));
        heals.forEach(heal -> heal.draw(gc));
        explosions.forEach(exp -> exp.draw(gc));
    }

    // เมธอดสำหรับรีเซ็ตคูลดาวน์ของบอส
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
