package se233.teamnoonkhemii;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputController {
    private boolean moveLeft, moveRight, moveUp, moveDown, shooting, ultimate, developerMode;
    private long lastShootingTime = 0;
    private long lastUltimateTime = 0;

    public InputController(Scene scene) {
        // Key press events for movement
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = true;
            if (event.getCode() == KeyCode.D) moveRight = true;
            if (event.getCode() == KeyCode.W) moveUp = true;
            if (event.getCode() == KeyCode.S) moveDown = true;
            if (event.getCode() == KeyCode.G) developerMode = true;
        });

        // Key release events
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = false;
            if (event.getCode() == KeyCode.D) moveRight = false;
            if (event.getCode() == KeyCode.W) moveUp = false;
            if (event.getCode() == KeyCode.S) moveDown = false;
            if (event.getCode() == KeyCode.G) developerMode = false;
        });

        // Mouse press events for shooting and ultimate
        scene.setOnMousePressed(event -> {
            long currentTime = System.nanoTime();
            if (event.getButton() == MouseButton.PRIMARY) {
                shooting = true;
                lastShootingTime = currentTime; // บันทึกเวลาเริ่มต้นการยิง
            }
            if (event.getButton() == MouseButton.SECONDARY) {
                ultimate = true;
                lastUltimateTime = currentTime; // บันทึกเวลาเริ่มต้นการใช้อัลติเมต
            }
        });

        // Mouse release events
        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) shooting = false;
            if (event.getButton() == MouseButton.SECONDARY) ultimate = false;
        });
    }

    public void update(long currentTime) {
        // ตรวจสอบระยะเวลาการยิง
        long shootingActiveTime = 30_000_000;
        if (shooting && (currentTime - lastShootingTime >= shootingActiveTime)) {
            shooting = false;
        }

        // ตรวจสอบระยะเวลาการใช้อัลติเมต
        long ultimateActiveTime = 10_000_000;
        if (ultimate && (currentTime - lastUltimateTime >= ultimateActiveTime)) {
            ultimate = false;
        }
    }

    public boolean isMoveLeftPressed() { return moveLeft; }
    public boolean isMoveRightPressed() { return moveRight; }
    public boolean isMoveUpPressed() { return moveUp; }
    public boolean isMoveDownPressed() { return moveDown; }
    public boolean isShootingPressed() { return shooting; }
    public boolean isUltimatePressed() { return ultimate; }
    public boolean isDeveloperMode() { return developerMode; }
}
