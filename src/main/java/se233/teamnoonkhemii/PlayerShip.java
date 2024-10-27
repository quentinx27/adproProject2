package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShip extends Character {
    private double angle;
    private static int PlayerShiplives;
    private static int PlayerShipBossEliminated;
    private static int score;
    private SpriteAnimation animation;

    public PlayerShip(double x, double y, double speed, double size) {
        super(x, y, speed, size);
        this.angle = 0; // Initial angle
        this.PlayerShiplives = 5; // เริ่มต้นด้วยชีวิต 5
        this.PlayerShipBossEliminated = 0;
        this.score = 0; // เริ่มต้นด้วยคะแนน 0

        // Initialize the sprite animation with the sprite sheet path
        animation = new SpriteAnimation("/Sprite Asset/xWing01.png", 2, 2, 125_000_000);  // 3x3 grid, 100 ms per frame
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
        double scaleFactor = 5.0;  // ขยายขนาดยานให้ใหญ่ขึ้น (ปรับจาก 3.0 เป็น 6.0)
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

    public static void resetPlayerShipLives() {
       PlayerShiplives = 5;
    }

    public void PlayerShipTakingDamage(int damage) {
        if (PlayerShiplives > 0) {
            PlayerShiplives -= damage;
        }
    }

    public int getPlayerShipBossEliminated() {
        return PlayerShipBossEliminated;
    }

    public static void addPlayerShipBossEliminated(int mark) {
        PlayerShipBossEliminated += mark;
    }

    public static void resetPlayerShipBossEliminated() {
        PlayerShipBossEliminated = 0;
    }

    public void addLives(int lives) {
        PlayerShiplives+= lives;
    }

    // เพิ่มเมธอดสำหรับการจัดการคะแนน
    public int getScore() {
        return score;
    }

    public static void addScore(int points) {
        score += points;
    }

    public static void resetScore() {
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

    // ตรวจสอบการชนระหว่าง PlayerShip กับ Boss
    public boolean collidesWith(Boss boss) {
        double dx = x - boss.getX();
        double dy = y - boss.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + boss.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่าง PlayerShip กับ Heal
    public boolean collidesWith(Heal heal) {
        double dx = x - heal.getX();
        double dy = y - heal.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + heal.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }
}
