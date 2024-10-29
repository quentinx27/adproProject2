package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public abstract class Character {

    protected double x, y; // ตำแหน่งของออบเจ็กต์บนแกน X และ Y
    protected double speed; // ความเร็วของออบเจ็กต์
    protected double size; // ขนาดของออบเจ็กต์

    // Constructor สำหรับกำหนดตำแหน่ง ความเร็ว และขนาดเริ่มต้นของออบเจ็กต์
    public Character(double x, double y, double speed, double size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.size = size;
    }

    // เมธอดแบบ abstract สำหรับการเคลื่อนที่ ซึ่งจะถูกกำหนดรายละเอียดในคลาสที่สืบทอด
    public abstract void move();

    // เมธอดสำหรับวาดออบเจ็กต์ลงบน GraphicsContext
    public void draw(GraphicsContext gc) {
        // สามารถใส่ลอจิกการวาดทั่วไปที่นี่ แต่ส่วนมากจะถูก override ในคลาสที่สืบทอด
    }

    // เมธอดสำหรับรับค่า X ตำแหน่งของออบเจ็กต์
    public double getX() {
        return x;
    }

    // เมธอดสำหรับกำหนดค่า X ตำแหน่งของออบเจ็กต์
    public void setX(double x) {
        this.x = x;
    }

    // เมธอดสำหรับรับค่า Y ตำแหน่งของออบเจ็กต์
    public double getY() {
        return y;
    }

    // เมธอดสำหรับกำหนดค่า Y ตำแหน่งของออบเจ็กต์
    public void setY(double y) {
        this.y = y;
    }

    // เมธอดสำหรับรับค่าขนาดของออบเจ็กต์
    public double getSize() {
        return size;
    }

    // เมธอดสำหรับกำหนดขนาดของออบเจ็กต์
    public void setSize(double size) {
        this.size = size;
    }

    // เมธอดสำหรับตรวจสอบว่าออบเจ็กต์อยู่นอกจอหรือไม่
    // คืนค่า true ถ้าออบเจ็กต์อยู่นอกขอบจอ โดยพิจารณาจากขนาดของมัน
    public boolean isOutOfScreen(double screenWidth, double screenHeight) {
        return x < -size || x > screenWidth + size || y < -size || y > screenHeight + size;
    }
}
