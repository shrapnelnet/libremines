package com.shr4pnel.minesweeper;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
        for (int column = 0; column < 30; ++column) {
            for (int row = 0; row < 16; ++row) {
                Image blank =
                    new Image(String.valueOf(getClass().getResource("img/blank.png")), 16, 16, true,
                        true);
                ImageView blankImage = new ImageView(blank);
                Button blankButton = new Button();
                blankButton.setGraphic(blankImage);
                blankButton.setMinSize(16, 16);
                blankButton.setStyle(
                    "-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
                blankButton.setOnMouseClicked(this::buttonClicked);
                grid.add(blankButton, column, row);
            }
        }
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
        gridPaneArray = new Node[30][16];
    }

    void buttonClicked(MouseEvent e) {
        Button clicked = (Button) e.getSource();
        int column = GridPane.getColumnIndex(clicked);
        int row = GridPane.getRowIndex(clicked);
        if (gameOver) {
            return;
        }
        if (isFirstLoad) {
            scheduleTimer();
        }
        isFirstLoad = false;
        if (e.getButton() == MouseButton.SECONDARY) {
            flag(clicked);
            return;
        }
        if (wrapper.atColumn(column).atRow(row).isBomb()) {
            gameOver(clicked);
            return;
        }
        int adjacentBombs = getAdjacentCount(clicked);
        setAdjacentCount(clicked);
        if (adjacentBombs == 0) {
            System.out.println("todo. implement this stupid!");
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
            Button nodeAsButton = (Button) n;
            nodeAsButton.setGraphic(new ImageView(new Image(String.valueOf(blank), 16, 16, true, false)));
        }
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
    }

//    @FXML
//    private void gridClicked(MouseEvent event) {

    void gameOver(Node tileClicked) {
        gameOver = true;
        timer.cancel();
        System.out.println("DEAD!!!");
        URL smileyURL = getClass().getResource("img/face_dead.png");
        smiley.setImage(new Image(String.valueOf(smileyURL)));
        Button tileClickedButton = (Button) tileClicked;
        URL deathBombURL = getClass().getResource("img/bomb_death.png");
        ImageView deathBombImage =
            new ImageView(new Image(String.valueOf(deathBombURL), 16, 16, true, false));
        tileClickedButton.setGraphic(deathBombImage);
        tileClickedButton.setMinSize(16, 16);
        tileClickedButton.setPrefSize(16, 16);
        showAllBombs(GridPane.getColumnIndex(tileClicked), GridPane.getRowIndex(tileClicked));
    }

    void flag(Node tileClicked) {
        bombCount--;
        URL flagURL = getClass().getResource("img/bomb_flagged.png");
        Image flagImage = new Image(String.valueOf(flagURL), 16, 16, true, false);
        Button tile = (Button) tileClicked;
//        if (tile.getGraphic().) {
//            bombCount++;
//        }
        updateBombCounter();
        tile.setGraphic(new ImageView(new Image(String.valueOf(flagURL), 16, 16, true, false)));
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
                Button button = (Button) node;
                ImageView buttonGraphic =
                    new ImageView(new Image(String.valueOf(bombRevealedURL), 16, 16, true, false));
                button.setGraphic(buttonGraphic);
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
        Button button = (Button) tileClicked;
        URL imageURL = getClass().getResource("img/num_" + adjacentBombs + ".png");
        Button adjacentButton = new Button();
        adjacentButton.setMinSize(16, 16);
        ImageView buttonImage =
            new ImageView(new Image(String.valueOf(imageURL), 16, 16, true, false));
        button.setGraphic(buttonImage);
    }

    int getAdjacentCount(Node tileClicked) {
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        return wrapper.atColumn(column).atRow(row).adjacentBombCount();

    }
}