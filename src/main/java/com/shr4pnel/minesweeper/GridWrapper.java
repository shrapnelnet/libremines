package com.shr4pnel.minesweeper;

//class ValidTileBean {
//    boolean top;
//    boolean topRight;
//    boolean right;
//    boolean bottomRight;
//    boolean bottom;
//    boolean bottomLeft;
//    boolean left;
//    boolean topLeft;
//}


public class GridWrapper {
    final boolean[][] grid = new boolean[30][16];
    private int currentColumn;
    private int currentRow;


    public GridWrapper() {
        this.currentColumn = 0;
        this.currentRow = 0;
    }

    GridWrapper atColumn(int column) {
        this.currentColumn = column;
        return this;
    }

    GridWrapper atRow(int row) {
        this.currentRow = row;
        return this;
    }

    void setBomb() {
        grid[this.currentColumn][this.currentRow] = true;
    }

    boolean isBomb() {
        return grid[currentColumn][currentRow];
    }

    int adjacentBombCount() {
        boolean checkLeft, checkRight, checkUp, checkDown;
        int count = 0;
        checkLeft = currentColumn > 0;
        checkRight = currentColumn < 29;
        checkUp = currentRow > 0;
        checkDown = currentRow < 15;

        if (checkLeft) {
            count += left();
        }
        if (checkRight) {
            count += right();
        }
        if (checkUp) {
            count += top();
        }
        if (checkDown) {
            count += bottom();
        }
        if (checkUp && checkLeft) {
            count += topLeft();
        }

        if (checkUp && checkRight) {
            count += topRight();
        }
        if (checkDown && checkRight) {
            count += bottomRight();
        }
        if (checkDown && checkLeft) {
            count += bottomLeft();
        }

        return count;
    }

    private int top() {
        return isValid() && currentRow > 0 ? (grid[currentColumn][currentRow - 1] ? 1 : 0) : 0;
    }

    private int topRight() {
        return isValid() && currentColumn < 29 && currentRow > 0 ?
            (grid[currentColumn + 1][currentRow - 1] ? 1 : 0) : 0;
    }

    private int right() {
        return isValid() && currentColumn < 29 ? (grid[currentColumn + 1][currentRow] ? 1 : 0) : 0;
    }

    private int bottomRight() {
        return isValid() && currentColumn < 29 && currentRow < 15 ?
            (grid[currentColumn + 1][currentRow + 1] ? 1 : 0) : 0;
    }

    private int bottom() {
        return isValid() && currentRow < 15 ? (grid[currentColumn][currentRow + 1] ? 1 : 0) : 0;
    }

    private int bottomLeft() {
        return isValid() && currentColumn > 0 && currentRow < 15 ?
            (grid[currentColumn - 1][currentRow + 1] ? 1 : 0) : 0;
    }

    private int left() {
        return isValid() && currentColumn > 0 ? (grid[currentColumn - 1][currentRow] ? 1 : 0) : 0;
    }

    private int topLeft() {
        return isValid() && currentColumn > 0 && currentRow > 0 ?
            (grid[currentColumn - 1][currentRow - 1] ? 1 : 0) : 0;
    }

    boolean isValid() {
        return this.currentColumn >= 0 && this.currentColumn < 30 && this.currentRow >= 0 &&
            this.currentRow < 16;
    }

//    ValidTileBean getBounds() {
//        ValidTileBean validTiles = new ValidTileBean();
//        validTiles.top = top() != 0;
//        validTiles.topRight = topRight() != 0;
//        validTiles.right = right() != 0;
//        validTiles.bottomRight = bottomRight() != 0;
//        validTiles.bottom = bottom() != 0;
//        validTiles.bottomLeft = bottomLeft() != 0;
//        validTiles.left = left() != 0;
//        validTiles.topLeft = topLeft() != 0;
//        return validTiles;
//    }

    void printGrid() {
        int i, j;
        for (i = 0; i < 30; ++i) {
            for (j = 0; j < 16; ++j) {
                System.out.printf(this.atColumn(i).atRow(j).isBomb() + " ");
            }
            System.out.println();
        }
    }
}
