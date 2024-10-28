package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Character {
    private SpriteAnimation animation;
    private double angle;  // มุมสำหรับเคลื่อนที่
    public static int Enemylives;
    private List<EnemyBullet> bullets; // รายการสำหรับกระสุน
    private long lastShotTime = 0;     // เวลาในการยิงครั้งสุดท้าย
    private final long shootInterval = 1_000_000_000; // ระยะเวลาในการยิง (1 วินาที)

    public Enemy(double x, double y, double speed, double size) {
        super(x, y, speed, size);
        this.angle = 0;
        this.Enemylives = 2;
        animation = new SpriteAnimation("/Sprite Asset/Enemy02p.png", 2, 3, 75_000_000);  // 3x3 grid, 125ms per frame
        bullets = new ArrayList<>(); // สร้างลิสต์สำหรับเก็บกระสุน
    }

    @Override
    public void move() {
        x += speed * Math.cos(Math.toRadians(angle)) + (Math.random() - 0.5) * 2;
        y += speed + (Math.random() - 0.5) * 2;
        angle += (Math.random() - 0.5) * 5;

        long currentTime = System.nanoTime();
        if (currentTime - lastShotTime >= shootInterval) {
            EnemyShoot();
            lastShotTime = currentTime;
        }
    }

    public void EnemyShoot() {
        EnemyBullet bullet = EnemyBullet.EnemyShoot(this);
        bullets.add(bullet);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.rotate(angle);

        long currentTime = System.nanoTime();
        animation.update(currentTime);

        double scaleFactor = 2.0;
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);
        gc.restore();

    }

    public void EnemyTakingDamage(int damage) {
        if (Enemylives > 0) {
            Enemylives -= damage;
        }
    }

    public boolean EnemyIsEliminated() {
        return Enemylives <= 0;
    }

    public double getAngle() {
        return angle;
    }
    public static int getEnemylives() {
        return Enemylives;
    }
}
