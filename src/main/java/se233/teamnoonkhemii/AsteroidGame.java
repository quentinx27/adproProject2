package se233.teamnoonkhemii;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class AsteroidGame extends Application {

    private static final Logger logger = Logger.getLogger(AsteroidGame.class.getName());

    private PlayerShip playerShip;
    private InputController inputController;
    private GraphicsContext gc;
    private ArrayList<PlayerShipBullet> normalBullets = new ArrayList<>();  // ลิสต์สำหรับเก็บกระสุน
    private ArrayList<Ultimate> ultimateBullets = new ArrayList<>();  // ลิสต์สำหรับเก็บกระสุน
    private SpawnManager spawnManager;
    private double mouseAngle;
    private boolean isGameOver; // ตัวแปรเพื่อบ่งบอกสถานะการจบเกม

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Asteroid Game");
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(1024, 768);
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);
        primaryStage.show();

        gc = canvas.getGraphicsContext2D();
        playerShip = new PlayerShip(400, 300, 3.0, 20);
        inputController = new InputController(scene);
        spawnManager = new SpawnManager();

        scene.setOnMouseMoved(event -> {
            mouseAngle = Math.atan2(event.getY() - playerShip.getY(), event.getX() - playerShip.getX()) + Math.toRadians(90);
        });

        logger.info("!!!Game start!!!");

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isGameOver) {
                    update(now);
                }
                render();
            }
        }.start();
    }

    private void update(long now) {
        logger.info("PlayerShip position X:[" + playerShip.getX() + "] Y:[ " + playerShip.getY() + "]");
        logger.warning("PlayerShip Score: " + playerShip.getScore());

        spawnManager.updateAndSpawn(now, 1024, 768);
        inputController.update(now);

        if (inputController.isMoveDownPressed()) {
            playerShip.setX(playerShip.getX() - playerShip.speed * Math.cos(Math.toRadians(playerShip.getAngle())));
            playerShip.setY(playerShip.getY() - playerShip.speed * Math.sin(Math.toRadians(playerShip.getAngle())));
        }
        if (inputController.isMoveUpPressed()) {
            playerShip.move();
        }
        if (inputController.isMoveLeftPressed()) {
            playerShip.setX(playerShip.getX() - playerShip.speed);
        }
        if (inputController.isMoveRightPressed()) {
            playerShip.setX(playerShip.getX() + playerShip.speed);
        }

        playerShip.setAngle(Math.toDegrees(mouseAngle));

        if (inputController.isShootingPressed()) {
            PlayerShipBullet bullet = PlayerShipBullet.createFromPlayerShip(playerShip);
            normalBullets.add(bullet);
            logger.warning("Player is shooting");
        }

        Iterator<PlayerShipBullet> normalBulletIterator = normalBullets.iterator();
        while (normalBulletIterator.hasNext()) {
            PlayerShipBullet bullet = normalBulletIterator.next();
            bullet.move();
            spawnManager.handleBulletCollision(bullet);

            if (bullet.isOutOfScreen(1024, 768) || !bullet.isActive()) {
                normalBulletIterator.remove();
            }
        }

        if (inputController.isUltimatePressed()) {
            Ultimate ultimate = Ultimate.createFromPlayerShip(playerShip);
            ultimateBullets.add(ultimate);
            logger.warning("Player is using Ultimate skill");
        }

        Iterator<Ultimate> ultimateBulletIterator = ultimateBullets.iterator();
        while (ultimateBulletIterator.hasNext()) {
            Ultimate ultimate = ultimateBulletIterator.next();
            ultimate.move();
            spawnManager.handleUltimateCollision(ultimate);

            if (ultimate.isOutOfScreen(1024, 768) || !ultimate.isActive()) {
                ultimateBulletIterator.remove();
            }
        }

        spawnManager.handleEnemyBulletCollision(playerShip);
        spawnManager.handlePlayerShipCollision(playerShip);

        // Check if the game should end
        if (!playerShip.isAlive()) {
            isGameOver = true;
            logger.warning("PlayerShip already Eliminated");
        }

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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 1024, 768);

        if (isGameOver) {
            // Display Game Over message
            gc.setFill(Color.RED);
            gc.setFont(new Font(40));
            gc.fillText("Game Over", 1024 / 2 - 120, 768 / 2 - 50);

            gc.setFont(new Font(30));
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 100, 768 / 2 + 10);
            gc.fillText("Press Enter to Retry", 1024 / 2 - 100, 768 / 2 + 50);
        } else {
            // Draw game elements
            playerShip.draw(gc);
            for (PlayerShipBullet bullet : normalBullets) {
                bullet.draw(gc);
            }
            for (Ultimate bullet : ultimateBullets) {
                bullet.draw(gc);
            }
            spawnManager.drawEntities(gc);

            // Draw Lives and Score of the player
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(26));
            gc.fillText("Lives: " + playerShip.getPlayerShipLives(), 20, 30);
            gc.fillText("Score: " + playerShip.getScore(), 20, 60);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
