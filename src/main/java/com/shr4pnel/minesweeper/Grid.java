package com.shr4pnel.minesweeper;

import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    final GridWrapper grid = new GridWrapper();

    /**
     * Generates the bomb-grid structure on instantiation
     *
     * @see #generateBombs(int)
     */
    public Grid() {
        generateBombs(99);
    }

    /**
     * Generates a boolean 2D array by randomly selecting a column and row, and setting it to true to represent a bomb.
     *
     * @param bombMax The number of bombs to generate.
     */
    private void generateBombs(int bombMax) {
        int i;
        boolean success;
        for (i = 0; i < bombMax; ++i) {
            success = false;
            while (!success) {
                int column = ThreadLocalRandom.current().nextInt(30);
                int row = ThreadLocalRandom.current().nextInt(16);
                if (grid.atColumn(column).atRow(row).isBomb()) {
                    continue;
                }
                grid.atColumn(column).atRow(row).setBomb();
                success = true;
            }
        }
    }
}
