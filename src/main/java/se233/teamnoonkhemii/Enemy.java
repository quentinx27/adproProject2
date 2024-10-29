package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Character {
    private SpriteAnimation animation; // ออบเจ็กต์สำหรับจัดการแอนิเมชันของศัตรู
    private double angle;  // มุมสำหรับเคลื่อนที่ของศัตรู
    private int Enemylives; // จำนวนชีวิตของศัตรู
    public static int EnemylivesTest; // ตัวแปร static สำหรับทดสอบจำนวนชีวิตของศัตรู
    private List<EnemyBullet> bullets; // รายการสำหรับเก็บกระสุนของศัตรู
    private long lastShotTime = 0;     // เวลาในการยิงครั้งสุดท้าย
    private final long shootInterval = 1_000_000_000; // ระยะเวลาในการยิง (1 วินาที)

    // Constructor สำหรับสร้างออบเจ็กต์ศัตรู โดยกำหนดตำแหน่ง ความเร็ว ขนาด และตั้งค่าเริ่มต้นต่าง ๆ
    public Enemy(double x, double y, double speed, double size) {
        super(x, y, speed, size);
        this.angle = 0; // กำหนดมุมเริ่มต้นเป็น 0
        this.Enemylives = 2; // กำหนดจำนวนชีวิตเริ่มต้นของศัตรูเป็น 2
        EnemylivesTest = 2; // กำหนดค่าเริ่มต้นสำหรับตัวแปรทดสอบชีวิตศัตรู
        animation = new SpriteAnimation("/Sprite Asset/Enemy02p.png", 2, 3, 75_000_000);  // กำหนด SpriteAnimation ของศัตรู
        bullets = new ArrayList<>(); // สร้างลิสต์สำหรับเก็บกระสุนของศัตรู
    }

    // เมธอดสำหรับการเคลื่อนที่ของศัตรู
    @Override
    public void move() {
        // คำนวณตำแหน่งใหม่ของศัตรูโดยใช้ความเร็วและมุม
        x += speed * Math.cos(Math.toRadians(angle)) + (Math.random() - 0.5) * 2; // เพิ่มความสุ่มเล็กน้อยให้กับการเคลื่อนที่
        y += speed + (Math.random() - 0.5) * 2; // เพิ่มความสุ่มเล็กน้อยให้กับการเคลื่อนที่แนวตั้ง
        angle += (Math.random() - 0.5) * 5; // ปรับมุมให้มีการเปลี่ยนแปลงเล็กน้อยเพื่อการเคลื่อนที่แบบสุ่ม

        long currentTime = System.nanoTime();
        // ตรวจสอบว่าผ่านไปนานพอสำหรับการยิงกระสุนใหม่หรือไม่
        if (currentTime - lastShotTime >= shootInterval) {
            EnemyShoot(); // ยิงกระสุนใหม่
            lastShotTime = currentTime; // อัปเดตเวลายิงล่าสุด
        }
    }

    // เมธอดสำหรับยิงกระสุนของศัตรู
    public void EnemyShoot() {
        EnemyBullet bullet = EnemyBullet.EnemyShoot(this); // สร้างกระสุนใหม่จากตำแหน่งของศัตรู
        bullets.add(bullet); // เพิ่มกระสุนลงในลิสต์
    }

    // เมธอดสำหรับวาดภาพศัตรูบน GraphicsContext
    @Override
    public void draw(GraphicsContext gc) {
        gc.save(); // บันทึกสถานะของ canvas
        gc.translate(x, y); // ย้าย canvas ไปที่ตำแหน่งของศัตรู
        gc.rotate(angle); // หมุน canvas ตามมุมของศัตรู

        long currentTime = System.nanoTime();
        animation.update(currentTime); // อัปเดตเฟรมของแอนิเมชัน

        double scaleFactor = 2.0; // ปรับขนาดของศัตรูให้ใหญ่ขึ้น
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor); // วาดแอนิเมชันของศัตรู
        gc.restore(); // คืนค่าคอนเท็กซ์ canvas หลังจากวาดเสร็จ
    }

    // เมธอดสำหรับลดจำนวนชีวิตของศัตรูเมื่อถูกโจมตี
    public void EnemyTakingDamage(int damage) {
        if (Enemylives > 0) {
            Enemylives -= damage; // ลดจำนวนชีวิตตามค่าความเสียหายที่ได้รับ
            EnemylivesTest = Enemylives; // อัปเดตตัวแปรทดสอบชีวิตของศัตรู
        }
    }

    // เมธอดสำหรับตรวจสอบว่าศัตรูถูกกำจัดแล้วหรือไม่
    public boolean EnemyIsEliminated() {
        return Enemylives <= 0; // คืนค่า true ถ้าศัตรูไม่มีชีวิตเหลือ
    }

    // เมธอดสำหรับรับค่ามุมปัจจุบันของศัตรู
    public double getAngle() {
        return angle;
    }

    // เมธอด static สำหรับรับค่าจำนวนชีวิตปัจจุบันของศัตรู (ใช้สำหรับการทดสอบ)
    public static int getEnemylives() {
        return EnemylivesTest;
    }
}
