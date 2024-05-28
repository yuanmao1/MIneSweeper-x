package grassField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import Main.*;
public class GrassFieldPanel extends JPanel implements IGamePanel {
    JFrame frame;
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    //all info
    //minesUnclear List
    //--------------------------------------None,L1,L2,L3,L4,L5,L6,L7
    private final int[] minesUnclearList = {-100,-1, 0, 0, 0, 0, 0, 0};
    public int getMinesUnclear(final int indexLevel) {
        if (indexLevel < 1 || indexLevel > 7) {
            throw new IllegalArgumentException("index error1");
        }
        return minesUnclearList[indexLevel];
    }
    public void setMinesUnclear(final int indexLevel, final  int value) {
        if (indexLevel < 1 || indexLevel > 7) {
            throw new IllegalArgumentException("index error2");
        }
        minesUnclearList[indexLevel] = value;
    }
    private void minusMinesUnclear(final int indexLevel) {
        if (indexLevel < 1 || indexLevel > 7) {
            throw new IllegalArgumentException("index error3");
        }
        minesUnclearList[indexLevel]--;
    }
    private void initAllMinesUnclear() {
        int base = numberOfMines / 7;
        for (int i = 1; i <= 7; i++) {
            setMinesUnclear(i, base);
        }
        int remain = numberOfMines % 7;
        for (int i = 1; i <= remain; i++) {
            setMinesUnclear(i, base + 1);
        }
    }

    //numberOfMines
    private int numberOfMines = 0;

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
    private GrassCell[][] cellArray = null;
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
        setOpaque(false);
        if (rowTotal * colTotal < 16) {
            throw new IllegalArgumentException("The total number of cells cannot be less than 16.");
        }
        if (diffRate == null) {
            throw new IllegalArgumentException("Difficulty rate cannot be null.");
        }

        this.setPreferredSize(new Dimension(cellSize * colTotal + 300, cellSize * rowTotal + 300));

        setRowTotal(rowTotal);
        setColTotal(colTotal);

        initCellArray();

        setAllMine(diffRate);

        putAllNumberMark();

        findASafePlaceToPutThePlayer();

        initAllMinesUnclear();

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

                updateMinesUnclearAndPlayerHP();

                repaint();

            }
        });
        this.setFocusable(true);

        if (player != null) {
            revealAt(player.getRow(), player.getCol());
            if (numberOfMines / 7 - 1 > 1) {
                player.setLevelUpNeeded(numberOfMines / 7 - 1);
            }
            System.out.println("Player level up needed: " + player.getLevelUpNeeded());
        }

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
                            && (! current.isSearched())
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
            //绘制格子
            for (int i = 0; i < cellArray.length; i++) {
                for (int j = 0; j < cellArray[i].length; j++) {
                    cellArray[i][j].paintSelf(g, j * cellSize + 150, i * cellSize + 150, cellSize, cellSize);
                }
            }

            //绘制玩家
            if (player != null) {
                player.paintSelf(g, player.getCol() * cellSize + 150, player.getRow() * cellSize + 150);
            }
        } catch (NullPointerException e) {
            // ignore
            System.out.println("some ignored paint error");
        }

        //绘制其他信息
        g.setColor(Color.BLACK);
        String text = "<null>";
        text = "#PlayerHP:";
        if (player != null) {
            text +=  player.getHitPoints() + " ";
        } else {
            text += "null ";
        }
        text += "#PlayerLev:";
        if (player != null) {
            text +=  player.getLevel() + " ";
        } else {
            text += "null ";
        }
        final String[] numberToWord = {"ZER", "ONE", "TWO", "THR", "FOU", "FIV", "SIX", "SEV"};
        for (int i = 1; i <= 7; i++) {
            text += "#M-" + numberToWord[i] + ":" + getMinesUnclear(i) + " ";
        }
        text += "#LUN:" + player.getLevelUpNeeded();
        g.drawString(text, 50, 50);

    }

    public String getTitle() {
        return "Grass Field";
    }

    // set some cells as mines according to difficulty rate
    public void setAllMine(final Difficulty diff) {
        numberOfMines = (int) (diff.getMineProbability() * (rowTotal * colTotal));

        if (numberOfMines < 7) {
            numberOfMines = 7;
        }
        System.out.println("Number of mines: " + numberOfMines);
        int currentMineLevel = 1; //from m1 to m7
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
            if (!isCellAWall(cellArray[row][col])) {
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


    private static final int NONE_STATE = 0;
    private static final int IS_WINNER = 1;
    private static final int NOT_WINNER = 2;
    private int winnerState = NONE_STATE;
    public void gameOver() {
        JDialog dialog = new JDialog();
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new FlowLayout());
        JLabel label = new JLabel("Game over!");
        JButton okButton = new JButton("OK");
        dialog.add(label);
        dialog.add(okButton);
        okButton.addActionListener(e -> {
            dialog.dispose();
            frame.dispose();
            Main.frame.setVisible(true);
            Main.frame.toFront();
            Main.frame.requestFocus();
        });
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //need do something
        System.out.print("Game over!" );
        if (winnerState == IS_WINNER) {
            System.out.println("you win!");
        } else {
            System.out.println("you lose!");
        }
        //System.exit(0);
    }

    public void updateMinesUnclearAndPlayerHP() {
        GrassCell cell = cellArray[player.getRow()][player.getCol()]; //玩家所在格
        int level = cell.getMineLevel(); //所在格的雷的等级

        //扣血及死亡
        if (player.getHitPoints() > 0 && level > 0 && player.getLevel() < level) {
            player.decreaseHitPoints(); //有雷 玩家等级低于雷等级 玩家有血可扣， 扣血
        }
        if (player.getHitPoints() <= 0) {
            winnerState = NOT_WINNER;
            gameOver(); //玩家血量归零 结束游戏
        }

        //排雷及升级
        if (level > 0 && !cell.isFlag()) { //有雷 雷未排
            if (player.getLevel() >= level) { //玩家等级高于雷等级
                minusMinesUnclear(level); //剩余雷数更新
                cell.setIsFlag(true); //排雷
                if (player.getLevel() == level) { //玩家等级等于雷等级
                    player.updateWhenClearedOneMine(level); //玩家升级
                }
            }
        }

        //胜利条件
        if (getMinesUnclear(1) == 0
                && getMinesUnclear(2) == 0
                && getMinesUnclear(3) == 0
                && getMinesUnclear(4) == 0
                && getMinesUnclear(5) == 0
                && getMinesUnclear(6) == 0
                && getMinesUnclear(7) == 0) {
            winnerState = IS_WINNER;
            gameOver(); //所有雷都被清除 结束游戏
        }
    }
}
