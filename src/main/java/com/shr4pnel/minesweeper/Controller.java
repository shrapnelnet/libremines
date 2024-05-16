package com.shr4pnel.minesweeper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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

    @FXML
    ImageView bomb_2;

    @FXML
    ImageView bomb_3;


    Grid gridHandler;
    GridWrapper wrapper;
    boolean gameOver = false;
    boolean isFirstLoad = true;
    static Timer timer = new Timer();
    int time = 0;
    long startTime;
    int bombCount = 99;
    private Node[][] gridPaneArray;


    @FXML
    private void initialize() {
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
        gridPaneArray = new Node[30][16];
        for (Node node : grid.getChildren()) {
            gridPaneArray[GridPane.getColumnIndex(node)][GridPane.getRowIndex(node)] = node;
        }
    }


    private void reinitialize() {
        bombCount = 99;
        updateBombCounter();
        URL zeroSecondURL = getClass().getResource("img/0_seconds.png");
        time_1.setImage(new Image(String.valueOf(zeroSecondURL)));
        time_2.setImage(new Image(String.valueOf(zeroSecondURL)));
        time_3.setImage(new Image(String.valueOf(zeroSecondURL)));
        timer.cancel();
        isFirstLoad = true;
        timer = new Timer();
        URL blank = getClass().getResource("img/blank.png");
        for (Node n : grid.getChildren()) {
            ImageView nodeAsImage = (ImageView) n;
            nodeAsImage.setImage(new Image(String.valueOf(blank)));
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
            scheduleTimer();
        }
        isFirstLoad = false;
        Node tileClicked = event.getPickResult().getIntersectedNode();
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        if (event.getButton() == MouseButton.SECONDARY) {
            flag(tileClicked);
            return;
        }
        if (wrapper.atColumn(column).atRow(row).isBomb()) {
            gameOver(tileClicked);
            return;
        }
        int adjacentBombs = getAdjacentCount(tileClicked);
        setAdjacentCount(tileClicked);
        if (adjacentBombs == 0) {
            System.out.println("todo. implement this stupid!");
        }
    }

    void gameOver(Node tileClicked) {
        gameOver = true;
        timer.cancel();
        System.out.println("DEAD!!!");
        URL smileyURL = getClass().getResource("img/face_dead.png");
        smiley.setImage(new Image(String.valueOf(smileyURL)));
        ImageView tileClickedImage = (ImageView) tileClicked;
        URL deathBombURL = getClass().getResource("img/bomb_death.png");
        tileClickedImage.setImage(new Image(String.valueOf(deathBombURL)));
        showAllBombs(GridPane.getColumnIndex(tileClicked), GridPane.getRowIndex(tileClicked));
    }

    void flag(Node tileClicked) {
        bombCount--;
        URL flagURL = getClass().getResource("img/bomb_flagged.png");
        Image flagImage = new Image(String.valueOf(flagURL));
        ImageView tile = (ImageView) tileClicked;
        if (tile.getImage().equals(flagImage)) {
            bombCount++;
        }
        updateBombCounter();
        tile.setImage(new Image(String.valueOf(flagURL)));
    }

    void updateBombCounter() {
        String bombCountString = String.valueOf(bombCount);
        char[] bombCountCharArray = bombCountString.toCharArray();
        if (bombCountString.length() == 2) {
            URL bombCountTensURL =
                getClass().getResource("img/" + bombCountCharArray[0] + "_seconds.png");
            URL bombCountUnitsURL =
                getClass().getResource("img/" + bombCountCharArray[1] + "_seconds.png");
            bomb_2.setImage(new Image(String.valueOf(bombCountTensURL)));
            bomb_3.setImage(new Image(String.valueOf(bombCountUnitsURL)));
            return;
        }
        URL bombCountUnitsURL =
            getClass().getResource("img/" + bombCountCharArray[1] + "_seconds.png");
        bomb_3.setImage(new Image(String.valueOf(bombCountUnitsURL)));
    }

    void scheduleTimer() {
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

    void showAllBombs(int clickedColumn, int clickedRow) {
        URL bombRevealedURL = getClass().getResource("img/bomb_revealed.png");
        for (Node node : grid.getChildren()) {
            int column = GridPane.getColumnIndex(node);
            int row = GridPane.getRowIndex(node);
            if (column == clickedColumn && row == clickedRow) {
                continue;
            }
            if (wrapper.atColumn(column).atRow(row).isBomb()) {
                ImageView imageView = (ImageView) node;
                imageView.setImage(new Image(String.valueOf(bombRevealedURL)));
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
        int adjacentBombs = getAdjacentCount(tileClicked);
        ImageView image = (ImageView) tileClicked;
        URL imageURL = getClass().getResource("img/num_" + adjacentBombs + ".png");
        image.setImage(new Image(String.valueOf(imageURL)));
    }

    int getAdjacentCount(Node tileClicked) {
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        return wrapper.atColumn(column).atRow(row).adjacentBombCount();

    }
}