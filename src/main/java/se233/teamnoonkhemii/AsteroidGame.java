package se233.teamnoonkhemii;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
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
    private Image FinalSkullIcon;
    private SpriteAnimation backgroundAnimation; // แอนิเมชันสำหรับพื้นหลัง
    private SpriteAnimation menuBackgroundAnimation; // แอนิเมชันสำหรับพื้นหลังเมนูหลัก
    private GraphicsContext menuGC;
    private Button restartButton;
    private Button startButton; // ปุ่มสำหรับเริ่มเกม
    private Button backtoMainMenuButton;

    // ตัวแปรสำหรับ cooldown ของบอส
    private long bossCooldown = 0; // เวลาที่เหลือในการ spawn บอสใหม่

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Asteroid Game");

        // เรียกสร้างเมนูหลัก
        showMainMenu(primaryStage);
        configureLogging();
    }

    private void configureLogging() {
        // กำหนด log แสดงเป็นสีเขียว
        //log Green
        System.setProperty("java.util.logging.SimpleFormatter.format", "\u001B[32m[%1$tF %1$tT] [%2$s] %4$s: %5$s %n\u001B[0m");
    }

    public static void playSound(String soundFilePath) {
        // เล่นไฟล์เสียง หากมีปัญหาจะแสดง error
        try {
            Media sound = new Media(AsteroidGame.class.getResource(soundFilePath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            logger.severe("Error playing sound: " + e.getMessage());
        }
    }
    private void showMainMenu(Stage primaryStage) {
        // สร้างเมนูหลักของเกมและแสดงแอนิเมชันพื้นหลัง
        // สร้าง Group และ Scene สำหรับเมนูหลัก
        StackPane root = new StackPane();
        Scene menuScene = new Scene(root, 1024, 768);

        // สร้าง canvas และ GraphicsContext สำหรับวาดพื้นหลังในเมนูหลัก
        Canvas menuCanvas = new Canvas(1024, 768);
        menuGC = menuCanvas.getGraphicsContext2D();
        root.getChildren().add(menuCanvas);

        // สร้างแอนิเมชันสำหรับพื้นหลังเมนู
        menuBackgroundAnimation = new SpriteAnimation("/Sprite Asset/bg1.png", 6, 4, 125_000_000);

        // สร้างปุ่มเริ่มเกม
        startButton = new Button("Start Game");
        startButton.setPrefWidth(200);
        startButton.setPrefHeight(80);
        startButton.setStyle("-fx-font-size: 24px;");
        startButton.setOnAction(event -> showGameScene(primaryStage));
        root.getChildren().add(startButton);

        // แสดงเมนูหลัก
        primaryStage.setScene(menuScene);
        primaryStage.show();
        isGameOver = false;

        // ใช้ AnimationTimer สำหรับอัปเดตเฟรมของแอนิเมชันพื้นหลังเมนู
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                menuBackgroundAnimation.update(now);
                renderMenuBackground();
            }
        }.start();
    }

    private void renderMenuBackground() {
        // วาดแอนิเมชันพื้นหลังของเมนูหลัก
        double backgroundScale = 1.0;
        menuBackgroundAnimation.render(menuGC, 0, 0, 1024 * backgroundScale, 768 * backgroundScale);

        // วาดข้อความ "Asteroid Game" ที่ตำแหน่งตรงกลางของหน้าจอ
        menuGC.setFont(new Font(72));

        menuGC.setFill(Color.BLACK);
        menuGC.fillText("Asteroid Game", 1024 / 2 - 220, 768 / 2 - 80);  // ปรับตำแหน่ง X, Y ตามความต้องการ
        menuGC.fillText("Asteroid Game", 1024 / 2 - 216, 768 / 2 - 76);
        menuGC.fillText("Asteroid Game", 1024 / 2 - 220, 768 / 2 - 76);
        menuGC.fillText("Asteroid Game", 1024 / 2 - 216, 768 / 2 - 80);

        menuGC.setFill(Color.WHITE);
        menuGC.fillText("Asteroid Game", 1024 / 2 - 218, 768 / 2 - 78);

    }


    private void showGameScene(Stage primaryStage) {
        //สร้างฉากของเกมและตั้งค่าพื้นหลัง รวมถึงการควบคุม
        Group root = new Group();
        Scene gameScene = new Scene(root);
        Canvas canvas = new Canvas(1024, 768);
        root.getChildren().add(canvas);
        primaryStage.setScene(gameScene);
        primaryStage.show();

        gc = canvas.getGraphicsContext2D();
        playerShip = new PlayerShip(400, 300, 3.0, 20);
        inputController = new InputController(gameScene);
        spawnManager = new SpawnManager();

        // สร้างแอนิเมชันสำหรับพื้นหลัง bg1= 6,4,125_000_000
        backgroundAnimation = new SpriteAnimation("/Sprite Asset/bg1.png", 6, 4, 125_000_000);

        gameScene.setOnMouseMoved(event -> {
            mouseAngle = Math.atan2(event.getY() - playerShip.getY(), event.getX() - playerShip.getX()) + Math.toRadians(90);
        });

        try {
            FinalSkullIcon = new Image("/Sprite Asset/FinalSkullIcon.png");
        } catch (IllegalArgumentException e) {
            logger.severe("Failed to load skull icon: " + e.getMessage());
        }

        // สร้างปุ่มรีสตาร์ท แต่ยังไม่แสดงผลจนกว่าจะจบเกม
        try {
            restartButton = new Button("Restart");
            restartButton.setLayoutX(1024 / 2 - 75);
            restartButton.setLayoutY(768 / 2 - 10);
            restartButton.setPrefWidth(150);
            restartButton.setPrefHeight(70);
            restartButton.setStyle("-fx-font-size: 24px;");
            restartButton.setVisible(false);
            restartButton.setOnAction(event -> restartGame());
            root.getChildren().add(restartButton);

            // สร้างปุ่มกลับไปเมนูหลัก
            backtoMainMenuButton = new Button("MainMenu");
            backtoMainMenuButton.setLayoutX(1024 / 2 - 75);
            backtoMainMenuButton.setLayoutY(768 / 2 + 80);
            backtoMainMenuButton.setPrefWidth(150);
            backtoMainMenuButton.setPrefHeight(70);
            backtoMainMenuButton.setStyle("-fx-font-size: 24px;");
            backtoMainMenuButton.setVisible(false);
            backtoMainMenuButton.setOnAction(event -> showMainMenu(primaryStage));
            root.getChildren().add(backtoMainMenuButton);
        }catch (RuntimeException e) {
            logger.severe("Error setting up buttons: " + e.getMessage());
        }


        logger.info("!!!Game start!!!");

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isGameOver) {
                    update(now);
                }
                try {
                    render();
                } catch (RuntimeException e) {
                    logger.severe("Error during render: " + e.getMessage());
                }
            }
        }.start();
    }


    private void update(long now) {
        // อัปเดตสถานะต่าง ๆ ของเกม เช่น การเคลื่อนไหวของ PlayerShip การตรวจจับการชนกัน
        logger.info("PlayerShip position X:[" + playerShip.getX() + "] Y:[ " + playerShip.getY() + "]");
        logger.warning("PlayerShip Score: " + playerShip.getScore());

        // อัปเดต bossCooldown
        bossCooldown = Math.max(0, (spawnManager.getNextBossSpawnTime() - now) / 1_000_000_000); // คำนวณเวลาที่เหลือเป็นวินาที

        spawnManager.updateAndSpawn(now, 1024, 768);
        inputController.update(now);

        // อัปเดตแอนิเมชันของพื้นหลัง
        backgroundAnimation.update(now);

        // จัดการการเคลื่อนที่ของ PlayerShip ตามทิศทางที่กำหนด
        if (inputController.isMoveDownPressed()) {
            playerShip.setY(playerShip.getY() + playerShip.speed);
        }
        if (inputController.isMoveUpPressed()) {
            playerShip.setY(playerShip.getY() - playerShip.speed);
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
            playSound("/Sounds/Leser01.wav");
            logger.warning("Player is shooting");
        }


        // จัดการการเคลื่อนที่และการลบกระสุนที่ไม่จำเป็นออก
        Iterator<PlayerShipBullet> normalBulletIterator = normalBullets.iterator();
        while (normalBulletIterator.hasNext()) {
            PlayerShipBullet bullet = normalBulletIterator.next();
            bullet.move();
            spawnManager.handleBulletCollision(bullet);

            if (bullet.isOutOfScreen(1024, 768) || !bullet.isActive()) {
                normalBulletIterator.remove();
            }
        }

        // ตรวจสอบสถานะอื่น ๆ เช่น การใช้ Ultimate และการโกง
        if (inputController.isUltimatePressed()) {
            Ultimate[] ultimates = Ultimate.createFromPlayerShip(playerShip);
            // เพิ่มกระสุนทั้ง 3 ลูกในลิสต์
            ultimateBullets.addAll(Arrays.asList(ultimates));
            playSound("/Sounds/sUltimate01.mp3");
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

        // ตรวจสอบการชนและสถานะของเกม เช่น การสิ้นสุดของเกม
        if (inputController.isDeveloperCheat()) {
            playerShip.addLives(500);
            logger.warning("DeveloperCheat is Active");
        }

        if (inputController.isGameOverKeyboard()) {
            isGameOver = true;
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
        // วาดแอนิเมชันของพื้นหลัง
        double backgroundScale = 1.0; // ปรับขนาดพื้นหลังถ้าต้องการ
        backgroundAnimation.render(gc, 0, 0, 1024 * backgroundScale, 768 * backgroundScale);

        if (isGameOver) {
            gc.setFont(new Font(50));

            // วาดกรอบสีดำรอบข้อความ "Game Over"
            gc.setFill(Color.BLACK);
            gc.fillText("Game Over", 1024 / 2 - 122, 768 / 2 - 172);
            gc.fillText("Game Over", 1024 / 2 - 118, 768 / 2 - 168);
            gc.fillText("Game Over", 1024 / 2 - 122, 768 / 2 - 168);
            gc.fillText("Game Over", 1024 / 2 - 118, 768 / 2 - 172);

            // วาดข้อความ "Game Over" สีแดงทับลงไป
            gc.setFill(Color.RED);
            gc.fillText("Game Over", 1024 / 2 - 120, 768 / 2 - 170);

            gc.setFont(new Font(30));

            // วาดกรอบสีดำรอบข้อความ "Final Score"
            gc.setFill(Color.BLACK);
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 102, 768 / 2 - 112);
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 98, 768 / 2 - 108);
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 102, 768 / 2 - 108);
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 98, 768 / 2 - 112);

            // วาดข้อความ "Final Score" สีแดงทับลงไป
            gc.setFill(Color.RED);
            gc.fillText("Final Score: " + playerShip.getScore(), 1024 / 2 - 100, 768 / 2 - 110);

            // วาดกรอบสีดำรอบข้อความ "Boss Eliminated"
            gc.setFill(Color.BLACK);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 1024 / 2 - 102, 768 / 2 - 82);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 1024 / 2 - 98, 768 / 2 - 78);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 1024 / 2 - 102, 768 / 2 - 78);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 1024 / 2 - 98, 768 / 2 - 82);

            // วาดข้อความ "Boss Eliminated" สีแดงทับลงไป
            gc.setFill(Color.RED);
            gc.fillText("Boss Eliminated: " + playerShip.getPlayerShipBossEliminated(), 1024 / 2 - 100, 768 / 2 - 80);

            restartButton.setVisible(true);
            backtoMainMenuButton.setVisible(true);
        }
        else {
            // วาดองค์ประกอบต่าง ๆ เมื่อเกมยังไม่จบ เช่น คะแนนและจำนวนชีวิต
            restartButton.setVisible(false);
            backtoMainMenuButton.setVisible(false);
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
        // วาดแถบ cooldown ของบอสบนหน้าจอ
        double barWidth = 200;
        double barHeight = 20;
        double x = 1024 / 2 - barWidth / 2;
        double y = 30;

        // คำนวณเปอร์เซ็นต์ของ cooldown
        double percentage = bossCooldown / 25.0;
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
        // วาดจำนวนชีวิตของบอสที่ด้านบนของหน้าจอ
        double iconSize = 50; // ขนาดของไอคอนหัวกะโหลก
        // วาดไอคอนหัวกะโหลกที่ตำแหน่ง x = 820, y = 10
        gc.drawImage(FinalSkullIcon, 790, 15, iconSize, iconSize);

        // วาดข้อความจำนวนชีวิตของบอสด้านหลังไอคอน
        gc.setFill(Color.RED);
        gc.setFont(new Font(26));
        gc.fillText("Boss Lives: " + boss.getBosslives(), 844, 50);
    }

    private void restartGame(){
        // รีเซ็ตสถานะของเกมเมื่อเริ่มเกมใหม่
        isGameOver = false;
        PlayerShip.resetPlayerShipLives();
        PlayerShip.resetPlayerShipBossEliminated();
        PlayerShip.resetScore();
        SpawnManager.enemies.clear();
        SpawnManager.enemyBullets.clear();
        SpawnManager.bosses.clear();
        SpawnManager.bossBullets.clear();
        SpawnManager.asteroids.clear();
        SpawnManager.heals.clear();
        SpawnManager.resetBossCooldown();
    }

    public static void main(String[] args) {
        // เริ่มโปรแกรม
        launch(args);
    }
}
