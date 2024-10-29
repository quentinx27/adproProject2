package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class EnemyBullet extends Bullet {
    private SpriteAnimation animation; // SpriteAnimation สำหรับกระสุนของศัตรู

    // Constructor สำหรับสร้างกระสุนของศัตรู
    public EnemyBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนดค่า SpriteAnimation สำหรับกระสุนศัตรู
        animation = new SpriteAnimation("/Sprite Asset/FinalBadBullet01.png", 3, 3, 125_000_000);  // สมมติว่า sprite sheet มี 2x2 เฟรม
    }

    // เมธอด static สำหรับการยิงกระสุนจากศัตรู โดยใช้ตำแหน่งและมุมของศัตรูในการคำนวณ
    public static EnemyBullet EnemyShoot(Enemy enemy) {
        // คำนวณตำแหน่งกระสุนจากตำแหน่งและมุมของศัตรู
        double offsetX = (enemy.getSize() / 2) * Math.cos(Math.toRadians(enemy.getAngle()));
        double offsetY = (enemy.getSize() / 2) * Math.sin(Math.toRadians(enemy.getAngle()));

        // กำหนดตำแหน่ง X และ Y ของกระสุนที่ปล่อยออกมาจากตำแหน่งศัตรู
        double bulletX = enemy.getX() + offsetX - 10;
        double bulletY = enemy.getY() + offsetY - 10;

        // กำหนดความเร็ว มุม และขนาดของกระสุน
        double bulletSpeed = 3.0;
        double bulletAngle = enemy.getAngle();
        double bulletSize = 9.0;

        // สร้างและส่งคืนออบเจ็กต์ EnemyBullet
        return new EnemyBullet(bulletX, bulletY, bulletSpeed, bulletAngle, bulletSize);
    }

    // เมธอดสำหรับการเคลื่อนที่ของกระสุน
    @Override
    public void move() {
        super.move(); // ใช้เมธอด move() ของคลาสแม่ (Bullet) เพื่อคำนวณตำแหน่งใหม่
        // อัปเดตแอนิเมชันทุกครั้งที่มีการเคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime);
    }

    // เมธอดสำหรับวาดกระสุนศัตรูลงใน GraphicsContext
    @Override
    public void draw(GraphicsContext gc) {
        // ใช้ SpriteAnimation ในการแสดงผลกระสุนศัตรู
        gc.save();  // บันทึกสถานะของ canvas
        gc.translate(x, y);  // ย้ายไปที่ตำแหน่งของกระสุน

        // วาดเฟรมปัจจุบันของ SpriteAnimation โดยหมุนตามมุมของกระสุน
        gc.rotate(angle);
        double scaleFactor = 5.0;  // สามารถปรับขนาดของกระสุนได้ตามต้องการ
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าคอนเท็กซ์ canvas หลังจากวาดเสร็จ
    }

    // เมธอดสำหรับรับมุมของกระสุน
    public double getAngle() {
        return angle;
    }
}
