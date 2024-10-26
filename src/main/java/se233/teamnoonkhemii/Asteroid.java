package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Random;

public class Asteroid extends Character {
    private double direction;
    private Image asteroidImage;
    private static final Image[] LARGE_ASTEROID_IMAGES = new Image[4];
    private static final Image[] SMALL_ASTEROID_IMAGES = new Image[4];
    private static final double LARGE_SIZE_THRESHOLD = 50.0; // Example threshold for large asteroids

    static {
        // Load large asteroid images
        LARGE_ASTEROID_IMAGES[0] = new Image("/Sprite Asset/Ukka01.png");
        LARGE_ASTEROID_IMAGES[1] = new Image("/Sprite Asset/Ukka02.png");
        LARGE_ASTEROID_IMAGES[2] = new Image("/Sprite Asset/Ukka03.png");
        LARGE_ASTEROID_IMAGES[3] = new Image("/Sprite Asset/Ukka04.png");

        // Load small asteroid images
        SMALL_ASTEROID_IMAGES[0] = new Image("/Sprite Asset/Uk01.png");
        SMALL_ASTEROID_IMAGES[1] = new Image("/Sprite Asset/Uk02.png");
        SMALL_ASTEROID_IMAGES[2] = new Image("/Sprite Asset/Uk03.png");
        SMALL_ASTEROID_IMAGES[3] = new Image("/Sprite Asset/Uk04.png");
    }

    public Asteroid(double x, double y, double speed, double size, double direction) {
        super(x, y, speed, size);
        this.direction = direction;
        this.asteroidImage = selectRandomImage(size);
    }

    private Image selectRandomImage(double size) {
        Random random = new Random();
        if (size >= LARGE_SIZE_THRESHOLD) {
            return LARGE_ASTEROID_IMAGES[random.nextInt(LARGE_ASTEROID_IMAGES.length)];
        } else {
            return SMALL_ASTEROID_IMAGES[random.nextInt(SMALL_ASTEROID_IMAGES.length)];
        }
    }

    @Override
    public void move() {
        x += speed * Math.cos(Math.toRadians(direction));
        y += speed * Math.sin(Math.toRadians(direction));
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (asteroidImage != null) {
            gc.drawImage(asteroidImage, x - size / 2, y - size / 2, size, size);
        }
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}
