package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Boss extends Character {
    private int Bosslives;
    private SpriteAnimation animation;  // SpriteAnimation สำหรับแสดงผลบอส
    public List<BossBullet> bullets;   // รายการเก็บกระสุนของบอส
    private long lastShotTime = 0;      // เวลาในการยิงครั้งสุดท้าย
    private final long shootInterval = 500_000_000;  // ระยะเวลาในการยิง (500 ms)

    public Boss(double x, double y, double speed, double size, int lives) {
        super(x, y, speed, size);
        this.Bosslives = 10;
        this.bullets = new ArrayList<>();  // Initialize the list of bullets

        // กำหนด SpriteAnimation สำหรับบอส
        animation = new SpriteAnimation("/Sprite Asset/BossSheet.png", 3, 3, 125_000_000); // 3x3 grid, 125ms per frame
    }

    @Override
    public void move() {
        // Boss will remain stationary in the center of the screen.
        // No changes to x and y positions.
        long currentTime = System.nanoTime();
        animation.update(currentTime); // อัปเดตเฟรมแอนิเมชัน
        BossShoot(currentTime); // ยิงกระสุนออกมาทุกทิศทาง
    }

    public void BossShoot(long currentTime) {
        if (currentTime - lastShotTime >= shootInterval) {
            // ยิงกระสุนออกมาจากทุกทิศทาง
            for (int angle = 0; angle < 360; angle += 45) {  // ยิงทุก 45 องศา
                BossBullet bullet = BossBullet.createFromBoss(this, angle);
                bullets.add(bullet);
            }
            lastShotTime = currentTime;  // อัปเดตเวลายิงล่าสุด
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // บันทึกสถานะของ canvas ก่อนที่จะวาด
        gc.save();

        // แปลตำแหน่งของ canvas ไปยังตำแหน่งของบอส
        gc.translate(x, y);

        // วาดแอนิเมชันของบอส
        double scaleFactor = 2.0;  // ปรับขนาดของบอสให้ใหญ่ขึ้น
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        // คืนค่าสถานะของ canvas หลังจากวาดเสร็จ
        gc.restore();
    }



    public int getBosslives() {
        return Bosslives;
    }


    public void BossTakingDamage(int damage) {
        if (Bosslives > 0) {
            Bosslives -= damage;
        }
    }

    public boolean isAlive() {
        // ตรวจสอบว่าบอสยังมีชีวิตอยู่หรือไม่
        return Bosslives> 0;
    }
}
