package com.shr4pnel.minesweeper;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Controller {
    /**
     * The grid within the FXML holding all the tiles.
     */
    @FXML
    private GridPane grid;

    /**
     * Images within the FXML structure.
     */
    @FXML
    private ImageView smiley, time_1, time_2, time_3, bomb_2, bomb_3;

    /**
     * Menu items
     */
    @FXML
    private RadioMenuItem color, marks;

    /**
     * The menu item "about".
     */
    @FXML
    private MenuItem about;

    /**
     * The underlying 2D array representing where bombs are on the grid.
     */
    private Grid gridHandler;

    /**
     * A wrapper to make operations on the underlying grid easier.
     */
    private GridWrapper wrapper;

    /**
     * Handles the grid during a game over state preventing further clicking of the grid.
     */
    private boolean gameOver = false;

    /**
     * A variable specifying if the game has been loaded for the first time.
     */
    private boolean isFirstLoad = true;

    /**
     * The game timer.
     */
    static Timer timer = new Timer();

    /**
     * The time that has elapsed since the game started.
     */
    private int time = 0;

    /**
     * The time the game was started at.
     */
    private long startTime;

    /**
     * The number of bombs on the grid.
     */
    private int bombCount = 99;

    /**
     * A 2D array holding all tiles that have previously recursively been expanded.
     */
    private boolean[][] expandedTiles;

    /**
     * A boolean that shows if the user has performed their first click on the GridPane.
     */
    private boolean isFirstClick = true;

    /**
     * Sets important variables on Main class initialization
     */
    @FXML
    private void initialize() {
        setNotYetImplemented(color);
        setNotYetImplemented(marks);
        about.setOnAction(this::openAbout);
        setupGrid();
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
        expandedTiles = new boolean[30][16];
    }

    /**
     * Opens the about window when the radiomenuitem is clicked
     *
     * @param actionEvent The node that the window opens on, on click
     */
    private void openAbout(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("about.fxml"));
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a window if an unimplemented menu item is clicked
     *
     * @param node The node that the window opens on, on click
     */
    private void setNotYetImplemented(RadioMenuItem node) {
        node.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("unimplemented.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Unimplemented!");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Fills the GridPane with blank buttons
     */
    private void setupGrid() {
        for (int column = 0; column < 30; ++column) {
            for (int row = 0; row < 16; ++row) {
                Button blankButton = createBlankButton();
                grid.add(blankButton, column, row);
            }
        }
    }

    /**
     * Used during initialization to fill the board
     *
     * @return A blank button
     */
    private Button createBlankButton() {
        Image blank = new Image(String.valueOf(getClass().getResource("img/blank.png")), 16, 16, true, false);
        ImageView blankImage = new ImageView(blank);
        Button blankButton = new Button();
        blankButton.setGraphic(blankImage);
        blankButton.setMinSize(16, 16);
        blankButton.setOnMouseClicked(this::buttonClicked);
        blankButton.setOnMousePressed(this::mouseHeld);
        blankButton.setOnMouseReleased(this::mouseReleased);
        return blankButton;
    }

    /**
     * Changes smiley face to concerned look when the mouse is held
     *
     * @param mouseEvent Stub, required by EventHandler interface
     */
    private void mouseHeld(MouseEvent mouseEvent) {
        if (gameOver)
            return;
        setImage(smiley, "img/face_ooh.png");
    }

    /**
     * Changes smiley face back to smiling after mouse release
     *
     * @param mouseEvent Stub, required by EventHandler interface
     */
    private void mouseReleased(MouseEvent mouseEvent) {
        if (gameOver)
            return;
        setImage(smiley, "img/face_smile.png");
    }

    /**
     * Handles the main GridPane being clicked
     * Identifies the intention of the click (e.g. flag, open tile)
     *
     * @param e The event fired on click
     */
    private void buttonClicked(MouseEvent e) {
        if (gameOver) {
            return;
        }
        Button clicked = (Button) e.getSource();
        ImageView buttonImageView = (ImageView) clicked.getGraphic();
        Image buttonImage = buttonImageView.getImage();
        int column = GridPane.getColumnIndex(clicked);
        int row = GridPane.getRowIndex(clicked);
        if (isFirstLoad) {
            scheduleTimer();
            isFirstLoad = false;
        }
        if (e.getButton() == MouseButton.SECONDARY) {
            flag(clicked);
            return;
        }
        if (buttonImage.getUrl().contains("flagged.png")) {
            return;
        }
//        if (buttonImage.getUrl().contains("num") && !buttonImage.getUrl().contains("num_0.png")) {
//            chord(clicked, buttonImage.getUrl());
//            return;
//        }
        handlePrimaryClick(clicked, column, row);
    }

//    private void chord(Button clicked, String clickedURL) {
//        int column = GridPane.getColumnIndex(clicked);
//        int row = GridPane.getRowIndex(clicked);
//        int urlLength = clickedURL.length();
//        char expectedAdjacentChar = clickedURL.charAt(urlLength-5);
//        int expectedAdjacent = Integer.parseInt(Character.toString(expectedAdjacentChar));
//        int actualAdjacent = wrapper.atColumn(column).atRow(row).adjacentBombCount();
//        if (expectedAdjacent != actualAdjacent) {
//            return;
//        }
//        // chord logic
//    }

    /**
     * If the first tile clicked was a bomb, move that bomb to the first available column on row 0 before opening it
     *
     * @param column The column of the clicked tile
     * @param row    The row of the clicked tile
     * @return An array containing the column and row the bomb was moved to
     */
    private int[] setBombIfFirstTileIsBomb(int column, int row) {
        for (int c = 0; c < 30; ++c) {
            for (int r = 0; r < 16; ++r) {
                if (!wrapper.atColumn(c).atRow(r).isBomb()) {
                    wrapper.setBomb();
                    wrapper.updateGrid(column, row, c, r);
                    return new int[]{c, r};
                }
            }
        }
        // this realistically should never happen... sorry!
        return null;
    }

    /**
     * Extracts the URL of a given button
     *
     * @param button A javafx.scene.control.Button object
     * @return A string representation of the image URL
     */
    private String getButtonURL(Button button) {
        ImageView graphic = (ImageView) button.getGraphic();
        Image image = graphic.getImage();
        return image.getUrl();
    }

    /**
     * Iterates through the grid to find all tiles that are either blank or flagged
     *
     * @return The number of tiles that were blank or flagged
     */
    private int numberOfUnrevealedTiles() {
        int unrevealedTiles = 0;
        int column, row;
        for (column = 0; column < 30; ++column) {
            for (row = 0; row < 16; ++row) {
                Button current = (Button) getNodeByRowColumnIndex(row, column);
                assert current != null;
                String currentURL = getButtonURL(current);
                if (currentURL.contains("blank.png") || currentURL.contains("flag.png")) {
                    unrevealedTiles++;
                }
            }
        }
        return unrevealedTiles;
    }

    /**
     * Checks if the number of unrevealed tiles is equal to 99 (the win condition)
     *
     * @return A boolean representing if the game has been won
     * @see #numberOfUnrevealedTiles()
     */
    private boolean checkWinCondition() {
        int unrevealedTiles = numberOfUnrevealedTiles();
        return unrevealedTiles == 99;
    }

    /**
     * Handles a left mouse click on an unopened tile
     *
     * @param clicked The button that was clicked
     * @param column  The column that it was clicked at
     * @param row     The row that it was clicked at
     */
    private void handlePrimaryClick(Button clicked, int column, int row) {
        if (wrapper.atColumn(column).atRow(row).isBomb() && !isFirstClick) {
            gameOver(clicked);
            return;
        }
        if (wrapper.isBomb() && isFirstClick) {
            int[] chosenColumnAndRow = setBombIfFirstTileIsBomb(column, row);
            // assertions are evil but i dont care
            assert chosenColumnAndRow != null;
            int columnMovedTo = chosenColumnAndRow[0];
            int rowMovedTo = chosenColumnAndRow[1];
            wrapper.atColumn(column).atRow(row).switchBomb(columnMovedTo, rowMovedTo);
            recursiveExpandTiles(column, row);
            clicked = (Button) getNodeByRowColumnIndex(row, column);
        }
        isFirstClick = false;
        int adjacentBombs = wrapper.adjacentBombCount();
        setAdjacentCount(clicked, adjacentBombs);
        if (adjacentBombs == 0) {
            recursiveExpandTiles(column, row);
        }
        boolean win = checkWinCondition();
        if (win) {
            win();
        }
    }

    /**
     * If a tile is surrounded by tiles with no adjacent bombs, open all of the tiles recursively
     *
     * @param column The column that was clicked
     * @param row    The row that was clicked
     * @see #expandTile(int, int)
     */
    private void recursiveExpandTiles(int column, int row) {
        if (column < 0 || column >= 30 || row < 0 || row >= 16 ||
                expandedTiles[column][row] && !wrapper.atColumn(column).atRow(row).isBomb()) {
            return;
        }
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

    /**
     * Opens a tile, and adds it to an array to prevent overflow
     *
     * @param column The column of the button to open
     * @param row    The row of the button to open
     */
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

    /**
     * Iterates over the grid and returns the node when it's at the correct child
     *
     * @param row    The desired row to find
     * @param column The desired column to find
     * @return The node at the specified position
     */
    private Node getNodeByRowColumnIndex(int row, int column) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    /**
     * Resets important state variables on restart
     */
    @FXML
    private void reinitialize() {
        gameOver = false;
        bombCount = 99;
        updateBombCounter();
        resetTimer();
        resetGrid();
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
        expandedTiles = new boolean[30][16];
        isFirstClick = true;
    }

    /**
     * Resets the timer to 0
     *
     * @see #reinitialize()
     */
    private void resetTimer() {
        URL zeroSecondURL = getClass().getResource("img/0_seconds.png");
        time_1.setImage(new Image(String.valueOf(zeroSecondURL)));
        time_2.setImage(new Image(String.valueOf(zeroSecondURL)));
        time_3.setImage(new Image(String.valueOf(zeroSecondURL)));
        timer.cancel();
        isFirstLoad = true;
        timer = new Timer();
    }

    // todo figure out why createBlankButton errors here

    /**
     * Refills the grid with new blank buttons
     */
    private void resetGrid() {
        URL blank = getClass().getResource("img/blank.png");
        for (Node node : grid.getChildren()) {
            Button button = (Button) node;
            button.setGraphic(new ImageView(new Image(String.valueOf(blank), 16, 16, true, false)));
        }
    }

    /**
     * When a bomb is clicked, change the smiley, set gameOver to true.
     *
     * @param tileClicked The node that was clicked, to set the correct image
     */
    private void gameOver(Node tileClicked) {
        gameOver = true;
        timer.cancel();
        setImage(smiley, "img/face_dead.png");
        setImage((Button) tileClicked, "img/bomb_death.png");
        showAllBombs(GridPane.getColumnIndex(tileClicked), GridPane.getRowIndex(tileClicked));
    }

    /**
     * Creates a win state, change the smiley.
     */
    private void win() {
        gameOver = true;
        timer.cancel();
        setImage(smiley, "img/face_win.png");
        flagAllRemaining();
    }

    /**
     * After a win, set every bomb tile that is unflagged to flagged.
     */
    private void flagAllRemaining() {
        int column, row;
        for (column = 0; column < 30; ++column) {
            for (row = 0; row < 16; ++row) {
                Button current = (Button) getNodeByRowColumnIndex(row, column);
                assert current != null;
                String currentURL = getButtonURL(current);
                boolean tileIsBomb = wrapper.atColumn(column).atRow(row).isBomb();
                if (currentURL.contains("blank.png") && tileIsBomb) {
                    setImage(current, "img/bomb_flagged.png");
                }
            }
        }
    }

    /**
     * Create a flag on a tile, representing a tile that the user suspects has a bomb behind it.
     * This prevents the tile being clicked on.
     *
     * @param tileClicked
     */
    private void flag(Node tileClicked) {
        Button tileAsButton = (Button) tileClicked;
        ImageView tileGraphic = (ImageView) tileAsButton.getGraphic();
        Image tileGraphicImage = tileGraphic.getImage();
        if (!tileGraphicImage.getUrl().contains("blank.png") && !tileGraphicImage.getUrl().contains("flagged.png")) {
            return;
        }
        boolean flagged = tileGraphicImage.getUrl().contains("flagged.png");
        if (flagged) {
            bombCount++;
            setImage(tileAsButton, "img/blank.png");
            updateBombCounter();
            return;
        }
        bombCount--;
        if (bombCount > 0)
            updateBombCounter();
        setImage((Button) tileClicked, "img/bomb_flagged.png");

    }

    /**
     * On flag or reinitialization, update the bomb counter to represent the amount of currently flagged tiles
     */
    private void updateBombCounter() {
        String bombCountString = String.format("%03d", bombCount);
        setBombCounterImage(bomb_2, bombCountString.charAt(1));
        setBombCounterImage(bomb_3, bombCountString.charAt(2));
    }

    /**
     * Used to update the digits of the bomb counter.
     *
     * @param imageView The ImageView representing the counter digit to be changed.
     * @param digit     The digit the counter should be changed to.
     * @see #updateBombCounter()
     */
    private void setBombCounterImage(ImageView imageView, char digit) {
        URL imageURL = getClass().getResource("img/" + digit + "_seconds.png");
        imageView.setImage(new Image(String.valueOf(imageURL)));
    }

    /**
     * Starts the timer when the user clicks or flags their first tile.
     */
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

    /**
     * Calculates the time since the game started, and passes it on.
     *
     * @see #setTimerImage(ImageView, char)
     */
    private void updateTimer() {
        long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        time = (int) (elapsedTimeMillis / 1000);
        String timeString = String.format("%03d", time);
        setTimerImage(time_1, timeString.charAt(0));
        setTimerImage(time_2, timeString.charAt(1));
        setTimerImage(time_3, timeString.charAt(2));
    }

    /**
     * Updates the timer digits individually based on updateTimer()
     *
     * @param imageView The ImageView to be updated
     * @param digit     The digit to update the ImageView with
     * @see #updateTimer()
     */
    private void setTimerImage(ImageView imageView, char digit) {
        URL imageURL = getClass().getResource("img/" + digit + "_seconds.png");
        imageView.setImage(new Image(String.valueOf(imageURL)));
    }

    /**
     * On game over, show every bomb that was left on the grid, as well as every incorrectly flagged bomb.
     *
     * @param clickedColumn The column that caused the game over.
     * @param clickedRow    The row that caused the game over.
     */
    private void showAllBombs(int clickedColumn, int clickedRow) {
        for (Node node : grid.getChildren()) {
            Button b = (Button) node;
            String buttonURL = ((ImageView) b.getGraphic()).getImage().getUrl();
            int column = GridPane.getColumnIndex(node);
            int row = GridPane.getRowIndex(node);
            // if the tile isn't the one that was clicked AND the tile is a bomb
            if (!(column == clickedColumn && row == clickedRow) && wrapper.atColumn(column).atRow(row).isBomb()) {
                setImage((Button) node, "img/bomb_revealed.png");
            }
            if (buttonURL.contains("flagged.png") && !wrapper.atColumn(column).atRow(row).isBomb()) {
                setImage(b, "img/bomb_wrong.png");
            }
        }
    }

    /**
     * Sets a button's graphic.
     *
     * @param button    The button to change.
     * @param imagePath A path to an image to change the graphic to.
     */
    private void setImage(Button button, String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        button.setGraphic(new ImageView(new Image(String.valueOf(imageURL), 16, 16, true, false)));
    }

    /**
     * Replaces an ImageView's graphic with another.
     *
     * @param imageView The ImageView to change.
     * @param imagePath A path to an image to change the graphic to.
     */
    private void setImage(ImageView imageView, String imagePath) {
        URL imageURL = getClass().getResource(imagePath);
        imageView.setImage(new Image(String.valueOf(imageURL)));
    }

    /**
     * Replaces the smiley with a pressed down variant when it is clicked.
     */
    @FXML
    private void smileyPressed() {
        setImage(smiley, "img/face_smile_pressed.png");
    }

    /**
     * Releases the smiley from being pressed down, starts reinitialization.
     *
     * @see #reinitialize()
     */
    @FXML
    private void smileyReleased() {
        setImage(smiley, "img/face_smile.png");
        reinitialize();
    }

    /**
     * Sets the graphic of a tile to indicate how many bombs are adjacent to it.
     *
     * @param tileClicked   The tile to update.
     * @param adjacentBombs The number of bombs adjacent to the tile.
     */
    private void setAdjacentCount(Node tileClicked, int adjacentBombs) {
        Button button = (Button) tileClicked;
        setImage(button, "img/num_" + adjacentBombs + ".png");
    }
}
