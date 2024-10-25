module se233.teamnoonkhemii {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens se233.teamnoonkhemii to javafx.fxml;
    exports se233.teamnoonkhemii;
}