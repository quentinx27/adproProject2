package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Explosion extends Character {
    private SpriteAnimation explosionAnimation; // SpriteAnimation สำหรับ Explosion
    private long startTime; // เวลาที่เริ่มต้นของแอนิเมชัน
    private long duration = 300_000_000; // ระยะเวลาของแอนิเมชัน (1 second)

    public Explosion(double x, double y, double size) {
        super(x, y, 0, size);
        // สร้าง SpriteAnimation สำหรับ Explosion
        this.explosionAnimation = new SpriteAnimation("/Sprite Asset/BigBrokenSheet.png", 1, 3, 125_000_000);
        this.startTime = System.nanoTime();
    }

    @Override
    public void move() {
        long currentTime = System.nanoTime();
        // อัปเดตแอนิเมชันของ Explosion
        explosionAnimation.update(currentTime);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();

        // แปลตำแหน่งของ canvas ไปยังตำแหน่งของ Explosion
        gc.translate(x, y);

        // ใช้ scaleFactor เพื่อปรับขนาดของ Explosion
        double scaleFactor = 2.5;
        double scaledSize = size * scaleFactor;

        // วาดแอนิเมชันของ Explosion โดยใช้ scaleFactor เพื่อปรับขนาด
        explosionAnimation.render(gc, -scaledSize / 2, -scaledSize / 2, scaledSize, scaledSize);

        gc.restore();
    }

    public boolean isAnimationFinished() {
        // คำนวณระยะเวลาตั้งแต่เริ่มต้นแอนิเมชัน
        long elapsedTime = System.nanoTime() - startTime;
        return elapsedTime >= duration;
    }

}
