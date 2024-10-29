package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class BossBullet extends Bullet {
    private SpriteAnimation animation; // เก็บแอนิเมชันสำหรับกระสุนของบอส

    // เมธอดสร้างออบเจกต์ BossBullet โดยกำหนดค่าตำแหน่ง ความเร็ว มุม และขนาด
    public BossBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size); // เรียกใช้งาน constructor ของคลาสแม่ (Bullet)
        // กำหนดค่า SpriteAnimation สำหรับกระสุนของบอส
        animation = new SpriteAnimation("/Sprite Asset/FinalBadBullet02.png", 2, 2, 125_000_000);  // สมมติว่า sprite sheet มี 2x3 เฟรม
    }

    // เมธอดสร้างกระสุนจากบอส โดยมีการคำนวณมุมการยิงที่แตกต่างกัน
    public static BossBullet createFromBoss(Boss boss, double angleOffset) {
        // คำนวณตำแหน่งการปล่อยกระสุนจากตำแหน่งของบอสและมุมที่กำหนด
        double offsetX = (boss.getSize() / 2) * Math.cos(Math.toRadians(angleOffset));
        double offsetY = (boss.getSize() / 2) * Math.sin(Math.toRadians(angleOffset));

        // คำนวณตำแหน่ง X และ Y ของกระสุนที่ปล่อยออกมาจากตำแหน่งบอส
        double bulletX = boss.getX() + offsetX - 1;
        double bulletY = boss.getY() + offsetY - 10;

        double bulletSpeed = 3.0;  // ความเร็วของกระสุนบอส
        double bulletAngle = angleOffset;  // มุมการยิงของกระสุนบอสตามมุมที่กำหนด
        double bulletSize = 6.0;  // ขนาดของกระสุนบอส

        // สร้างและคืนค่า BossBullet ที่มีตำแหน่ง ความเร็ว มุม และขนาดตามที่กำหนด
        return new BossBullet(bulletX, bulletY, bulletSpeed, bulletAngle, bulletSize);
    }

    // เมธอดสำหรับการเคลื่อนที่ของกระสุนบอส
    @Override
    public void move() {
        super.move(); // เรียกใช้การเคลื่อนที่จากคลาสแม่ (Bullet)
        // อัปเดตแอนิเมชันทุกครั้งที่มีการเคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime); // อัปเดตเฟรมของแอนิเมชัน
    }

    // เมธอดสำหรับวาดกระสุนบอสลงบน Canvas โดยใช้ GraphicsContext
    @Override
    public void draw(GraphicsContext gc) {
        // ใช้ SpriteAnimation ในการแสดงผลกระสุนของบอส
        gc.save();  // บันทึกสถานะของ canvas
        gc.translate(x, y);  // ย้ายไปที่ตำแหน่งของกระสุน

        // วาดเฟรมปัจจุบันของ SpriteAnimation
        gc.rotate(angle); // หมุนภาพตามมุมของกระสุน
        double scaleFactor = 8.0;  // ขนาดการแสดงผลของกระสุนบอส
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าสถานะของ canvas หลังจากวาดเสร็จ
    }
}
