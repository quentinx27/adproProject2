package se233.teamnoonkhemii;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.logging.Logger;
import java.util.logging.LogManager;

public class AsteroidGame extends Application {

    private static final Logger logger = Logger.getLogger(AsteroidGame.class.getName());

    private PlayerShip playerShip;
    private InputController inputController;
    private GraphicsContext gc;
    private ArrayList<PlayerShipBullet> normalBullets = new ArrayList<>();  // ลิสต์สำหรับเก็บกระสุน
    private ArrayList<Ultimate> ultimateBullets = new ArrayList<>();  // ลิสต์สำหรับเก็บกระสุน

    @Override
    public void start(Stage primaryStage) {
        // Set up the window
        primaryStage.setTitle("Asteroid Game");
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(1024, 768);
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize the graphics context
        gc = canvas.getGraphicsContext2D();

        // Initialize the player ship and input controller
        playerShip = new PlayerShip(400, 300, 3.0, 20);
        inputController = new InputController(scene);

        logger.info("!!!Game start!!!");

        // Set up the game loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();
    }

    private void update() {
        // Handle player movement based on input
        if (inputController.isMoveDownPressed()) {
            playerShip.setX(playerShip.getX() - playerShip.speed * Math.cos(Math.toRadians(playerShip.getAngle())));
            playerShip.setY(playerShip.getY() - playerShip.speed * Math.sin(Math.toRadians(playerShip.getAngle())));
        }
        if (inputController.isMoveUpPressed()) {
            playerShip.move(); // Forward movement in the current angle
        }
        if (inputController.isMoveLeftPressed()) {
            playerShip.setX(playerShip.getX() - playerShip.speed);
        }
        if (inputController.isMoveRightPressed()) {
            playerShip.setX(playerShip.getX() + playerShip.speed);
        }
        if (inputController.isRotateLeftPressed()) {
            playerShip.rotateLeft();
        }
        if (inputController.isRotateRightPressed()) {
            playerShip.rotateRight();
        }

        // Generate a new bullet when shooting is pressed
        if (inputController.isShootingPressed()) {
            PlayerShipBullet bullet = PlayerShipBullet.createFromPlayerShip(playerShip);
            normalBullets.add(bullet);
            logger.info("Player is shooting");
        }

        // Update normal bullets
        Iterator<PlayerShipBullet> normalBulletIterator = normalBullets.iterator();
        while (normalBulletIterator.hasNext()) {
            PlayerShipBullet bullet = normalBulletIterator.next();
            bullet.move();
            if (bullet.isOutOfScreen(1024, 768)) {
                normalBulletIterator.remove();  // Remove bullet if it's out of screen
            }
        }

        // Generate a new Ultimate bullet when Ultimate skill is pressed
        if (inputController.isUltimatePressed()) {
            Ultimate ultimate = Ultimate.createFromPlayerShip(playerShip);
            ultimateBullets.add(ultimate);
            logger.info("Player is using Ultimate skill");
        }

        // Update ultimate bullets
        Iterator<Ultimate> ultimateBulletIterator = ultimateBullets.iterator();
        while (ultimateBulletIterator.hasNext()) {
            Ultimate ultimate = ultimateBulletIterator.next();
            ultimate.move();
            if (ultimate.isOutOfScreen(1024, 768)) {
                ultimateBulletIterator.remove();  // Remove ultimate if it's out of screen
            }
        }
        // Pacman Effect: If the ship moves beyond the screen boundaries, wrap around
        double screenWidth = 1024;
        double screenHeight = 768;

        if (playerShip.getX() < 0) {
            playerShip.setX(screenWidth);
        } else if (playerShip.getX() > screenWidth) {
            playerShip.setX(0);
        }

        if (playerShip.getY() < 0) {
            playerShip.setY(screenHeight);
        } else if (playerShip.getY() > screenHeight) {
            playerShip.setY(0);
        }
    }

    private void render() {
        // Clear the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 1024, 768);

        // Draw the player ship
        playerShip.draw(gc);

        // Draw each bullet
        for (PlayerShipBullet bullet : normalBullets) {
            bullet.draw(gc);
        }

        for (Ultimate bullet : ultimateBullets) {
            bullet.draw(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
