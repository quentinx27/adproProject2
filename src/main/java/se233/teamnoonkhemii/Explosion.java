package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Explosion extends Character {
    private SpriteAnimation explosionAnimation; // SpriteAnimation สำหรับ Explosion
    private long startTime; // เวลาที่เริ่มต้นของแอนิเมชัน
    private long duration = 300_000_000; // ระยะเวลาของแอนิเมชัน (300 milliseconds)

    // Constructor สำหรับสร้าง Explosion โดยกำหนดตำแหน่งและขนาด
    public Explosion(double x, double y, double size) {
        super(x, y, 0, size); // เรียก constructor ของคลาส Character โดยกำหนดความเร็วเป็น 0 (เนื่องจาก Explosion ไม่เคลื่อนที่)
        // สร้าง SpriteAnimation สำหรับ Explosion
        this.explosionAnimation = new SpriteAnimation("/Sprite Asset/BigBrokenSheet.png", 1, 3, 125_000_000);
        this.startTime = System.nanoTime(); // บันทึกเวลาที่เริ่มต้นของแอนิเมชัน
    }

    // เมธอดสำหรับอัปเดตการเคลื่อนไหวของ Explosion
    @Override
    public void move() {
        long currentTime = System.nanoTime(); // รับค่าเวลาปัจจุบัน
        // อัปเดตแอนิเมชันของ Explosion โดยอิงจากเวลาปัจจุบัน
        explosionAnimation.update(currentTime);
    }

    // เมธอดสำหรับวาด Explosion ลงใน GraphicsContext
    @Override
    public void draw(GraphicsContext gc) {
        gc.save(); // บันทึกสถานะของ canvas

        // แปลตำแหน่งของ canvas ไปยังตำแหน่งของ Explosion
        gc.translate(x, y);

        // ใช้ scaleFactor เพื่อปรับขนาดของ Explosion
        double scaleFactor = 2.5;
        double scaledSize = size * scaleFactor;

        // วาดแอนิเมชันของ Explosion โดยใช้ scaleFactor เพื่อปรับขนาด
        explosionAnimation.render(gc, -scaledSize / 2, -scaledSize / 2, scaledSize, scaledSize);

        gc.restore(); // คืนค่าคอนเท็กซ์ canvas หลังจากวาดเสร็จ
    }

    // เมธอดสำหรับตรวจสอบว่าแอนิเมชันสิ้นสุดแล้วหรือยัง
    public boolean isAnimationFinished() {
        // คำนวณระยะเวลาตั้งแต่เริ่มต้นแอนิเมชัน
        long elapsedTime = System.nanoTime() - startTime;
        // หากเวลาที่ผ่านไปมากกว่าหรือเท่ากับ duration ให้คืนค่า true (แอนิเมชันสิ้นสุด)
        return elapsedTime >= duration;
    }
}
