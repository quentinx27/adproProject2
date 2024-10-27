package se233.teamnoonkhemii;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private Image skullIcon;

    // ตัวแปรสำหรับ cooldown ของบอส
    private long bossCooldown = 0; // เวลาที่เหลือในการ spawn บอสใหม่

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

        skullIcon = new Image("/Sprite Asset/skullIcon.png"); // ระบุพาธของรูปภาพไอคอนหัวกะโหลก

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

        // อัปเดต bossCooldown
        bossCooldown = Math.max(0, (spawnManager.getNextBossSpawnTime() - now) / 1_000_000_000); // คำนวณเวลาที่เหลือเป็นวินาที

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

        if (inputController.isDeveloperCheat()) {
            playerShip.addLives(500);
            logger.warning("DeveloperCheat is Active");
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
        spawnManager.handleBossBulletCollision(playerShip);
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
            gc.setFill(Color.RED);
            gc.setFont(new Font(40));
            gc.fillText("Game Over", 1024 / 2 - 120, 768 / 2 - 50);

            gc.setFont(new Font(30));
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 100, 768 / 2 + 10);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 1024 / 2 - 100, 768 / 2 + 40);
            gc.fillText("Press Enter to Retry", 1024 / 2 - 100, 768 / 2 + 90);
        } else {
            playerShip.draw(gc);
            for (PlayerShipBullet bullet : normalBullets) {
                bullet.draw(gc);
            }
            for (Ultimate bullet : ultimateBullets) {
                bullet.draw(gc);
            }
            spawnManager.drawEntities(gc);

            // วาดแถบ cooldown สำหรับบอส
            drawBossCooldownBar(gc);

            if (!spawnManager.getBosses().isEmpty()) {
                drawBossLivesText(gc, spawnManager.getBosses().get(0)); // วาดแถบเลือดของบอสตัวแรก
            }

            gc.setFill(Color.WHITE);
            gc.setFont(new Font(26));
            gc.fillText("Lives: " + playerShip.getPlayerShipLives(), 20, 30);
            gc.fillText("Score: " + playerShip.getScore(), 20, 60);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 20, 90);
        }
    }

    private void drawBossCooldownBar(GraphicsContext gc) {
        double barWidth = 200;
        double barHeight = 20;
        double x = 1024 / 2 - barWidth / 2;
        double y = 30;

        // คำนวณเปอร์เซ็นต์ของ cooldown
        double percentage = bossCooldown / 40.0;
        double fillWidth = barWidth * percentage;

        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, barWidth, barHeight);

        gc.setFill(Color.RED);
        gc.fillRect(x, y, fillWidth, barHeight);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font(18));
        gc.fillText("Boss Cooldown: " + bossCooldown + "s", x + 24, y + 15);
    }

    private void drawBossLivesText(GraphicsContext gc, Boss boss) {
        double iconSize = 100; // ขนาดของไอคอนหัวกะโหลก

        // วาดไอคอนหัวกะโหลกที่ตำแหน่ง x = 820, y = 10
        gc.drawImage(skullIcon, 765, -7, iconSize, iconSize);

        // วาดข้อความจำนวนชีวิตของบอสด้านหลังไอคอน
        gc.setFill(Color.RED);
        gc.setFont(new Font(26));
        gc.fillText("Boss Lives: " + boss.getBosslives(), 844, 50);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
