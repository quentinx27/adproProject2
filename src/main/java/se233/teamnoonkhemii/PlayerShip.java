package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShip extends Character {
    private double angle;
    private int PlayerShiplives;  // จำนวนชีวิตของผู้เล่น
    private static int score;  // คะแนนของผู้เล่น
    private SpriteAnimation animation;

    public PlayerShip(double x, double y, double speed, double size) {
        super(x, y, speed, size);
        this.angle = 0; // Initial angle
        this.PlayerShiplives = 5; // เริ่มต้นด้วยชีวิต 5
        this.score = 0; // เริ่มต้นด้วยคะแนน 0

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

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    // เพิ่มเมธอดสำหรับการจัดการชีวิต
    public int getPlayerShipLives() {
        return PlayerShiplives;
    }

    public void resetPlayerShipLives() {
       PlayerShiplives = 5;
    }

    public void PlayerShipTakingDamage(int damage) {
        if (PlayerShiplives > 0) {
            PlayerShiplives -= damage;
        }
    }

    public void gainLife() {
        PlayerShiplives++;
    }

    // เพิ่มเมธอดสำหรับการจัดการคะแนน
    public int getScore() {
        return score;
    }

    public static void addScore(int points) {
        score += points;
    }

    public void resetScore() {
        score = 0;
    }

    public boolean isAlive() {
        return PlayerShiplives > 0;
    }

    public boolean isGameOver() {
        return PlayerShiplives == 0;
    }

    // ตรวจสอบการชนระหว่าง PlayerShip กับ Asteroid
    public boolean collidesWith(Asteroid asteroid) {
        double dx = x - asteroid.getX();
        double dy = y - asteroid.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + asteroid.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่าง PlayerShip กับ Enemy
    public boolean collidesWith(Enemy enemy) {
        double dx = x - enemy.getX();
        double dy = y - enemy.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + enemy.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }
}
