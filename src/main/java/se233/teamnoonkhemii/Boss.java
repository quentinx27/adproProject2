package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boss extends Character {
    private int lives;
    private int maxLives;
    private SpriteAnimation animation; // SpriteAnimation สำหรับแสดงผลบอส

    public Boss(double x, double y, double speed, double size, int lives) {
        super(x, y, speed, size);
        this.lives = lives;
        this.maxLives = lives;  // เก็บค่าสูงสุดของ lives ไว้ใช้ในการแสดง health bar

        // กำหนด SpriteAnimation สำหรับบอส
        animation = new SpriteAnimation("/Sprite Asset/BossSheet.png", 3, 3, 125_000_000); // 3x3 grid, 125ms per frame
    }

    @Override
    public void move() {
        // Boss-specific movement logic, e.g., oscillating or slow movement
        y += speed; // ตัวอย่าง: บอสเคลื่อนที่ลงล่าง

        // อัปเดตแอนิเมชันของบอสทุกครั้งที่เคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime);  // อัปเดตเฟรมแอนิเมชัน
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
}
