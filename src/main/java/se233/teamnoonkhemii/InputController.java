package se233.teamnoonkhemii;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputController {
    private boolean moveLeft, moveRight, moveUp, moveDown, rotateLeft,rotateRight, shooting, ultimate;
    private boolean godMode;

    public InputController(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = true;
            if (event.getCode() == KeyCode.D) moveRight = true;
            if (event.getCode() == KeyCode.W) moveUp = true;
            if (event.getCode() == KeyCode.S) moveDown = true;
            if (event.getCode() == KeyCode.Q) rotateLeft = true;
            if (event.getCode() == KeyCode.E) rotateRight = true;
            if (event.getCode() == KeyCode.SPACE) shooting = true;
            if (event.getCode() == KeyCode.R) ultimate = true;
            if (event.getCode() == KeyCode.G) godMode = true;
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.A) moveLeft = false;
            if (event.getCode() == KeyCode.D) moveRight = false;
            if (event.getCode() == KeyCode.W) moveUp = false;
            if (event.getCode() == KeyCode.S) moveDown = false;
            if (event.getCode() == KeyCode.Q) rotateLeft = false;
            if (event.getCode() == KeyCode.E) rotateRight = false;
            if (event.getCode() == KeyCode.SPACE) shooting = false;
            if (event.getCode() == KeyCode.R) ultimate = false;
            if (event.getCode() == KeyCode.G) godMode = false;

        });
    }

    public boolean isMoveLeftPressed() {
        return moveLeft;
    }

    public boolean isRotateLeftPressed() {
        return rotateLeft;
    }

    public boolean isMoveRightPressed() {
        return moveRight;
    }

    public boolean isRotateRightPressed() {
        return rotateRight;
    }

    public boolean isMoveUpPressed() {
        return moveUp;
    }

    public boolean isMoveDownPressed() {
        return moveDown;
    }

    public boolean isShootingPressed() {
        return shooting;
    }

    public boolean isUltimatePressed() {
        return ultimate;
    }

    public boolean isGodModeEnabled() {
        return godMode;
    }
}
