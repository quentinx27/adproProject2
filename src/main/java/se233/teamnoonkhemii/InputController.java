package se233.teamnoonkhemii;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputController {
    // ตัวแปรเก็บสถานะของการเคลื่อนไหว การยิง และการใช้งานพิเศษต่างๆ
    private boolean moveLeft, moveRight, moveUp, moveDown, shooting, ultimate, developerCheat, gameOver;
    private long lastShootingTime = 0; // เวลาในการยิงครั้งสุดท้าย
    private long lastUltimateTime = 0; // เวลาในการใช้อัลติเมตครั้งสุดท้าย
    private long lastDeveloperCheatTime = 0; // เวลาในการใช้โหมด developer cheat ครั้งสุดท้าย

    // Constructor สำหรับสร้าง InputController โดยรับ Scene เพื่อรับการอินพุตจากผู้ใช้
    public InputController(Scene scene) {
        // การกดปุ่มเพื่อควบคุมการเคลื่อนไหว
        scene.setOnKeyPressed(event -> {
            long currentTime = System.nanoTime(); // รับเวลาในหน่วยนาโนวินาทีปัจจุบัน
            if (event.getCode() == KeyCode.A) {
                moveLeft = true;
                moveRight = false; // ปิดการทำงานของการขวาเมื่อกดซ้าย
            } else if (event.getCode() == KeyCode.D) {
                moveRight = true;
                moveLeft = false; // ปิดการทำงานของการซ้ายเมื่อกดขวา
            } else if (event.getCode() == KeyCode.W) {
                moveUp = true;
                moveDown = false; // ปิดการทำงานของการลงเมื่อกดขึ้น
            } else if (event.getCode() == KeyCode.S) {
                moveDown = true;
                moveUp = false; // ปิดการทำงานของการขึ้นเมื่อกดลง
            } else if (event.getCode() == KeyCode.G) {
                developerCheat = true; // เปิดใช้งานโหมด developer cheat
                lastDeveloperCheatTime = currentTime; // บันทึกเวลาในการเปิดใช้งาน
            } else if (event.getCode() == KeyCode.O) {
                gameOver = true; // กดเพื่อจบเกม
            }
        });

        // การปล่อยปุ่มเมื่อหยุดการเคลื่อนไหว
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = false; // หยุดการเคลื่อนไหวซ้าย
            if (event.getCode() == KeyCode.D) moveRight = false; // หยุดการเคลื่อนไหวขวา
            if (event.getCode() == KeyCode.W) moveUp = false; // หยุดการเคลื่อนไหวขึ้น
            if (event.getCode() == KeyCode.S) moveDown = false; // หยุดการเคลื่อนไหวลง
            if (event.getCode() == KeyCode.G) developerCheat = false; // ปิดโหมด developer cheat
            if (event.getCode() == KeyCode.O) gameOver = false; // หยุดการจบเกม
        });

        // การคลิกเมาส์เพื่อยิงหรือใช้อัลติเมต
        scene.setOnMousePressed(event -> {
            long currentTime = System.nanoTime(); // รับเวลาในหน่วยนาโนวินาทีปัจจุบัน
            if (event.getButton() == MouseButton.PRIMARY) {
                shooting = true; // เปิดใช้งานการยิง
                lastShootingTime = currentTime; // บันทึกเวลาเริ่มต้นการยิง
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                ultimate = true; // เปิดใช้งานอัลติเมต
                lastUltimateTime = currentTime; // บันทึกเวลาเริ่มต้นการใช้อัลติเมต
            }
        });

        // การปล่อยปุ่มเมาส์เมื่อหยุดการยิงหรือใช้อัลติเมต
        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) shooting = false; // หยุดการยิง
            if (event.getButton() == MouseButton.SECONDARY) ultimate = false; // หยุดใช้อัลติเมต
        });
    }

    // เมธอดสำหรับอัปเดตสถานะของการยิงและการใช้งานพิเศษ
    public void update(long currentTime) {
        // ตรวจสอบระยะเวลาที่ผ่านไปตั้งแต่เริ่มยิงเพื่อหยุดการยิงอัตโนมัติ
        long shootingActiveTime = 30_000_000; // กำหนดเวลาที่การยิงจะหยุดเอง (30ms)
        if (shooting && (currentTime - lastShootingTime >= shootingActiveTime)) {
            shooting = false; // หยุดการยิง
        }

        // ตรวจสอบระยะเวลาการใช้อัลติเมตเพื่อหยุดอัตโนมัติ
        long ultimateActiveTime = 10_000_000; // กำหนดเวลาที่การใช้อัลติเมตจะหยุดเอง (10ms)
        if (ultimate && (currentTime - lastUltimateTime >= ultimateActiveTime)) {
            ultimate = false; // หยุดใช้อัลติเมต
        }

        // ตรวจสอบระยะเวลาการใช้โหมด developer cheat เพื่อหยุดอัตโนมัติ
        long developerCheatActiveTime = 10_000_000; // กำหนดเวลาที่โหมด developer cheat จะหยุดเอง (10ms)
        if (developerCheat && (currentTime - lastDeveloperCheatTime >= developerCheatActiveTime)) {
            developerCheat = false; // ปิดโหมด developer cheat
        }
    }

    // เมธอดตรวจสอบสถานะของการกดปุ่มซ้าย
    public boolean isMoveLeftPressed() { return moveLeft; }

    // เมธอดตรวจสอบสถานะของการกดปุ่มขวา
    public boolean isMoveRightPressed() { return moveRight; }

    // เมธอดตรวจสอบสถานะของการกดปุ่มขึ้น
    public boolean isMoveUpPressed() { return moveUp; }

    // เมธอดตรวจสอบสถานะของการกดปุ่มลง
    public boolean isMoveDownPressed() { return moveDown; }

    // เมธอดตรวจสอบสถานะของการกดปุ่มยิง
    public boolean isShootingPressed() { return shooting; }

    // เมธอดตรวจสอบสถานะของการใช้อัลติเมต
    public boolean isUltimatePressed() { return ultimate; }

    // เมธอดตรวจสอบสถานะของการเปิดใช้งานโหมด developer cheat
    public boolean isDeveloperCheat() { return developerCheat; }

    // เมธอดตรวจสอบสถานะของการกดปุ่มจบเกม
    public boolean isGameOverKeyboard() { return gameOver; }
}
