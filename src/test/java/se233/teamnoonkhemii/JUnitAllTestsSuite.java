package se233.teamnoonkhemii;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CollisionTest.class,
        GameStateTest.class,
        CharacterMovementTest.class,
        BulletTest.class
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JUnitAllTestsSuite {
    // คลาสนี้ว่างไว้เพราะใช้เพียงเป็นตัวจัดเก็บ annotations ข้างต้นเท่านั้น

    @BeforeAll
    public static void initFxRuntime() {
        javafx.application.Platform.startup(() -> {}
        );
    }
}
