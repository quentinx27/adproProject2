package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Bullet {
    protected double x, y;
    protected double speed;
    protected double angle;
    protected double size;
    protected boolean isActive;

    public Bullet(double x, double y, double speed, double angle, double size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
        this.size = size;
        this.isActive = true; // กระสุนเริ่มต้นด้วยการเปิดใช้งาน
    }

    // การเคลื่อนที่ของกระสุน
    public void move() {
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));
    }

    // วาดกระสุนลงใน canvas
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW); // เปลี่ยนสีของกระสุนหากต้องการ
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    // ตรวจสอบว่ากระสุนยังคงอยู่ในหน้าจอหรือไม่
    public boolean isOutOfScreen(double screenWidth, double screenHeight) {
        return (x < 0 || x > screenWidth || y < 0 || y > screenHeight);
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}
