package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShip extends Character {
    private double angle;
    private SpriteAnimation animation;

    public PlayerShip(double x, double y, double speed, double size) {
        super(x, y, speed, size);
        this.angle = 0; // Initial angle

        // Initialize the sprite animation with the sprite sheet path
        animation = new SpriteAnimation("/Sprite Asset/yanSheet.png", 3, 3, 125_000_000);  // 3x3 grid, 100 ms per frame
    }

    @Override
    public void move() {
        // Movement logic based on current angle and speed
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Clear the previous ship image position before redrawing
        gc.save();  // Save the current state of the canvas

        // Move to the player's position and rotate
        gc.translate(x, y);
        gc.rotate(angle);

        // Update and render the current frame of the sprite animation
        long currentTime = System.nanoTime();
        animation.update(currentTime);  // Update the frame
        double scaleFactor = 3.0;  // ขยายขนาดยานให้ใหญ่ขึ้น (ปรับจาก 3.0 เป็น 6.0)
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);  // Draw the current frame

        gc.restore();  // Restore the previous state of the canvas
    }

    public void rotateLeft() {
        angle -= 5;  // Rotate left by 5 degrees
    }

    public void rotateRight() {
        angle += 5;  // Rotate right by 5 degrees
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }
}
