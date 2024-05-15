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


    // i already said i'm sorry!
    Set<String> visitedTiles = new HashSet<>();
    Grid gridHandler;
    GridWrapper wrapper;

    @FXML
    private void initialize() {
        gridHandler = new Grid();
        wrapper = gridHandler.grid;
    }

    @FXML
    private void gridClicked(MouseEvent event) {
        Node tileClicked = event.getPickResult().getIntersectedNode();
        int column = GridPane.getColumnIndex(tileClicked);
        int row = GridPane.getRowIndex(tileClicked);
        if (wrapper.atColumn(column).atRow(row).isBomb()) {
            System.out.println("DEAD!!!");
            return;
            // todo do stuff!!
        }

        setAdjacentCount(tileClicked);
        expandGrid(column, row);
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