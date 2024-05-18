package com.shr4pnel.minesweeper;


import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    final GridWrapper grid = new GridWrapper();

    public Grid() {
        // todo fix beginner mode and intermediate!
        // sorry :3
        // 99 bombs in expert:
        generateBombs(99);
    }

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
