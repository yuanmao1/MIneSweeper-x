package grassField;

public class CellMatrix {
    public CellMatrix(int rowTotal, int colTotal) {
        if (rowTotal < 4 || colTotal < 4) {
            throw new IllegalArgumentException("Row total and column total must be at least 4.");
        }
        this.rowTotal = rowTotal;
        this.colTotal = colTotal;
        cellArray = new GrassCell[rowTotal][colTotal];
        for (int i = 0; i < rowTotal; i++) {
            for (int j = 0; j < colTotal; j++) {
                cellArray[i][j] = new GrassCell();
                cellArray[i][j].setRow(i);
                cellArray[i][j].setCol(j);
            }
        }
    }

    private final GrassCell[][] cellArray;
    public GrassCell getCell(int row, int col) {
        if (row < 0 || row >= rowTotal || col < 0 || col >= colTotal) {
            return null;
        }
        return cellArray[row][col];
    }

    private final int rowTotal;
    public int getRowTotal() {
        return rowTotal;
    }

    private final int colTotal;
    public int getColTotal() {
        return colTotal;
    }

    //minesUnclear List
    //------------------------------------None,L1,L2,L3,L4,L5,L6,L7
    //minesUnclearList[0]用于表示该数组是否初始化，若为-1则未初始化，若为0则已初始化
    private final int[] minesUnclearList = {-1, 0, 0, 0, 0, 0, 0, 0};
    private void checkIndexLevel(final int indexLevel) {
        if (indexLevel < 1 || indexLevel > 7) {
            throw new IllegalArgumentException("indexLevel must be between 1 and 7.");
        }
    }
    public boolean isAllMinesCleared() {
        if (minesUnclearList[0] == -1) {
            return false;
        }
        for (int i = 1; i <= 7; i++) {
            if (minesUnclearList[i] > 0) {
                return false;
            }
        }
        return true;
    }
    public int getMinesUnclear(final int indexLevel) {
        checkIndexLevel(indexLevel);
        return minesUnclearList[indexLevel];
    }
    public void setMinesUnclear(final int indexLevel, final int value) {
        checkIndexLevel(indexLevel);
        minesUnclearList[indexLevel] = value;
    }
    public void decreaseMinesUnclear(final int indexLevel) {
        checkIndexLevel(indexLevel);
        minesUnclearList[indexLevel]--;
    }

    // set some cells to be mines
    public void setAllMine(int numberOfMines) {
        if (numberOfMines > rowTotal * colTotal / 3) {
            throw new IllegalArgumentException("Number of mines must be less than or equal to 1/3 of the total cells.");
        }
        if (numberOfMines < 7) {
            throw new IllegalArgumentException("Number of mines must be at least 7.");
        }

        int currentMineLevel = 1; //from m1 to m7
        for (int i = 0; i < numberOfMines; i++) {
            int row = (int) (Math.random() * rowTotal);
            int col = (int) (Math.random() * colTotal);
            if (cellArray[row][col].getMineLevel() == 0) {
                cellArray[row][col].setMineLevel(currentMineLevel);
                minesUnclearList[currentMineLevel]++;
                currentMineLevel = (currentMineLevel < 7) ? (currentMineLevel + 1) : 1;
            } else {
                i--; // try again
            }
        }

        minesUnclearList[0] = 0; //initialized
    }

    //put all number mark on the cells
    public void putAllNumberMark() {
        for (int row = 0; row < rowTotal; row++) {
            for (int col = 0; col < colTotal; col++) {
                int count = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (row + i >= 0 && row + i < rowTotal
                                && col + j >= 0 && col + j < colTotal
                                && !(i == 0 && j == 0)) {
                            if (cellArray[row + i][col + j].getMineLevel() >= 1) {
                                count += cellArray[row + i][col + j].getMineLevel();
                            }
                        }
                    }
                }

                cellArray[row][col].setNumberMark(count);
            }
        }
    }
}
