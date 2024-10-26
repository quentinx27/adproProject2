package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class BossBullet extends Bullet {
    private SpriteAnimation animation;

    public BossBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนดค่า SpriteAnimation สำหรับกระสุนของบอส
        animation = new SpriteAnimation("/Sprite Asset/BossBulletSheet.png", 2, 3, 125_000_000);  // สมมติว่า sprite sheet มี 2x3 เฟรม
    }

    public static BossBullet createFromBoss(Boss boss, double angleOffset) {
        // คำนวณตำแหน่งการปล่อยกระสุนจากตำแหน่งของบอส
        double offsetX = (boss.getSize() / 2) * Math.cos(Math.toRadians(angleOffset));
        double offsetY = (boss.getSize() / 2) * Math.sin(Math.toRadians(angleOffset));

        // ตำแหน่ง X และ Y ของกระสุนที่ปล่อยออกมาจากตำแหน่งบอส
        double bulletX = boss.getX() + offsetX - 10;
        double bulletY = boss.getY() + offsetY - 10;

        double bulletSpeed = 3.0;  // ความเร็วของกระสุนบอส
        double bulletAngle = angleOffset;  // มุมการยิงของกระสุนบอสตามมุมที่กำหนด
        double bulletSize = 8.0;  // ขนาดของกระสุนบอส

        return new BossBullet(bulletX, bulletY, bulletSpeed, bulletAngle, bulletSize);
    }

    @Override
    public void move() {
        super.move();
        // อัปเดตแอนิเมชันทุกครั้งที่มีการเคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // ใช้ SpriteAnimation ในการแสดงผลกระสุนของบอส
        gc.save();  // บันทึกสถานะของ canvas
        gc.translate(x, y);  // ย้ายไปที่ตำแหน่งของกระสุน

        // วาดเฟรมปัจจุบันของ SpriteAnimation
        double scaleFactor = 8.0;  // ขนาดการแสดงผลของกระสุนบอส
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าคอนเท็กซ์ canvas
    }
}
