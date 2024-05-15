package com.shr4pnel.minesweeper;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
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


    // i already said i'm sorry!
    Set<String> visitedTiles = new HashSet<>();
    Grid gridHandler;
    GridWrapper wrapper;
    boolean gameOver = false;

    @FXML
    private void initialize() {
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
    }

    @FXML
    private void gridClicked(MouseEvent event) {
        if (gameOver)
            return;
        Node tileClicked = event.getPickResult().getIntersectedNode();
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        if (wrapper.atColumn(column).atRow(row).isBomb()) {
            gameOver = true;
            System.out.println("DEAD!!!");
            URL smileyURL = getClass().getResource("img/face_dead.png");
            smiley.setImage(new Image(smileyURL.toString()));
            ImageView tileClickedImage = (ImageView) tileClicked;
            URL deathBombURL = getClass().getResource("img/bomb_death.png");
            tileClickedImage.setImage(new Image(deathBombURL.toString()));
            showAllBombs();
            return;
            // todo do stuff!!
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
                    currentBombTile.setImage(new Image(bombRevealedURL.toString()));
                }
            }
        }
    }

    void setAdjacentCount(Node tileClicked) {
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        ImageView image = (ImageView) tileClicked;
        int adjacentBombs = wrapper.atColumn(column).atRow(row).adjacentBombCount();
        URL imageURL = getClass().getResource("img/num_" + adjacentBombs + ".png");
        image.setImage(new Image(imageURL.toString()));
    }

    void expandGrid(int baseColumn, int baseRow) {

    }
}