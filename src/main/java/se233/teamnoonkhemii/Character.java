package se233.teamnoonkhemii;

import javafx.scene.canvas.GraphicsContext;

public abstract class Character {

        protected double x, y;
        protected double speed;
        protected double size;

        public Character(double x, double y, double speed, double size) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.size = size;
        }

        public abstract void move();  // Abstract method, each character type will have its own movement logic

        public void draw(GraphicsContext gc) {
            // Common draw logic can go here, but will likely be overridden
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getSize() {
            return size;
        }

        public void setSize(double size) {
            this.size = size;
        }

    // เพิ่มเมธอด isOutOfScreen เพื่อตรวจสอบว่าออบเจ็กต์อยู่นอกจอหรือไม่
    public boolean isOutOfScreen(double screenWidth, double screenHeight) {
        return x < -size || x > screenWidth + size || y < -size || y > screenHeight + size;
    }
}
