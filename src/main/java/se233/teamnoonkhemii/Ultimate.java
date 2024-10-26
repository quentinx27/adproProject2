package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Ultimate extends Bullet {
    private SpriteAnimation animation;

    public Ultimate(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนด SpriteAnimation สำหรับ Ultimate Skill ที่แตกต่างจาก PlayerShipBullet
        animation = new SpriteAnimation("/Sprite Asset/UltimateSheet.png", 3, 3, 100_000_000);  // สมมติว่า sprite sheet มี 3x3 เฟรม
    }

    public static Ultimate createFromPlayerShip(PlayerShip playerShip) {
        // คำนวณตำแหน่งกระสุนให้ออกจากหัวของยาน โดยใช้ค่าขนาดยานเพื่อลบออกจากตำแหน่งกระสุนที่ถูกยิงออก
        double offsetX = (playerShip.getSize() / 2) * Math.cos(Math.toRadians(playerShip.getAngle()));
        double offsetY = (playerShip.getSize() / 2) * Math.sin(Math.toRadians(playerShip.getAngle()));

        // ตำแหน่ง X และ Y ของ Ultimate ที่ปล่อยออกมาจากหัวของยาน
        double ultimateX = playerShip.getX() + offsetX - 10;
        double ultimateY = playerShip.getY() + offsetY - 10;

        double ultimateSpeed = 3.0;  // ความเร็วของ Ultimate (สามารถเปลี่ยนได้)
        double ultimateAngle = playerShip.getAngle();  // ทิศทางการยิงของ Ultimate จะเป็นไปตามมุมของยาน
        double ultimateSize = 10.0;  // ขนาดของ Ultimate จะใหญ่กว่ากระสุนธรรมดา

        return new Ultimate(ultimateX, ultimateY, ultimateSpeed, ultimateAngle, ultimateSize);
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
        // ใช้ SpriteAnimation ในการแสดงผล Ultimate Skill
        gc.save();  // บันทึกสถานะของ canvas
        gc.translate(x, y);  // ย้ายไปที่ตำแหน่งของ Ultimate

        // วาดเฟรมปัจจุบันของ SpriteAnimation
        double scaleFactor = 10.0;  // ปรับขนาดของ Ultimate ให้ใหญ่กว่า PlayerShipBullet
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore();  // คืนค่าคอนเท็กซ์ canvas
    }

    public void deactivate() {
        this.isActive = false;
    }
}
