package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Heal extends Character {
    private SpriteAnimation healAnimation; // SpriteAnimation สำหรับ Heal

    // Constructor สำหรับสร้างออบเจ็กต์ Heal โดยกำหนดตำแหน่งและขนาด
    public Heal(double x, double y, double size) {
        // เรียก constructor ของคลาส Character โดยกำหนด speed เป็น 0 เพราะ Heal จะอยู่กับที่
        super(x, y, 0, size);
        // สร้าง SpriteAnimation สำหรับ Heal โดยกำหนดพาธของภาพ, แถว, คอลัมน์ และระยะเวลาของเฟรม
        this.healAnimation = new SpriteAnimation("/Sprite Asset/HealSheet.png", 3, 3, 125_000_000);
        // สมมติว่า sprite sheet มี 3 แถว, 3 คอลัมน์ และแต่ละเฟรมแสดงผลนาน 125ms
    }

    // เมธอดสำหรับอัปเดตการเคลื่อนไหวของ Heal
    @Override
    public void move() {
        long currentTime = System.nanoTime(); // รับค่าเวลาปัจจุบัน
        // อัปเดตแอนิเมชันของ Heal โดยอิงจากเวลาปัจจุบัน
        healAnimation.update(currentTime);
    }

    // เมธอดสำหรับวาด Heal ลงใน GraphicsContext
    @Override
    public void draw(GraphicsContext gc) {
        gc.save(); // บันทึกสถานะของ canvas

        // แปลตำแหน่งของ canvas ไปยังตำแหน่งของ Heal
        gc.translate(x, y);

        // ใช้ scaleFactor เพื่อปรับขนาดของ Heal
        double scaleFactor = 2.5;
        double scaledSize = size * scaleFactor;

        // วาดแอนิเมชันของ Heal โดยใช้ scaleFactor เพื่อปรับขนาดให้ใหญ่ขึ้น
        healAnimation.render(gc, -scaledSize / 2, -scaledSize / 2, scaledSize, scaledSize);

        gc.restore(); // คืนค่าคอนเท็กซ์ canvas หลังจากวาดเสร็จ
    }
}
