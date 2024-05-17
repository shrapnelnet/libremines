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
    private GridPane grid;

    @FXML
    private ImageView smiley, time_1, time_2, time_3, bomb_2, bomb_3;

    private Grid gridHandler;
    private GridWrapper wrapper;
    private boolean gameOver = false;
    private boolean isFirstLoad = true;
    static Timer timer = new Timer();
    private int time = 0;
    private long startTime;
    private int bombCount = 99;
    private boolean[][] expandedTiles;

    @FXML
    private void initialize() {
        setupGrid();
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
        expandedTiles = new boolean[30][16];

    }

    private void setupGrid() {
        for (int column = 0; column < 30; ++column) {
            for (int row = 0; row < 16; ++row) {
                Button blankButton = createBlankButton();
                grid.add(blankButton, column, row);
            }
        }
    }

    private Button createBlankButton() {
        Image blank =
            new Image(String.valueOf(getClass().getResource("img/blank.png")), 16, 16, true, true);
        ImageView blankImage = new ImageView(blank);
        Button blankButton = new Button();
        blankButton.setGraphic(blankImage);
        blankButton.setMinSize(16, 16);
        blankButton.setOnMouseClicked(this::buttonClicked);
        return blankButton;
    }

    private void buttonClicked(MouseEvent e) {
        if (gameOver) {
            return;
        }
        Button clicked = (Button) e.getSource();
        int column = GridPane.getColumnIndex(clicked);
        int row = GridPane.getRowIndex(clicked);

        if (isFirstLoad) {
            scheduleTimer();
            isFirstLoad = false;
        }

        if (e.getButton() == MouseButton.SECONDARY) {
            flag(clicked);
        } else {
            handlePrimaryClick(clicked, column, row);
        }
    }

    private void handlePrimaryClick(Button clicked, int column, int row) {
        if (wrapper.atColumn(column).atRow(row).isBomb()) {
            gameOver(clicked);
        } else {
            int adjacentBombs = wrapper.adjacentBombCount();
            setAdjacentCount(clicked, adjacentBombs);
            if (adjacentBombs == 0) {
                recursiveExpandTiles(column, row);
            }
        }
    }

    private void recursiveExpandTiles(int column, int row) {
        if (column < 0 || column >= 30 || row < 0 || row >= 16 || expandedTiles[column][row] && !wrapper.atColumn(column).atRow(row).isBomb())
            return;
        expandTile(column, row);
        if (wrapper.atColumn(column).atRow(row).adjacentBombCount() == 0) {
            recursiveExpandTiles(column, row - 1);
            recursiveExpandTiles(column + 1, row - 1);
            recursiveExpandTiles(column + 1, row);
            recursiveExpandTiles(column + 1, row + 1);
            recursiveExpandTiles(column, row + 1);
            recursiveExpandTiles(column - 1, row + 1);
            recursiveExpandTiles(column - 1, row);
            recursiveExpandTiles(column - 1, row - 1);
        }
    }

    private void expandTile(int column, int row) {
        Node tile = getNodeByRowColumnIndex(row, column);
        if (tile != null) {
            Button button = (Button) tile;
            if (button.isVisible()) {
                int adjacentBombs = wrapper.atColumn(column).atRow(row).adjacentBombCount();
                setAdjacentCount(button, adjacentBombs);
                if (adjacentBombs == 0 && !expandedTiles[column][row]) {
                    expandedTiles[column][row] = true;
                    recursiveExpandTiles(column, row);
                }
            }
        }
    }

    private Node getNodeByRowColumnIndex(int row, int column) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    @FXML
    private void reinitialize() {
        bombCount = 99;
        updateBombCounter();
        resetTimer();
        resetGrid();
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
    }

    private void resetTimer() {
        URL zeroSecondURL = getClass().getResource("img/0_seconds.png");
        time_1.setImage(new Image(String.valueOf(zeroSecondURL)));
        time_2.setImage(new Image(String.valueOf(zeroSecondURL)));
        time_3.setImage(new Image(String.valueOf(zeroSecondURL)));
        timer.cancel();
        isFirstLoad = true;
        timer = new Timer();
    }

    private void resetGrid() {
        URL blank = getClass().getResource("img/blank.png");
        for (Node node : grid.getChildren()) {
            Button button = (Button) node;
            button.setGraphic(new ImageView(new Image(String.valueOf(blank), 16, 16, true, false)));
        }
    }

    private void gameOver(Node tileClicked) {
        gameOver = true;
        timer.cancel();
        setImage(smiley, "img/face_dead.png");
        setImage((Button) tileClicked, "img/bomb_death.png");
        showAllBombs(GridPane.getColumnIndex(tileClicked), GridPane.getRowIndex(tileClicked));
    }

    private void flag(Node tileClicked) {
        Button tileAsButton = (Button) tileClicked;
        ImageView tileGraphic = (ImageView) tileAsButton.getGraphic();
        Image tileGraphicImage = tileGraphic.getImage();
        boolean flagged = tileGraphicImage.getUrl().contains("flagged.png");
        if (flagged) {
            bombCount++;
            setImage(tileAsButton, "img/blank.png");
            updateBombCounter();
            return;
        }
        bombCount--;
        updateBombCounter();
        setImage((Button) tileClicked, "img/bomb_flagged.png");

    }

    private void updateBombCounter() {
        String bombCountString = String.format("%03d", bombCount);
        setBombCounterImage(bomb_2, bombCountString.charAt(1));
        setBombCounterImage(bomb_3, bombCountString.charAt(2));
    }

    private void setBombCounterImage(ImageView imageView, char digit) {
        URL imageURL = getClass().getResource("img/" + digit + "_seconds.png");
        imageView.setImage(new Image(String.valueOf(imageURL)));
    }

    private void scheduleTimer() {
        startTime = System.currentTimeMillis();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (time >= 999) {
                    timer.cancel();
                } else {
                    updateTimer();
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }

    private void updateTimer() {
        long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        time = (int) (elapsedTimeMillis / 1000);
        String timeString = String.format("%03d", time);
        setTimerImage(time_1, timeString.charAt(0));
        setTimerImage(time_2, timeString.charAt(1));
        setTimerImage(time_3, timeString.charAt(2));
    }

    private void setTimerImage(ImageView imageView, char digit) {
        URL imageURL = getClass().getResource("img/" + digit + "_seconds.png");
        imageView.setImage(new Image(String.valueOf(imageURL)));
    }

    private void showAllBombs(int clickedColumn, int clickedRow) {
        for (Node node : grid.getChildren()) {
            int column = GridPane.getColumnIndex(node);
            int row = GridPane.getRowIndex(node);
            if (!(column == clickedColumn && row == clickedRow) &&
                wrapper.atColumn(column).atRow(row).isBomb()) {
                setImage((Button) node, "img/bomb_revealed.png");
            }
        }
    }

    private void setImage(Button button, String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        button.setGraphic(new ImageView(new Image(String.valueOf(imageURL), 16, 16, true, false)));
    }

    private void setImage(ImageView imageView, String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        imageView.setImage(new Image(String.valueOf(imageURL)));
    }

    @FXML
    private void smileyPressed() {
        setImage(smiley, "img/face_smile_pressed.png");
    }

    @FXML
    private void smileyReleased() {
        gameOver = false;
        setImage(smiley, "img/face_smile.png");
        reinitialize();
    }

    private void setAdjacentCount(Node tileClicked, int adjacentBombs) {
        Button button = (Button) tileClicked;
        URL imageURL = getClass().getResource("img/num_" + adjacentBombs + ".png");
        button.setGraphic(new ImageView(new Image(String.valueOf(imageURL), 16, 16, true, false)));
    }

    private int getAdjacentCount(Node tileClicked) {
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        return wrapper.atColumn(column).atRow(row).adjacentBombCount();
    }
}
