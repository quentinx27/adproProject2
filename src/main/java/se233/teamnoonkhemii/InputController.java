package se233.teamnoonkhemii;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputController {
    private boolean moveLeft, moveRight, moveUp, moveDown, shooting, ultimate, godMode;
    private double mouseAngle;
    // Add reference to PlayerShip

    public InputController(Scene scene, PlayerShip playerShip) {

        // Key press events for movement
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = true;
            if (event.getCode() == KeyCode.D) moveRight = true;
            if (event.getCode() == KeyCode.W) moveUp = true;
            if (event.getCode() == KeyCode.S) moveDown = true;
            if (event.getCode() == KeyCode.SPACE) shooting = true;
            if (event.getCode() == KeyCode.R) ultimate = true;
            if (event.getCode() == KeyCode.G) godMode = true;
        });

        // Key release events
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = false;
            if (event.getCode() == KeyCode.D) moveRight = false;
            if (event.getCode() == KeyCode.W) moveUp = false;
            if (event.getCode() == KeyCode.S) moveDown = false;
            if (event.getCode() == KeyCode.SPACE) shooting = false;
            if (event.getCode() == KeyCode.R) ultimate = false;
            if (event.getCode() == KeyCode.G) godMode = false;
        });

        // Track mouse position for aiming
        scene.setOnMouseMoved(event -> {
            // Calculate angle based on player's position
            mouseAngle = Math.toDegrees(Math.atan2(event.getY() - playerShip.getY(), event.getX() - playerShip.getX()));
        });
    }

    public boolean isMoveLeftPressed() { return moveLeft; }
    public boolean isMoveRightPressed() { return moveRight; }
    public boolean isMoveUpPressed() { return moveUp; }
    public boolean isMoveDownPressed() { return moveDown; }
    public boolean isShootingPressed() { return shooting; }
    public boolean isUltimatePressed() { return ultimate; }
    public boolean isGodModeEnabled() { return godMode; }
    public double getMouseAngle() { return mouseAngle; }
}
