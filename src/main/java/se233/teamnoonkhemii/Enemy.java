package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Enemy extends Character {
    private SpriteAnimation animation;
    private double angle;  // มุมสำหรับเคลื่อนที่ (ถ้ามี)

    public Enemy(double x, double y, double speed, double size, String imagePath) {
        super(x, y, speed, size);
        this.angle = 0; // กำหนดมุมเริ่มต้น (ถ้าไม่ต้องการหมุนให้ตั้งเป็น 0)
        // โหลดภาพศัตรูและสร้างแอนิเมชัน
        animation = new SpriteAnimation(imagePath, 3, 3, 125_000_000);  // 3x3 grid, 125ms per frame
    }

    @Override
    public void move() {
        // Logic สำหรับการเคลื่อนที่ของศัตรู
        y += speed;  // เคลื่อนที่ลงด้านล่าง
    }

    @Override
    public void draw(GraphicsContext gc) {
        // บันทึกสถานะของ canvas
        gc.save();

        // แปลตำแหน่งของ canvas ไปยังตำแหน่งศัตรู
        gc.translate(x, y);
        gc.rotate(angle);  // หมุนตามมุมที่กำหนด (ถ้ามี)

        // อัปเดตเฟรมแอนิเมชัน
        long currentTime = System.nanoTime();
        animation.update(currentTime);  // อัปเดตเฟรม

        // วาดเฟรมปัจจุบันของแอนิเมชัน
        double scaleFactor = 3.0;  // ปรับขนาดของภาพ
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        // คืนค่าสถานะของ canvas
        gc.restore();
    }

    public void rotate(double degrees) {
        this.angle += degrees;  // ปรับมุมสำหรับการหมุน
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
