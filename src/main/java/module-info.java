module libremines {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens com.shr4pnel.minesweeper to javafx.fxml, javafx.graphics;
}