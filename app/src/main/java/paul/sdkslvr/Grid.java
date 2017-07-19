package paul.sdkslvr;

class Grid {

    private Cell[][] cells;

    Grid() {
        init();
    }

    Grid(Grid clone){
        init();
        for(int c = 0; c < 9; c++){
            for(int r = 0; r < 9; r++){
                this.cells[c][r].setValue(clone.getCell(c,r));
                for(int i = 1; i <= 9; i++){
                    this.removeProb(c,r,(clone.getProb(c,r,i) == 0 ? i : 0));
                }
            }
        }
    }

    private void init() {
        cells = new Cell[9][9];
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                cells[c][r] = new Cell();
            }
        }
    }

    void reset() {
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                cells[c][r].reset();
            }
        }
    }

    void setCell(int column, int row, int value) {
        cells[column][row].setValue(value);
    }

    void setGrid(int newCells[][]) {
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                cells[column][row].setValue(newCells[column][row]);
            }
        }
    }

    int getCell(int column, int row) {
        return cells[column][row].getValue();
    }

    int[][] getGrid() {
        int[][] cellsValue = new int[9][9];
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                cellsValue[c][r] = getCell(c, r);
            }
        }
        return cellsValue;
    }

    void removeProb(int column, int row, int value) {
        cells[column][row].removePossibleValue(value);
    }

    int getProb(int column, int row, int index) {
        return cells[column][row].getProb(index);
    }

}
