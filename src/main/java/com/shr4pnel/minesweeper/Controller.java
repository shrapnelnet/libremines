package com.shr4pnel.minesweeper;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
    GridPane grid;

    @FXML
    ImageView smiley;

    @FXML
    ImageView time_1;

    @FXML
    ImageView time_2;

    @FXML
    ImageView time_3;


    // i already said i'm sorry!
    Set<String> visitedTiles = new HashSet<>();
    Grid gridHandler;
    GridWrapper wrapper;
    boolean gameOver = false;
    boolean isFirstLoad = true;
    static Timer timer = new Timer();
    int time = 0;
    long startTime;

    @FXML
    private void initialize() {
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
    }


    private void reinitialize() {
        timer = new Timer();
        URL blank = getClass().getResource("img/blank.png");
        for (Node n : grid.getChildren()) {
            ImageView nodeAsImage = (ImageView) n;
            nodeAsImage.setImage(new Image(blank.toString()));
        }
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
    }

    @FXML
    private void gridClicked(MouseEvent event) {
        if (gameOver) {
            return;
        }
        if (isFirstLoad) {
            startTime = System.currentTimeMillis();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (time >= 999) {
                        timer.cancel();
                        return;
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    long elapsedTimeMillis = currentTimeMillis - startTime;
                    time = (int) (elapsedTimeMillis / 1000);
                    String timeString = String.valueOf(time);
                    char[] timeStringArray = timeString.toCharArray();
                    int length = timeString.length();
                    if (length == 3) {
                        char hundred = timeStringArray[0];
                        char tens = timeStringArray[1];
                        char unit = timeStringArray[2];
                        URL hundredURL = getClass().getResource("img/" + hundred + "_seconds.png");
                        URL tensURL = getClass().getResource("img/" + tens + "_seconds.png");
                        URL unitURL = getClass().getResource("img/" + unit + "_seconds.png");
                        time_1.setImage(new Image(String.valueOf(hundredURL)));
                        time_2.setImage(new Image(String.valueOf(tensURL)));
                        time_3.setImage(new Image(String.valueOf(unitURL)));
                    } else if (length == 2) {
                        char tens = timeStringArray[0];
                        char unit = timeStringArray[1];
                        URL tensURL = getClass().getResource("img/" + tens + "_seconds.png");
                        URL unitURL = getClass().getResource("img/" + unit + "_seconds.png");
                        time_2.setImage(new Image(String.valueOf(tensURL)));
                        time_3.setImage(new Image(String.valueOf(unitURL)));
                    } else if (length == 1) {
                        char unit = timeStringArray[0];
                        URL unitURL = getClass().getResource("img/" + unit + "_seconds.png");
                        time_3.setImage(new Image(String.valueOf(unitURL)));
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
        Node tileClicked = event.getPickResult().getIntersectedNode();
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        if (wrapper.atColumn(column).atRow(row).isBomb()) {
            gameOver = true;
            System.out.println("DEAD!!!");
            URL smileyURL = getClass().getResource("img/face_dead.png");
            smiley.setImage(new Image(String.valueOf(smileyURL)));
            ImageView tileClickedImage = (ImageView) tileClicked;
            URL deathBombURL = getClass().getResource("img/bomb_death.png");
            tileClickedImage.setImage(new Image(String.valueOf(deathBombURL)));
            showAllBombs();
            return;
        }
        setAdjacentCount(tileClicked);
        expandGrid(column, row);
    }

    void showAllBombs() {
        Node[] children = grid.getChildren().toArray(new Node[0]);
        int column, row;
        for (column = 0; column < 30; ++column) {
            for (row = 0; row < 16; ++row) {
                if (wrapper.atColumn(column).atRow(row).isBomb()) {
                    ImageView currentBombTile = (ImageView) children[column + row * 30];
                    URL bombRevealedURL = getClass().getResource("img/bomb_revealed.png");
                    currentBombTile.setImage(new Image(String.valueOf(bombRevealedURL)));
                }
            }
        }
    }

    @FXML
    void smileyPressed() {
        URL smileyURL = getClass().getResource("img/face_smile_pressed.png");
        smiley.setImage(new Image(String.valueOf(smileyURL)));
    }

    @FXML
    void smileyReleased() {
        gameOver = false;
        URL smileyURL = getClass().getResource("img/face_smile.png");
        smiley.setImage(new Image(String.valueOf(smileyURL)));
        reinitialize();
    }

    void setAdjacentCount(Node tileClicked) {
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        ImageView image = (ImageView) tileClicked;
        int adjacentBombs = wrapper.atColumn(column).atRow(row).adjacentBombCount();
        URL imageURL = getClass().getResource("img/num_" + adjacentBombs + ".png");
        image.setImage(new Image(String.valueOf(imageURL)));
    }

    void expandGrid(int baseColumn, int baseRow) {

    }
}