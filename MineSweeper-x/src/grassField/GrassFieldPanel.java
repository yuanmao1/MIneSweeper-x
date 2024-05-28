package grassField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class GrassFieldPanel extends JPanel implements IGamePanel {
    // cell size when painting
    private int cellSize = 25;
    public int getCellSize() {
        return cellSize;
    }
    public void setCellSize(final int cellSize) {
        if (cellSize < 10) {
            throw new IllegalArgumentException("Cell size cannot be less than 10.");
        }
        this.cellSize = cellSize;
    }

    // cell array
    private GrassCell cellArray[][] = null;
    private void initCellArray() {
        cellArray = new GrassCell[rowTotal][colTotal];
        for (int i = 0; i < rowTotal; i++) {
            for (int j = 0; j < colTotal; j++) {
                cellArray[i][j] = new GrassCell();
                cellArray[i][j].setRow(i);
                cellArray[i][j].setCol(j);
            }
        }

    }

    // row and col total
    private int rowTotal = 1;
    public int getRowTotal() {
        return rowTotal;
    }
    private void setRowTotal(final int rowTotal) {
        if (rowTotal < 1) {
            throw new IllegalArgumentException("Row total cannot be less than 1.");
        }
        this.rowTotal = rowTotal;
    }
    private int colTotal = 1;
    public int getColTotal() {
        return colTotal;
    }
    private void setColTotal(final int colTotal) {
        if (colTotal < 1) {
            throw new IllegalArgumentException("Column total cannot be less than 1.");
        }
        this.colTotal = colTotal;
    }

    private Player player;

    // constructor
    public GrassFieldPanel(final int rowTotal, final int colTotal, final Difficulty diffRate) {
        this.setPreferredSize(new Dimension(cellSize * colTotal + 300, cellSize * rowTotal + 300));

        setRowTotal(rowTotal);
        setColTotal(colTotal);

        initCellArray();

        setAllMine(diffRate);

        putAllNumberMark();

        findASafePlaceToPutThePlayer();

        //start game
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_A) {
                    if (player != null) {
                        player.leftMove();
                    }
                } else if (keyCode == KeyEvent.VK_D) {
                    if (player != null) {
                        player.rightMove();
                    }
                } else if (keyCode == KeyEvent.VK_W) {
                    if (player != null) {
                        player.upMove();
                    }
                } else if (keyCode == KeyEvent.VK_S) {
                    if (player != null) {
                        player.downMove();
                    }
                }

                /*Thread thread = new Thread(() -> {
                    revealAt(player.getRow(), player.getCol());
                    repaint();
                });
                thread.start();*/
                if (player != null) {
                    revealAt(player.getRow(), player.getCol());
                }
                repaint();

            }
        });
        this.setFocusable(true);

    } //end of constructor

    //reveal at (r,c)
    private void revealAt(int rowA, int colA) {
        System.out.println("rowA" + rowA + "colA" + colA);
        //queue
        Queue<GrassCell> queue = new LinkedList<>();
        queue.add(cellArray[rowA][colA]);

        for (int row = 0; row < rowTotal; row++) {
            for (int col = 0; col < colTotal; col++) {
                cellArray[row][col].setSearched(false);
            }
        }

        while (!queue.isEmpty()) {
            GrassCell current = queue.poll();

            current.setCovered(false);

            if (isCellAWall(current)) {
                continue;
            }

            int row = current.getRow();
            int col = current.getCol();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (row + i >= 0 && row + i < rowTotal
                            && col + j >= 0 && col + j < colTotal
                            && !(i == 0 && j == 0)
                            && current.isSearched() == false
                    ) {
                        queue.add(cellArray[row + i][col + j]);
                    }
                }
            }

            current.setSearched(true);
        }

    } // end

    private boolean isCellAWall(final GrassCell cell) {
        return cell.getMineLevel() > 0 || cell.getNumberMark() > 0;
    }

    // paint the cells and then the player
    public void paint(Graphics g) {
        super.paint(g);
        try {
            for (int i = 0; i < cellArray.length; i++) {
                for (int j = 0; j < cellArray[i].length; j++) {
                    cellArray[i][j].paintSelf(g, j * cellSize + 150, i * cellSize + 150, cellSize, cellSize);
                }
            }

            if (player != null) {
                player.paintSelf(g, player.getCol() * cellSize + 150, player.getRow() * cellSize + 150);
            }
        } catch (NullPointerException e) {
            // ignore
        }
    }

    public String getTitle() {
        return "Grass Field";
    }

    // set some cells as mines according to difficulty rate
    public void setAllMine(final Difficulty diff) {
        int numberOfMines = (int) (diff.getMineProbability() * (rowTotal * colTotal));
        int currentMineLevel = 1;
        for (int i = 0; i < numberOfMines; i++) {
            int row = (int) (Math.random() * rowTotal);
            int col = (int) (Math.random() * colTotal);
            if (cellArray[row][col].getMineLevel() == 0) {
                cellArray[row][col].setMineLevel(currentMineLevel);
                currentMineLevel++;
                if (currentMineLevel > 7) {
                    currentMineLevel = 1;
                }
            } else {
                i--; // try again
            }
        }
    }

    // find a safe place (not a mine) to put the player
    public void findASafePlaceToPutThePlayer() {
        boolean notFound = true;
        while (notFound) {
            int row = (int) (Math.random() * rowTotal);
            int col = (int) (Math.random() * colTotal);
            if (cellArray[row][col].getMineLevel() == 0) {
                player = new Player(this, 5);
                player.setRow(row);
                player.setCol(col);
                notFound = false;
            }
        }
    }

    //put number mark on the board
    private void putAllNumberMark() {
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

    public void gameOver() {
        //need do something
    }

}
