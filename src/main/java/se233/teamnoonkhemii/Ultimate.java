package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Ultimate extends Bullet {
    private SpriteAnimation animation;  // SpriteAnimation สำหรับ Ultimate Skill


    // คอนสตรัคเตอร์สำหรับสร้าง Ultimate โดยรับพารามิเตอร์ตำแหน่ง ความเร็ว มุม และขนาด
    public Ultimate(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนด SpriteAnimation สำหรับ Ultimate Skill ที่แตกต่างจาก PlayerShipBullet
        animation = new SpriteAnimation("/Sprite Asset/Ultimate01.png", 4, 5, 100_000_000);  // สมมติว่า sprite sheet มี 3x3 เฟรม
    }

    // เมธอดสำหรับสร้างวัตถุ Ultimate จากตำแหน่งของยานผู้เล่น
    public static Ultimate[] createFromPlayerShip(PlayerShip playerShip) {
        double baseAngle = playerShip.getAngle(); // มุมฐานจากยานผู้เล่น

        // กำหนดมุมการยิงสำหรับกระสุน 3 ทิศทาง
        double[] angles = {baseAngle - 15, baseAngle, baseAngle + 15};
        Ultimate[] ultimateShots = new Ultimate[3]; // อาร์เรย์สำหรับเก็บ Ultimate 3 ลูก

        for (int i = 0; i < angles.length; i++) {
            double angle = angles[i];
            double offsetX = (playerShip.getSize() / 2) * Math.cos(Math.toRadians(angle));
            double offsetY = (playerShip.getSize() / 2) * Math.sin(Math.toRadians(angle));

            double ultimateX = playerShip.getX() + offsetX - 10;
            double ultimateY = playerShip.getY() + offsetY - 10;

            double ultimateSpeed = 3.0;  // ความเร็วของ Ultimate
            double ultimateSize = 15.0;  // ขนาดของ Ultimate

            ultimateShots[i] = new Ultimate(ultimateX, ultimateY, ultimateSpeed, angle, ultimateSize);
        }

        return ultimateShots; // คืนค่าเป็นอาร์เรย์ Ultimate 3 ลูก
    }
    @Override
    public void move() {
        super.move();  // เรียกใช้เมธอด move ของคลาส Bullet เพื่ออัปเดตตำแหน่งของ Ultimate
        // อัปเดตแอนิเมชันทุกครั้งที่มีการเคลื่อนที่
        long currentTime = System.nanoTime();
        animation.update(currentTime); // อัปเดตเฟรมปัจจุบันของแอนิเมชัน
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
    // เมธอดสำหรับปิดการทำงานของ Ultimate
    public void deactivate() {

        this.isActive = false;  // ตั้งค่า isActive เป็น false เพื่อบ่งบอกว่า Ultimate ไม่แอคทีฟอีกต่อไป
    }
}
