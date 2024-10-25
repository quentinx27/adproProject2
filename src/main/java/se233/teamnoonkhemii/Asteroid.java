package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Asteroid extends Character {
    private double direction;

    public Asteroid(double x, double y, double speed, double size, double direction) {
        super(x, y, speed, size);
        this.direction = direction; // Set direction in degrees
    }

    @Override
    public void move() {
        // Asteroids move in a straight line based on their direction
        x += speed * Math.cos(Math.toRadians(direction));
        y += speed * Math.sin(Math.toRadians(direction));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
}
