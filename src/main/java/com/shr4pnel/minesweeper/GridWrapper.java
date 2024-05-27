package com.shr4pnel.minesweeper;

public class GridWrapper {
    private static final int COLUMNS = 30;
    private static final int ROWS = 16;
    final boolean[][] grid = new boolean[COLUMNS][ROWS];
    private int currentColumn;
    private int currentRow;

    public GridWrapper() {
        this.currentColumn = 0;
        this.currentRow = 0;
    }

    public GridWrapper atColumn(int column) {
        this.currentColumn = column;
        return this;
    }

    public GridWrapper atRow(int row) {
        this.currentRow = row;
        return this;
    }

    public void setBomb() {
        if (isValid(currentColumn, currentRow)) {
            grid[currentColumn][currentRow] = true;
        }
    }

    public void switchBomb(int destinationColumn, int destinationRow) {
        if (isValid(currentColumn, currentRow) && isValid(destinationColumn, destinationRow)) {
            grid[destinationColumn][destinationRow] = true;
            grid[currentColumn][currentRow] = false;
        }
    }

    public boolean isBomb() {
        return isValid(currentColumn, currentRow) && grid[currentColumn][currentRow];
    }

    public int adjacentBombCount() {
        int count = 0;

        if (isBombAt(currentColumn - 1, currentRow - 1)) {
            count++;
        }
        if (isBombAt(currentColumn, currentRow - 1)) {
            count++;
        }
        if (isBombAt(currentColumn + 1, currentRow - 1)) {
            count++;
        }
        if (isBombAt(currentColumn - 1, currentRow)) {
            count++;
        }
        if (isBombAt(currentColumn + 1, currentRow)) {
            count++;
        }
        if (isBombAt(currentColumn - 1, currentRow + 1)) {
            count++;
        }
        if (isBombAt(currentColumn, currentRow + 1)) {
            count++;
        }
        if (isBombAt(currentColumn + 1, currentRow + 1)) {
            count++;
        }
        return count;
    }

    public void updateGrid(int oldColumn, int oldRow, int newColumn, int newRow) {
        grid[oldColumn][oldRow] = false;
        grid[newColumn][newRow] = true;
    }

    private boolean isBombAt(int column, int row) {
        return isValid(column, row) && grid[column][row];
    }

    private boolean isValid(int column, int row) {
        return column >= 0 && column < COLUMNS && row >= 0 && row < ROWS;
    }
}
