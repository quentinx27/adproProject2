package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Heal extends Character {
    private SpriteAnimation healAnimation; // SpriteAnimation สำหรับ Heal

    public Heal(double x, double y, double size) {
        super(x, y, 0, size); // กำหนด speed เป็น 0 เพราะ Heal จะอยู่กับที่
        // สร้าง SpriteAnimation สำหรับ Heal โดยกำหนดพาธของภาพ, แถว, คอลัมน์ และระยะเวลาของเฟรม
        this.healAnimation = new SpriteAnimation("/Sprite Asset/HealSheet.png", 3, 3, 125_000_000); // Assume 3 rows, 3 columns, 125ms per frame
    }

    @Override
    public void move() {
        long currentTime = System.nanoTime();
        // อัปเดตแอนิเมชันของ Heal
        healAnimation.update(currentTime);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        // แปลตำแหน่งของ canvas ไปยังตำแหน่งของ Heal
        gc.translate(x, y);

        // ใช้ scaleFactor เพื่อปรับขนาดของ Heal
        double scaleFactor = 2.5;
        double scaledSize = size * scaleFactor;

        // วาดแอนิเมชันของ Heal โดยใช้ scaleFactor เพื่อปรับขนาด
        healAnimation.render(gc, -scaledSize / 2, -scaledSize / 2, scaledSize, scaledSize);

        gc.restore();
    }
}
