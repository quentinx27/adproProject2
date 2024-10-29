package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class PlayerShip extends Character {
    private double angle; // มุมการเคลื่อนที่ของยาน
    private static int PlayerShiplives; // จำนวนชีวิตของยานผู้เล่น
    private static int PlayerShipBossEliminated; // จำนวนบอสที่ถูกกำจัดโดยยานผู้เล่น
    private static int score; // คะแนนของยานผู้เล่น
    private SpriteAnimation animation; // SpriteAnimation สำหรับแสดงแอนิเมชันของยาน

    // Constructor สำหรับกำหนดค่าเริ่มต้นของ PlayerShip
    public PlayerShip(double x, double y, double speed, double size) {
        super(x, y, speed, size);
        this.angle = 0; // กำหนดมุมเริ่มต้น
        this.PlayerShiplives = 5; // เริ่มต้นด้วยชีวิต 5
        this.PlayerShipBossEliminated = 0; // เริ่มต้นด้วยบอสที่ถูกกำจัดเป็น 0
        this.score = 0; // เริ่มต้นด้วยคะแนน 0

        // สร้าง SpriteAnimation สำหรับยานผู้เล่นจาก sprite sheet
        animation = new SpriteAnimation("/Sprite Asset/FinalXwing02.png", 1, 8, 125_000_000);
    }

    @Override
    public void move() {
        // คำนวณตำแหน่งใหม่ของยานผู้เล่นตามมุมและความเร็วปัจจุบัน
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save(); // บันทึกสถานะปัจจุบันของ canvas

        // ย้ายตำแหน่ง canvas ไปที่ตำแหน่งของยานผู้เล่นและหมุนตามมุม
        gc.translate(x, y);
        gc.rotate(angle);

        // อัปเดตและแสดงเฟรมปัจจุบันของแอนิเมชัน
        long currentTime = System.nanoTime();
        animation.update(currentTime); // อัปเดตเฟรมแอนิเมชัน
        double scaleFactor = 4.0; // ปรับขนาดการแสดงผลของยาน
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);

        gc.restore(); // คืนค่าคอนเท็กซ์ canvas กลับสู่สถานะเดิม
    }

    // กำหนดมุมการหมุนของยานผู้เล่น
    public void setAngle(double angle) {
        this.angle = angle;
    }

    // รับค่ามุมปัจจุบันของยานผู้เล่น
    public double getAngle() {
        return angle;
    }

    // รับจำนวนชีวิตของยานผู้เล่น
    public int getPlayerShipLives() {
        return PlayerShiplives;
    }

    // รีเซ็ตจำนวนชีวิตของยานผู้เล่นเป็นค่าเริ่มต้น
    public static void resetPlayerShipLives() {
        PlayerShiplives = 5;
    }

    // ลดจำนวนชีวิตของยานผู้เล่นตามค่า damage ที่รับเข้ามา
    public void PlayerShipTakingDamage(int damage) {
        if (PlayerShiplives > 0) {
            PlayerShiplives -= damage;
        }
    }

    // รับจำนวนบอสที่ถูกกำจัดโดยยานผู้เล่น
    public int getPlayerShipBossEliminated() {
        return PlayerShipBossEliminated;
    }

    // เพิ่มจำนวนบอสที่ถูกกำจัด
    public static void addPlayerShipBossEliminated(int mark) {
        PlayerShipBossEliminated += mark;
    }

    // รีเซ็ตจำนวนบอสที่ถูกกำจัดเป็น 0
    public static void resetPlayerShipBossEliminated() {
        PlayerShipBossEliminated = 0;
    }

    // เพิ่มจำนวนชีวิตให้กับยานผู้เล่น
    public void addLives(int lives) {
        PlayerShiplives += lives;
    }

    // รับคะแนนปัจจุบันของยานผู้เล่น
    public int getScore() {
        return score;
    }

    // เพิ่มคะแนนให้กับยานผู้เล่น
    public static void addScore(int points) {
        score += points;
    }

    // รีเซ็ตคะแนนของยานผู้เล่นเป็น 0
    public static void resetScore() {
        score = 0;
    }

    // ตรวจสอบว่ายานผู้เล่นยังมีชีวิตอยู่หรือไม่
    public boolean isAlive() {
        return PlayerShiplives > 0;
    }

    // ตรวจสอบว่ายานผู้เล่นเสียชีวิตแล้วหรือไม่
    public boolean isGameOver() {
        return PlayerShiplives == 0;
    }

    // ตรวจสอบการชนระหว่างยานผู้เล่นกับ Asteroid
    public boolean collidesWith(Asteroid asteroid) {
        double dx = x - asteroid.getX();
        double dy = y - asteroid.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + asteroid.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่างยานผู้เล่นกับ Enemy
    public boolean collidesWith(Enemy enemy) {
        double dx = x - enemy.getX();
        double dy = y - enemy.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + enemy.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่างยานผู้เล่นกับ Boss
    public boolean collidesWith(Boss boss) {
        double dx = x - boss.getX();
        double dy = y - boss.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + boss.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }

    // ตรวจสอบการชนระหว่างยานผู้เล่นกับ Heal
    public boolean collidesWith(Heal heal) {
        double dx = x - heal.getX();
        double dy = y - heal.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (size / 2 + heal.getSize() / 2); // ตรวจสอบการชนแบบวงกลม
    }
}
