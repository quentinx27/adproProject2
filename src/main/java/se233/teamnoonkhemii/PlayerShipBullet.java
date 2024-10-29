package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShipBullet extends Bullet {
    private SpriteAnimation animation; // SpriteAnimation สำหรับกระสุนผู้เล่น

    // Constructor สำหรับกำหนดค่าเริ่มต้นของ PlayerShipBullet
    public PlayerShipBullet(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // สร้าง SpriteAnimation สำหรับกระสุนผู้เล่นจาก sprite sheet
        animation = new SpriteAnimation("/Sprite Asset/playerShipBulle02.png", 1, 5, 125_000_000); // กำหนดแถวและคอลัมน์ของ sprite sheet และระยะเวลาต่อเฟรม
    }

    // เมธอดสำหรับสร้าง PlayerShipBullet จากตำแหน่งและมุมของยานผู้เล่น
    public static PlayerShipBullet createFromPlayerShip(PlayerShip playerShip) {
        // คำนวณตำแหน่งกระสุนให้ออกจากหัวของยานโดยคำนวณระยะ offset ตามขนาดและมุมของยาน
        double offsetX = (playerShip.getSize() / 2) * Math.cos(Math.toRadians(playerShip.getAngle()));
        double offsetY = (playerShip.getSize() / 2) * Math.sin(Math.toRadians(playerShip.getAngle()));

        // กำหนดตำแหน่ง X และ Y ของกระสุน โดยลบออกเล็กน้อยเพื่อตำแหน่งตรงกลางของ sprite
        double bulletX = playerShip.getX() + offsetX - 12;
        double bulletY = playerShip.getY() + offsetY - 12;

        // กำหนดความเร็ว มุม และขนาดของกระสุน
        double bulletSpeed = 10.0; // ความเร็วของกระสุน
        double bulletAngle = playerShip.getAngle(); // กระสุนจะยิงออกไปในทิศทางเดียวกับมุมของยาน
        double bulletSize = 10.0; // ขนาดของกระสุน

        // สร้างและส่งคืน PlayerShipBullet ใหม่
        return new PlayerShipBullet(bulletX, bulletY, bulletSpeed, bulletAngle, bulletSize);
    }

    @Override
    public void move() {
        // เรียกใช้เมธอด move() จากคลาสแม่เพื่ออัปเดตตำแหน่งของกระสุน
        super.move();

        // อัปเดตเฟรมของแอนิเมชันเมื่อกระสุนเคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // ใช้ SpriteAnimation ในการแสดงผลกระสุนแทนการวาดแบบวงกลม
        gc.save(); // บันทึกสถานะปัจจุบันของ canvas
        gc.translate(x, y); // ย้าย canvas ไปยังตำแหน่งของกระสุน

        // วาดเฟรมปัจจุบันของ SpriteAnimation โดยหมุนตามมุมของกระสุน
        gc.rotate(angle);
        double scaleFactor = 5.0; // กำหนดขนาดของกระสุน
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore(); // คืนค่า canvas กลับสู่สถานะเดิม
    }

    // เมธอดสำหรับปิดการใช้งานกระสุน
    public void deactivate() {
        this.isActive = false;
    }
}
