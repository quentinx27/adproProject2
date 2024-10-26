package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class EnemyBullet extends Bullet {
    private SpriteAnimation animation;

    public EnemyBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนดค่า SpriteAnimation สำหรับกระสุนศัตรู
        animation = new SpriteAnimation("/Sprite Asset/EnemyBulletSheet.png", 2, 3, 125_000_000);  // สมมติว่า sprite sheet มี 2x2 เฟรม
    }

    public static EnemyBullet EnemyShoot(Enemy enemy) {
        // Calculate the bullet’s position and angle based on the Enemy’s position and angle
        double offsetX = (enemy.getSize() / 2) * Math.cos(Math.toRadians(enemy.getAngle()));
        double offsetY = (enemy.getSize() / 2) * Math.sin(Math.toRadians(enemy.getAngle()));

        double bulletX = enemy.getX() + offsetX - 10;
        double bulletY = enemy.getY() + offsetY - 10;
        double bulletSpeed = 3.0;
        double bulletAngle = enemy.getAngle();
        double bulletSize = 8.0;

        return new EnemyBullet(bulletX, bulletY, bulletSpeed, bulletAngle, bulletSize);
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
        // ใช้ SpriteAnimation ในการแสดงผลกระสุนศัตรู
        gc.save();  // บันทึกสถานะของ canvas
        gc.translate(x, y);  // ย้ายไปที่ตำแหน่งของกระสุน
        // วาดเฟรมปัจจุบันของ SpriteAnimation
        gc.rotate(angle);
        double scaleFactor = 5.0;  // สามารถปรับขนาดของกระสุนได้ตามต้องการ
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าคอนเท็กซ์ canvas
    }
}
