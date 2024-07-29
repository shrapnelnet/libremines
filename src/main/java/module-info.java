/**
 * The main module for the program. Required to launch to JavaFX as well as bundle into a native binary.
 * @author shrapnelnet admin@shr4pnel.com
 * @since 1.0.0
 */
module libremines {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens com.shr4pnel.minesweeper to javafx.fxml, javafx.graphics;
}