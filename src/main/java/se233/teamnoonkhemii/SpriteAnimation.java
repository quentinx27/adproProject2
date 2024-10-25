package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class SpriteAnimation {
    private Image spriteSheet;        // The complete sprite sheet
    private WritableImage[] frames;   // Array of frames extracted from the sprite sheet
    private int currentFrame = 0;     // Current frame index
    private long lastFrameTime = 0;   // Time at which the last frame was shown
    private long frameDuration;       // Duration of each frame (in nanoseconds)
    private int totalFrames;          // Total number of frames

    public SpriteAnimation(String imagePath, int rows, int columns, long frameDuration) {
        // Load the sprite sheet image
        spriteSheet = new Image(getClass().getResourceAsStream(imagePath));

        // Calculate the frame size
        int frameWidth = (int) spriteSheet.getWidth() / columns;
        int frameHeight = (int) spriteSheet.getHeight() / rows;

        // Calculate the total number of frames
        totalFrames = rows * columns;
        frames = new WritableImage[totalFrames];

        // Extract individual frames
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                frames[row * columns + col] = new WritableImage(spriteSheet.getPixelReader(), col * frameWidth, row * frameHeight, frameWidth, frameHeight);
            }
        }

        this.frameDuration = frameDuration;  // Frame duration (in nanoseconds)
    }

    // Update the frame based on time
    public void update(long currentTime) {
        if (currentTime - lastFrameTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % totalFrames;  // Move to the next frame
            lastFrameTime = currentTime;
        }
    }

    // Render the current frame
    public void render(GraphicsContext gc, double x, double y, double width, double height) {
        WritableImage currentSprite = frames[currentFrame];  // Get the current frame
        gc.drawImage(currentSprite, x, y, width, height);    // Draw the frame
    }
}
