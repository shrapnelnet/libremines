package com.shr4pnel.minesweeper;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The application opening point, used to bootstrap JavaFX and open to the GUI.
 * @since 1.0.0
 */
public class Main extends Application {
    /**
     * JavaFX opening method. Creates the stage and bootstraps the application.
     *
     * @param stage The stage object passed in by JavaFX.
     * @throws IOException If the FXML template or app icon are not found in resources.
     */
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

    /**
     * Application opening method. Calls the JavaFX start method.
     *
     * @param args Optional commandline parameters, unimplemented.
     * @see #start(Stage)
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Cancels the timer on application stop.
     */
    @Override
    @FXML
    public void stop() {
        Controller.timer.cancel();
    }
}