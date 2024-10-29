package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SpriteAnimation {
    private Image spriteSheet;        // ภาพ sprite sheet ทั้งหมดที่ใช้สำหรับการสร้างแอนิเมชัน
    private WritableImage[] frames;   // อาร์เรย์ที่เก็บเฟรมแต่ละเฟรมที่ดึงออกมาจาก sprite sheet
    private int currentFrame = 0;     // ดัชนีของเฟรมปัจจุบันที่จะแสดง
    private long lastFrameTime = 0;   // เวลาที่เฟรมสุดท้ายถูกแสดง (ใช้ในการควบคุมการเปลี่ยนเฟรม)
    private long frameDuration;       // ระยะเวลาของแต่ละเฟรม (หน่วยเป็นนาโนวินาที)
    private int totalFrames;          // จำนวนเฟรมทั้งหมดใน sprite sheet

    // คอนสตรัคเตอร์สำหรับสร้าง SpriteAnimation โดยใช้พาธของภาพ, จำนวนแถวและคอลัมน์ และระยะเวลาของเฟรม
    public SpriteAnimation(String imagePath, int rows, int columns, long frameDuration) {
        // โหลดภาพ sprite sheet
        spriteSheet = new Image(getClass().getResourceAsStream(imagePath));

        // คำนวณขนาดของแต่ละเฟรม
        int frameWidth = (int) spriteSheet.getWidth() / columns;
        int frameHeight = (int) spriteSheet.getHeight() / rows;

        // คำนวณจำนวนเฟรมทั้งหมด
        totalFrames = rows * columns;
        frames = new WritableImage[totalFrames];

        // ดึงเฟรมแต่ละเฟรมจาก sprite sheet และเก็บในอาร์เรย์
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                frames[row * columns + col] = new WritableImage(
                        spriteSheet.getPixelReader(),
                        col * frameWidth,
                        row * frameHeight,
                        frameWidth,
                        frameHeight
                );
            }
        }

        // กำหนดระยะเวลาของแต่ละเฟรม
        this.frameDuration = frameDuration;
    }

    // เมธอด update ใช้สำหรับอัปเดตเฟรมตามเวลาปัจจุบัน
    public void update(long currentTime) {
        // ตรวจสอบว่าเวลาปัจจุบันห่างจากเวลาที่เฟรมสุดท้ายแสดงนานกว่า frameDuration หรือไม่
        if (currentTime - lastFrameTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % totalFrames;  // เปลี่ยนไปยังเฟรมถัดไป (วนกลับไปเฟรมแรกเมื่อถึงเฟรมสุดท้าย)
            lastFrameTime = currentTime; // อัปเดตเวลาที่เฟรมสุดท้ายถูกแสดง
        }
    }

    // เมธอด render ใช้สำหรับวาดเฟรมปัจจุบันลงใน GraphicsContext
    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        WritableImage currentSprite = frames[currentFrame];  // ดึงเฟรมปัจจุบันมาแสดง
        gc.drawImage(currentSprite, x, y, width, height);    // วาดภาพเฟรมที่ตำแหน่งที่กำหนด
    }
}
