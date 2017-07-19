package paul.sdkslvr;

import static paul.sdkslvr.MainActivity.sdkGrid;


class SdkSolver {

    private SdkCopy sdkCopy;

    SdkSolver() {
        init();
    }

    private void init() {
        sdkCopy = new SdkCopy();
    }

    int sdkSolve() {
        int returnCode;
        refreshGridProb();
        while (fillObviousCell() == 1);
        sdkGrid.getCell(0,0);
        if(isDone() == 0) {
            returnCode = sdkCopy.sdkAttempt();
        }
        else {
            returnCode = 1;
        }
        sdkGrid.getCell(0,0);
        return returnCode;
    }

    // REMOVE FROM GRID
    // TO BE USED WHEN INITIALIZING GRID
    private void checkFromGrid(int column, int row){
        if(sdkGrid.getCell(column, row) == 0) {
            checkCellFromColumn(column, row);
            checkCellFromRow(column, row);
            checkCellFromBlock(column, row);
        }
    }

    private void checkCellFromColumn(int column, int row) {
        for (int cRow = 0; cRow < 9; cRow++) {
            if ((cRow != row) && (sdkGrid.getCell(column, cRow) != 0)) {
                sdkGrid.removeProb(column, row, sdkGrid.getCell(column, cRow));
            }
        }
    }

    private void checkCellFromRow(int column, int row) {
        for (int cColumn = 0; cColumn < 9; cColumn++) {
            if ((cColumn != column) && (sdkGrid.getCell(cColumn, row) != 0)) {
                sdkGrid.removeProb(column, row, sdkGrid.getCell(cColumn, row));
            }
        }
    }

    private void checkCellFromBlock(int column, int row) {
        int blockColumn = getBlockNumber(column);
        int blockRow = getBlockNumber(row);

        for (int cColumn = blockColumn * 3; cColumn < (blockColumn * 3 + 3); cColumn++) {
            for (int cRow = blockRow * 3; cRow < (blockRow * 3 + 3); cRow++) {
                if (cColumn != column && cRow != row && (sdkGrid.getCell(cColumn, cRow) != 0)) {
                    sdkGrid.removeProb(column, row, sdkGrid.getCell(cColumn, cRow));
                }
            }
        }
    }

    // REMOVE FROM CELL
    // TO BE USED AFTER SETTING A CELL
    private void checkFromCell(int column, int row) {
        if(sdkGrid.getCell(column, row) != 0) {
            checkColumnFromCell(column, row);
            checkRowFromCell(column, row);
            checkBlockFromCell(column, row);
        }
    }

    private void checkColumnFromCell(int column, int row) {
        int value = sdkGrid.getCell(column, row);
        for (int r = 0; r < 9; r++) {
            if (r != row) {
                sdkGrid.removeProb(column, r, value);
            }
        }
    }

    private void checkRowFromCell(int column, int row) {
        int value = sdkGrid.getCell(column, row);
        for (int c = 0; c < 9; c++) {
            if (c != column) {
                sdkGrid.removeProb(c, row, value);
            }
        }
    }

    private void checkBlockFromCell(int column, int row) {
        int value = sdkGrid.getCell(column, row);
        int blockColumn = getBlockNumber(column);
        int blockRow = getBlockNumber(row);

        for (int c = blockColumn * 3; c < (blockColumn * 3 + 3); c++) {
            for (int r = blockRow * 3; r < (blockRow * 3 + 3); r++) {
                if (c != column && r != row && (sdkGrid.getCell(c, r) != 0)) {
                    sdkGrid.removeProb(c, r, value);
                }
            }
        }
    }

    private int fillObviousCell() {
        int cellFilled = 0;
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                if (sdkGrid.getProb(col, row, 0) == 1) {
                    int value = 1;
                    while (sdkGrid.getProb(col, row, value) == 0) {
                        value++;
                    }
                    sdkGrid.setCell(col, row, value);
                    checkFromCell(col, row);

                    col = 9;
                    row = 9;
                    cellFilled = 1;
                }
            }
        }
        return cellFilled;
    }

    private void refreshGridProb() {
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                checkFromGrid(column, row);
            }
        }
    }

    public int isDone() {
        int done = 0;
        int filledCell = 0;
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                if (sdkGrid.getCell(column, row) != 0) {
                    filledCell++;
                }
            }
        }
        if (filledCell == 9 * 9) {
            done = 1;
        }
        return done;
    }

    private int getBlockNumber(int index) {
        int result;
        if (index < 3) {
            result = 0;
        } else if (index < 6) {
            result = 1;
        } else {
            result = 2;
        }
        return result;
    }
}
