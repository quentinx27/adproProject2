package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShipBullet extends Bullet {
    private SpriteAnimation animation;

    public PlayerShipBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนดค่า SpriteAnimation สำหรับกระสุนผู้เล่น
        animation = new SpriteAnimation("/Sprite Asset/playerShipBulletSheet.png", 2, 3, 125_000_000);  // สมมติว่า sprite sheet มี 2x2 เฟรม
    }

    public static PlayerShipBullet createFromPlayerShip(PlayerShip playerShip) {
        // แก้ไขการคำนวณตำแหน่งกระสุนให้ออกจากหัวของยาน โดยใช้ค่าขนาดยานเพื่อลบออกจากตำแหน่งกระสุนที่ถูกยิงออก
        double offsetX = (playerShip.getSize() /2) * Math.cos(Math.toRadians(playerShip.getAngle()));
        double offsetY = (playerShip.getSize() /2) * Math.sin(Math.toRadians(playerShip.getAngle()));

        //y: -10 x:-10
        // ตำแหน่ง X และ Y ของกระสุนที่ปล่อยออกมาจากหัวของยาน
        double bulletX = playerShip.getX() + offsetX -10;
        double bulletY = playerShip.getY() + offsetY -10;

        double bulletSpeed = 3.0;  // ความเร็วของกระสุน
        double bulletAngle = playerShip.getAngle();  // กระสุนจะยิงออกไปในทิศทางเดียวกับมุมของยาน
        double bulletSize = 8.0;  // ขนาดของกระสุน

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
        gc.rotate(angle);
        double scaleFactor = 5.0;  // สามารถปรับขนาดของกระสุนได้ตามต้องการ
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าคอนเท็กซ์ canvas
    }

    public void deactivate() {
        this.isActive = false;
    }
}