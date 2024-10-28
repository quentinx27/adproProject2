package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Asteroid extends Character {
    private double direction;
    private Image asteroidImage;
    private static Image[] largeAsteroidImages;
    private static Image[] smallAsteroidImages;
    public static final double LARGE_SIZE_THRESHOLD = 50.0; // Example threshold for large asteroids
    private static final double SMALL_ASTEROID_SIZE = 20.0;  // ขนาดของ Asteroid เล็ก

    public Asteroid(double x, double y, double speed, double size, double direction) {
        super(x, y, speed, size);
        this.direction = direction;
        this.asteroidImage = selectRandomImage(size);
    }

    private Image selectRandomImage(double size) {
        if (size >= LARGE_SIZE_THRESHOLD) {
            return getLargeAsteroidImages()[new Random().nextInt(getLargeAsteroidImages().length)];
        } else {
            return getSmallAsteroidImages()[new Random().nextInt(getSmallAsteroidImages().length)];
        }
    }

    private static Image[] getLargeAsteroidImages() {
        if (largeAsteroidImages == null) {
            largeAsteroidImages = new Image[4];
            largeAsteroidImages[0] = new Image("/Sprite Asset/Ukka01.png");
            largeAsteroidImages[1] = new Image("/Sprite Asset/Ukka02.png");
            largeAsteroidImages[2] = new Image("/Sprite Asset/Ukka03.png");
            largeAsteroidImages[3] = new Image("/Sprite Asset/Ukka04.png");
        }
        return largeAsteroidImages;
    }

    private static Image[] getSmallAsteroidImages() {
        if (smallAsteroidImages == null) {
            smallAsteroidImages = new Image[4];
            smallAsteroidImages[0] = new Image("/Sprite Asset/Uk01.png");
            smallAsteroidImages[1] = new Image("/Sprite Asset/Uk02.png");
            smallAsteroidImages[2] = new Image("/Sprite Asset/Uk03.png");
            smallAsteroidImages[3] = new Image("/Sprite Asset/Uk04.png");
        }
        return smallAsteroidImages;
    }

    @Override
    public void move() {
        x += speed * Math.cos(Math.toRadians(direction));
        y += speed * Math.sin(Math.toRadians(direction));
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (asteroidImage != null) {
            double scaleFactor = (size < LARGE_SIZE_THRESHOLD) ? 5.0 : 2.0;
            gc.drawImage(asteroidImage, x - (size * scaleFactor) / 2, y - (size * scaleFactor) / 2, size * scaleFactor, size * scaleFactor);
        }
    }

    public List<Asteroid> split() {
        List<Asteroid> fragments = new ArrayList<>();
        if (size >= LARGE_SIZE_THRESHOLD) {
            for (int i = 0; i < 2; i++) {
                double newDirection = direction + (i == 0 ? 30 : -30);  // แตกออกเป็น 2 ก้อนที่มุมต่างกัน
                Asteroid smallAsteroid = new Asteroid(x, y, speed * 1.2, SMALL_ASTEROID_SIZE, newDirection);
                fragments.add(smallAsteroid);
            }
        }
        return fragments;
    }
}
