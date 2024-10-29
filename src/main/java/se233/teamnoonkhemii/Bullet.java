package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Bullet {
    protected double x, y; // ตำแหน่งของกระสุนบนแกน X และ Y
    protected double speed; // ความเร็วของกระสุน
    protected double angle; // มุมการเคลื่อนที่ของกระสุน
    protected double size; // ขนาดของกระสุน
    protected boolean isActive; // สถานะของกระสุน ว่าถูกใช้งานอยู่หรือไม่

    // Constructor สำหรับสร้าง Bullet ด้วยตำแหน่ง ความเร็ว มุม และขนาดที่กำหนด
    public Bullet(double x, double y, double speed, double angle, double size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
        this.size = size;
        this.isActive = true; // กระสุนเริ่มต้นด้วยการเปิดใช้งาน
    }

    // เมธอดสำหรับการเคลื่อนที่ของกระสุน
    public void move() {
        // คำนวณการเปลี่ยนแปลงตำแหน่งของกระสุนตามมุมและความเร็ว
        x += Math.cos(Math.toRadians(angle - 90)) * speed;
        y += Math.sin(Math.toRadians(angle - 90)) * speed;
    }

    // เมธอดสำหรับวาดกระสุนลงใน canvas
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW); // กำหนดสีของกระสุน
        // วาดรูปวงกลมที่ตำแหน่งและขนาดที่กำหนด
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    // เมธอดสำหรับตรวจสอบว่ากระสุนยังคงอยู่ในหน้าจอหรือไม่
    public boolean isOutOfScreen(double screenWidth, double screenHeight) {
        // หากกระสุนหลุดออกนอกขอบจอ ให้คืนค่าเป็น true
        return (x < 0 || x > screenWidth || y < 0 || y > screenHeight);
    }

    // เมธอดสำหรับตรวจสอบสถานะของกระสุน ว่ายังคงเปิดใช้งานอยู่หรือไม่
    public boolean isActive() {
        return isActive;
    }

    // เมธอดสำหรับปิดการใช้งานกระสุน
    public void deactivate() {
        isActive = false;
    }

    // เมธอดสำหรับตรวจสอบการชนระหว่าง Bullet และ Asteroid
    public boolean collidesWith(Asteroid asteroid) {
        // คำนวณระยะห่างระหว่าง Bullet และ Asteroid
        double dx = x - asteroid.getX();
        double dy = y - asteroid.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        // ตรวจสอบว่าระยะห่างน้อยกว่าผลรวมของรัศมีของกระสุนและ Asteroid หรือไม่
        return distance < (size / 2 + asteroid.getSize() / 2);
    }

    // เมธอดสำหรับตรวจสอบการชนระหว่าง Bullet และ Enemy
    public boolean collidesWith(Enemy enemy) {
        // คำนวณระยะห่างระหว่าง Bullet และ Enemy
        double dx = x - enemy.getX();
        double dy = y - enemy.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        // ตรวจสอบว่าระยะห่างน้อยกว่าผลรวมของรัศมีของกระสุนและ Enemy หรือไม่
        return distance < (size / 2 + enemy.getSize() / 2);
    }

    // เมธอดสำหรับตรวจสอบการชนระหว่าง Bullet และ PlayerShip
    public boolean collidesWith(PlayerShip playerShip) {
        // คำนวณระยะห่างระหว่าง Bullet และ PlayerShip
        double dx = x - playerShip.getX();
        double dy = y - playerShip.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        // ตรวจสอบว่าระยะห่างน้อยกว่าผลรวมของรัศมีของกระสุนและ PlayerShip หรือไม่
        return distance < (size / 2 + playerShip.getSize() / 2);
    }

    // เมธอดสำหรับตรวจสอบการชนระหว่าง Bullet และ Boss
    public boolean collidesWith(Boss boss) {
        // คำนวณระยะห่างระหว่าง Bullet และ Boss
        double dx = x - boss.getX();
        double dy = y - boss.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        // ตรวจสอบว่าระยะห่างน้อยกว่าผลรวมของรัศมีของกระสุนและ Boss หรือไม่
        return distance < (size / 2 + boss.getSize() / 2);
    }

    // เมธอดสำหรับคืนค่าตำแหน่ง X ของกระสุน
    public double getX() {
        return x;
    }

    // เมธอดสำหรับคืนค่าตำแหน่ง Y ของกระสุน
    public double getY() {
        return y;
    }
}
