package com.shr4pnel.minesweeper;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(String.valueOf(getClass().getResource("winmine.png")));
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("minesweeper.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Minesweeper");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    @FXML
    public void stop() {
        Controller.timer.cancel();
    }
}