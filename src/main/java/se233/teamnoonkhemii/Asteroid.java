package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Asteroid extends Character {
    private double direction; // ทิศทางการเคลื่อนที่ของ Asteroid
    private Image asteroidImage; // ภาพของ Asteroid
    private static Image[] largeAsteroidImages; // อาร์เรย์ของภาพ Asteroid ขนาดใหญ่
    private static Image[] smallAsteroidImages; // อาร์เรย์ของภาพ Asteroid ขนาดเล็ก
    public static final double LARGE_SIZE_THRESHOLD = 50.0; // Asteroid เป็นขนาดใหญ่
    private static final double SMALL_ASTEROID_SIZE = 20.0; // ขนาดของ Asteroid เล็ก

    // เมธอดสร้างออบเจกต์ Asteroid โดยกำหนดค่าตำแหน่ง ความเร็ว ขนาด และทิศทาง
    public Asteroid(double x, double y, double speed, double size, double direction) {
        super(x, y, speed, size); // เรียกใช้งาน constructor ของคลาสแม่ (Character)
        this.direction = direction; // กำหนดทิศทางของ Asteroid
        this.asteroidImage = selectRandomImage(size); // เลือกภาพสำหรับ Asteroid ตามขนาด
    }

    // เมธอดสำหรับเลือกภาพแบบสุ่มให้กับ Asteroid ตามขนาด
    private Image selectRandomImage(double size) {
        if (size >= LARGE_SIZE_THRESHOLD) {
            // ถ้าเป็น Asteroid ขนาดใหญ่ จะสุ่มเลือกภาพจาก largeAsteroidImages
            return getLargeAsteroidImages()[new Random().nextInt(getLargeAsteroidImages().length)];
        } else {
            // ถ้าเป็น Asteroid ขนาดเล็ก จะสุ่มเลือกภาพจาก smallAsteroidImages
            return getSmallAsteroidImages()[new Random().nextInt(getSmallAsteroidImages().length)];
        }
    }

    // เมธอดสำหรับโหลดและเก็บภาพของ Asteroid ขนาดใหญ่
    private static Image[] getLargeAsteroidImages() {
        if (largeAsteroidImages == null) {
            // ถ้ายังไม่มีการโหลดภาพ จะทำการโหลดภาพต่าง ๆ เข้าไปในอาร์เรย์
            largeAsteroidImages = new Image[4];
            largeAsteroidImages[0] = new Image("/Sprite Asset/Ukka01.png");
            largeAsteroidImages[1] = new Image("/Sprite Asset/Ukka02.png");
            largeAsteroidImages[2] = new Image("/Sprite Asset/Ukka03.png");
            largeAsteroidImages[3] = new Image("/Sprite Asset/Ukka04.png");
        }
        return largeAsteroidImages;
    }

    // เมธอดสำหรับโหลดและเก็บภาพของ Asteroid ขนาดเล็ก
    private static Image[] getSmallAsteroidImages() {
        if (smallAsteroidImages == null) {
            // ถ้ายังไม่มีการโหลดภาพ จะทำการโหลดภาพต่าง ๆ เข้าไปในอาร์เรย์
            smallAsteroidImages = new Image[4];
            smallAsteroidImages[0] = new Image("/Sprite Asset/Uk01.png");
            smallAsteroidImages[1] = new Image("/Sprite Asset/Uk02.png");
            smallAsteroidImages[2] = new Image("/Sprite Asset/Uk03.png");
            smallAsteroidImages[3] = new Image("/Sprite Asset/Uk04.png");
        }
        return smallAsteroidImages;
    }

    // เมธอดสำหรับเคลื่อนที่ของ Asteroid ตามทิศทางและความเร็วที่กำหนด
    @Override
    public void move() {
        // คำนวณตำแหน่งใหม่ของ Asteroid โดยใช้ค่าความเร็วและทิศทาง
        x += speed * Math.cos(Math.toRadians(direction));
        y += speed * Math.sin(Math.toRadians(direction));
    }

    // เมธอดสำหรับวาด Asteroid ลงบน Canvas โดยใช้ GraphicsContext
    @Override
    public void draw(GraphicsContext gc) {
        if (asteroidImage != null) {
            // กำหนดขนาดของภาพตามขนาดของ Asteroid (ใหญ่หรือเล็ก)
            double scaleFactor = (size < LARGE_SIZE_THRESHOLD) ? 5.0 : 2.0;

            // วาดภาพ Asteroid ที่ตำแหน่ง x, y โดยเลื่อนให้ภาพอยู่ตรงกลาง
            gc.drawImage(asteroidImage, x - (size * scaleFactor) / 2, y - (size * scaleFactor) / 2, size * scaleFactor, size * scaleFactor);
        }
    }

    // เมธอดสำหรับแตก Asteroid ออกเป็นก้อนเล็ก ๆ เมื่อขนาดใหญ่กว่าเกณฑ์
    public List<Asteroid> split() {
        List<Asteroid> fragments = new ArrayList<>();
        if (size >= LARGE_SIZE_THRESHOLD) {
            // ถ้า Asteroid มีขนาดใหญ่กว่าเกณฑ์ จะทำการแตกเป็น 2 ก้อนเล็ก
            for (int i = 0; i < 2; i++) {
                // กำหนดทิศทางใหม่ของแต่ละก้อนเล็กให้ต่างกัน (มุม 30 องศา)
                double newDirection = direction + (i == 0 ? 30 : -30);

                // สร้าง Asteroid เล็กและเพิ่มลงในลิสต์
                Asteroid smallAsteroid = new Asteroid(x, y, speed * 1.2, SMALL_ASTEROID_SIZE, newDirection);
                fragments.add(smallAsteroid);
            }
        }
        return fragments; // คืนค่าลิสต์ของ Asteroid เล็กที่แตกออกมา
    }
}
