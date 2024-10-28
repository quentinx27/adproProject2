// file: se233/teamnoonkhemii/JavaFXInitializer.java
package se233.teamnoonkhemii;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

public class JavaFXInitializer {
    private static boolean initialized = false;

    public static void initialize() {
        if (!initialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to initialize JavaFX", e);
            }
            initialized = true;
        }
    }
}
