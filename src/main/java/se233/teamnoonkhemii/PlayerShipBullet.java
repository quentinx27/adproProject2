package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShipBullet extends Bullet {
    private SpriteAnimation animation;

    public PlayerShipBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนดค่า SpriteAnimation สำหรับกระสุนผู้เล่น
        animation = new SpriteAnimation("/Sprite Asset/playerShipBulletSheet.png", 2, 2, 100_000_000);  // สมมติว่า sprite sheet มี 2x2 เฟรม
    }

    public static PlayerShipBullet createFromPlayerShip(PlayerShip playerShip) {
        // คำนวณตำแหน่งกระสุนให้ออกจากหัวของยาน
        double offsetX = playerShip.getSize() * Math.cos(Math.toRadians(playerShip.getAngle()));  // การเลื่อนออกจากตำแหน่งยาน
        double offsetY = playerShip.getSize() * Math.sin(Math.toRadians(playerShip.getAngle()));
        double bulletX = playerShip.getX() + offsetX;  // ตำแหน่ง X ของกระสุนที่ปล่อยออกจากหัวของยาน
        double bulletY = playerShip.getY() + offsetY;  // ตำแหน่ง Y ของกระสุนที่ปล่อยออกจากหัวของยาน
        double bulletSpeed = 5.0;  // ความเร็วของกระสุน
        double bulletAngle = playerShip.getAngle();  // กระสุนจะยิงออกไปในทิศทางเดียวกับยาน
        double bulletSize = 5.0;  // ขนาดของกระสุน

        return new PlayerShipBullet(bulletX, bulletY, bulletSpeed, bulletAngle, bulletSize);
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
        // ใช้ SpriteAnimation ในการแสดงผลกระสุนแทนการวาดแบบเดิม
        gc.save();  // บันทึกสถานะของ canvas
        gc.translate(x, y);  // ย้ายไปที่ตำแหน่งของกระสุน

        // วาดเฟรมปัจจุบันของ SpriteAnimation
        double scaleFactor = 5.0;  // สามารถปรับขนาดของกระสุนได้ตามต้องการ
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าคอนเท็กซ์ canvas
    }
}