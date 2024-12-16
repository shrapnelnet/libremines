package com.shr4pnel.minesweeper;

public class GridWrapper {
    /**
     * Number of columns.
     */
    private static final int COLUMNS = 30;
    /**
     * Number of rows.
     */
    private static final int ROWS = 16;
    /**
     * A low level 2D array representing the position of bombs.
     */
    final boolean[][] grid = new boolean[COLUMNS][ROWS];
    /**
     * Points at a column in grid.
     */
    private int currentColumn;
    /**
     * Points at a row in grid.
     */
    private int currentRow;

    /**
     * On instantiation, initialize currentColumn and currentRow to 0
     */
    public GridWrapper() {
        this.currentColumn = 0;
        this.currentRow = 0;
    }

    /**
     * Sets currentColumn to a specified column.
     *
     * @param column The column specified.
     * @return Current GridWrapper instance.
     */
    public GridWrapper atColumn(int column) {
        this.currentColumn = column;
        return this;
    }

    /**
     * Sets currentColumn to a specified column.
     *
     * @param row The row specified.
     * @return Current GridWrapper instance.
     */
    public GridWrapper atRow(int row) {
        this.currentRow = row;
        return this;
    }

    /**
     * Sets a bomb in grid at the position specified by currentColumn and currentRow.
     */
    public void setBomb() {
        if (isValid(currentColumn, currentRow)) {
            grid[currentColumn][currentRow] = true;
        }
    }

    /**
     * Switches a bomb from one position to another.
     *
     * @param destinationColumn The new column for the bomb.
     * @param destinationRow    The new row for the bomb.
     */
    public void switchBomb(int destinationColumn, int destinationRow) {
        if (isValid(currentColumn, currentRow) && isValid(destinationColumn, destinationRow)) {
            grid[destinationColumn][destinationRow] = true;
            grid[currentColumn][currentRow] = false;
        }
    }

    /**
     * Checks if there is a bomb at the specified position.
     *
     * @return The value in grid specified by currentColumn and currentRow.
     */
    public boolean isBomb() {
        return isValid(currentColumn, currentRow) && grid[currentColumn][currentRow];
    }

    /**
     * Checks for bombs in every direction from the currentColumn and currentRow.
     *
     * @return The number of adjacent bombs.
     */
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

    // todo use switchBomb

    /**
     * Swaps one bomb with another.
     *
     * @param oldColumn The original column of the bomb.
     * @param oldRow    The original row of the bomb.
     * @param newColumn The destination column of the bomb.
     * @param newRow    The destination row of the bomb.
     */
    public void updateGrid(int oldColumn, int oldRow, int newColumn, int newRow) {
        grid[oldColumn][oldRow] = false;
        grid[newColumn][newRow] = true;
    }

    /**
     * Checks if a bomb is at a specified position.
     *
     * @param column The column specified.
     * @param row    The row specified.
     * @return A boolean AND representing a valid position and a bomb being present.
     * @see #isValid(int, int)
     */
    private boolean isBombAt(int column, int row) {
        return isValid(column, row) && grid[column][row];
    }

    /**
     * Checks if a given column and row is in bounds.
     *
     * @param column The column provided.
     * @param row    The row provided.
     * @return A boolean representing the tile being in bounds.
     */
    private boolean isValid(int column, int row) {
        return column >= 0 && column < COLUMNS && row >= 0 && row < ROWS;
    }
}
