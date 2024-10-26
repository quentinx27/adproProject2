package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;

public class Boss extends Character {
    private int lives;
    private int maxLives;
    private SpriteAnimation animation;  // SpriteAnimation สำหรับแสดงผลบอส
    private List<BossBullet> bullets;   // รายการเก็บกระสุนของบอส
    private long lastShotTime = 0;      // เวลาในการยิงครั้งสุดท้าย
    private final long shootInterval = 500_000_000;  // ระยะเวลาในการยิง (500 ms)

    public Boss(double x, double y, double speed, double size, int lives) {
        super(x, y, speed, size);
        this.lives = lives;
        this.maxLives = lives;  // เก็บค่าสูงสุดของ lives ไว้ใช้ในการแสดง health bar
        this.bullets = new ArrayList<>();  // Initialize the list of bullets

        // กำหนด SpriteAnimation สำหรับบอส
        animation = new SpriteAnimation("/Sprite Asset/BossSheet.png", 3, 3, 125_000_000); // 3x3 grid, 125ms per frame
    }

    @Override
    public void move() {
        // กำหนดทิศทางการเคลื่อนที่ของบอสในแนวนอนเท่านั้น (เริ่มต้นไปทางขวา)
        int moveDirection = 1;
        double boundaryLeft = 0;
        double boundaryRight = 1024 - size; // ขอบขวาของหน้าจอ

        // Boss-specific movement logic (left-right only)
        if (x <= boundaryLeft) {
            moveDirection = 1; // เปลี่ยนทิศทางเป็นไปทางขวา
        } else if (x >= boundaryRight) {
            moveDirection = -1; // เปลี่ยนทิศทางเป็นไปทางซ้าย
        }

        x += moveDirection * speed; // เคลื่อนที่ซ้าย-ขวา

        // อัปเดตแอนิเมชันของบอสทุกครั้งที่เคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime);  // อัปเดตเฟรมแอนิเมชัน

        // ยิงกระสุนออกมาทุกทิศทาง
        BossShooting(currentTime);
    }


    private void BossShooting(long currentTime) {
        if (currentTime - lastShotTime >= shootInterval) {
            // ยิงกระสุนออกมาจากทุกทิศทาง
            for (int angle = 0; angle < 360; angle += 45) {  // ยิงทุก 45 องศา
                BossBullet bullet = new BossBullet(x, y, 3.0, angle, 8.0);
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
        double scaleFactor = 4.0;  // ปรับขนาดของบอสให้ใหญ่ขึ้น
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        // คืนค่าสถานะของ canvas หลังจากวาดเสร็จ
        gc.restore();

        // วาดแถบชีวิต (health bar)
        gc.setFill(Color.GREEN);
        double healthBarWidth = size * ((double) lives / maxLives);  // ความยาวแถบชีวิตตามจำนวน lives
        gc.fillRect(x - size / 2, y - size, healthBarWidth, 5);  // แถบชีวิตอยู่ด้านบนของบอส

        // วาดกระสุนของบอส
        drawBullets(gc);
    }

    private void drawBullets(GraphicsContext gc) {
        // วาดกระสุนทั้งหมดในรายการ bullets
        bullets.forEach(bullet -> bullet.draw(gc));
    }

    public void updateBullets() {
        // อัปเดตตำแหน่งของกระสุนแต่ละลูก
        bullets.forEach(BossBullet::move);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void takeDamage(int damage) {
        // ลดจำนวนชีวิตลงเมื่อบอสถูกโจมตี
        lives -= damage;
        if (lives < 0) {
            lives = 0;  // ตรวจสอบให้จำนวนชีวิตไม่ติดลบ
        }
    }

    public boolean isAlive() {
        // ตรวจสอบว่าบอสยังมีชีวิตอยู่หรือไม่
        return lives > 0;
    }

    public List<BossBullet> getBullets() {
        return bullets;
    }
}
