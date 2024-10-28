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
        x += Math.cos(Math.toRadians(angle - 90)) * speed;
        y += Math.sin(Math.toRadians(angle - 90)) * speed;
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

    // ตรวจสอบการชนระหว่าง Bullet และ Asteroid
    public boolean collidesWith(Asteroid asteroid) {
        double dx = x - asteroid.getX();
        double dy = y - asteroid.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + asteroid.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่าง Bullet และ Enemy
    public boolean collidesWith(Enemy enemy) {
        double dx = x - enemy.getX();
        double dy = y - enemy.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + enemy.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่าง Bullet และ Enemy
    public boolean collidesWith(PlayerShip playerShip) {
        double dx = x - playerShip.getX();
        double dy = y - playerShip.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + playerShip.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่าง BossBullet และ Boss
    public boolean collidesWith(Boss boss) {
        double dx = x - boss.getX();
        double dy = y - boss.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + boss.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }
}
