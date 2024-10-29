package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public class Ultimate extends Bullet {
    private SpriteAnimation animation;

    public Ultimate(double x, double y, double speed, double angle, double size) {
        super(x, y, speed, angle, size);
        // กำหนด SpriteAnimation สำหรับ Ultimate Skill
        animation = new SpriteAnimation("/Sprite Asset/Ultimate01.png", 4, 5, 100_000_000);
    }

    public static Ultimate[] createFromPlayerShip(PlayerShip playerShip) {
        double baseAngle = playerShip.getAngle(); // มุมฐานจากยานผู้เล่น

        // กำหนดมุมการยิงสำหรับกระสุน 3 ทิศทาง
        double[] angles = {baseAngle - 15, baseAngle, baseAngle + 15};
        Ultimate[] ultimateShots = new Ultimate[3]; // อาร์เรย์สำหรับเก็บ Ultimate 3 ลูก

        for (int i = 0; i < angles.length; i++) {
            double angle = angles[i];
            double offsetX = (playerShip.getSize() / 2) * Math.cos(Math.toRadians(angle));
            double offsetY = (playerShip.getSize() / 2) * Math.sin(Math.toRadians(angle));

            double ultimateX = playerShip.getX() + offsetX - 10;
            double ultimateY = playerShip.getY() + offsetY - 10;

            double ultimateSpeed = 3.0;  // ความเร็วของ Ultimate
            double ultimateSize = 12.0;  // ขนาดของ Ultimate

            ultimateShots[i] = new Ultimate(ultimateX, ultimateY, ultimateSpeed, angle, ultimateSize);
        }

        return ultimateShots; // คืนค่าเป็นอาร์เรย์ Ultimate 3 ลูก
    }

    @Override
    public void move() {
        super.move();
        long currentTime = System.nanoTime();
        animation.update(currentTime);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        double scaleFactor = 10.0;
        animation.render(gc, -size / 2 * scaleFactor, -size / 2 * scaleFactor, size * scaleFactor, size * scaleFactor);
        gc.restore();
    }

    public void deactivate() {
        this.isActive = false;
    }
}
