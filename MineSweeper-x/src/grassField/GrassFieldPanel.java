package grassField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import Main.*;
public class GrassFieldPanel extends JPanel implements IGamePanel {
    // constructor
    public GrassFieldPanel(final int rowTotal, final int colTotal, final int numberOfMines) {

        this.setOpaque(false);

        this.setPreferredSize(new Dimension(cellSize * colTotal + 300, cellSize * rowTotal + 300));


        cellMatrix = new CellMatrix(rowTotal, colTotal);

        cellMatrix.setAllMine(numberOfMines);

        cellMatrix.putAllNumberMark();

        this.findASafePlaceToPutThePlayer();

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
                player.setMaxXP(numberOfMines / 7 - 1);
            }
            System.out.println("Player level up needed: " + player.getMaxXP());
        }

    } //end of constructor

    private final CellMatrix cellMatrix;
    private Player player;
    private JFrame frame;
    public void setFrame(JFrame frame) {
        if (frame == null) {
            throw new IllegalArgumentException("Frame cannot be null.");
        }
        this.frame = frame;
    }
    private final int cellSize = 25;


    //reveal at (r,c)
    private void revealAt(int rowA, int colA) {
        //System.out.println("rowA" + rowA + "colA" + colA);
        //queue
        Queue<GrassCell> queue = new LinkedList<>();
        queue.add(cellMatrix.getCell(rowA, colA));

        for (int row = 0; row < cellMatrix.getRowTotal(); row++) {
            for (int col = 0; col < cellMatrix.getColTotal(); col++) {
                cellMatrix.getCell(row, col).setSearched(false);
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
                    if (row + i >= 0 && row + i < cellMatrix.getRowTotal()
                            && col + j >= 0 && col + j < cellMatrix.getColTotal()
                            && !(i == 0 && j == 0)
                            && (! current.isSearched())
                    ) {
                        queue.add(cellMatrix.getCell(row + i, col + j));
                    }
                }
            }

            current.setSearched(true);
        }

    } // end

    // paint the cells and then the player
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            //绘制格子
            for (int i = 0; i < cellMatrix.getRowTotal(); i++) {
                for (int j = 0; j < cellMatrix.getColTotal(); j++) {
                    cellMatrix.getCell(i, j).paintSelf(g, j * cellSize + 150, i * cellSize + 150, cellSize, cellSize);
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
            text += "#M-" + numberToWord[i] + ":" + cellMatrix.getMinesUnclear(i) + " ";
        }
        text += "#LUN:" + player.getMaxXP();
        g.drawString(text, 50, 50);

    }

    public String getTitle() {
        return "Grass Field";
    }

    // find a safe place (not a mine) to put the player
    public void findASafePlaceToPutThePlayer() {
        boolean notFound = true;
        while (notFound) {
            int row = (int) (Math.random() * cellMatrix.getRowTotal());
            int col = (int) (Math.random() * cellMatrix.getColTotal());
            if (!isCellAWall(cellMatrix.getCell(row, col))) {
                player = new Player(this, cellMatrix, 5);
                player.setRow(row);
                player.setCol(col);
                notFound = false;
            }
        }
    }

    private boolean isCellAWall(final GrassCell cell) {
        return cell.getMineLevel() > 0 || cell.getNumberMark() > 0;
    }

    private WinnerStatus winnerStatus = WinnerStatus.NONE_STATUS;
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
        if (winnerStatus == WinnerStatus.IS_WINNER) {
            label.setText("Game over! You win!");
        } else {
            label.setText("Game over! You lose!");
        }
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //need do something
        System.out.print("Game over!" );
        if (winnerStatus == WinnerStatus.IS_WINNER) {
            System.out.println("you win!");
        } else {
            System.out.println("you lose!");
        }
        //System.exit(0);
    }

    public void updateMinesUnclearAndPlayerHP() {
        GrassCell cell = cellMatrix.getCell(player.getRow(), player.getCol()); //玩家所在格
        int level = cell.getMineLevel(); //所在格的雷的等级

        //扣血及死亡
        if (player.getHitPoints() > 0 && level > 0 && player.getLevel() < level) {
            player.decreaseHitPoints(); //有雷 玩家等级低于雷等级 玩家有血可扣， 扣血
        }
        if (player.getHitPoints() <= 0) {
            winnerStatus = WinnerStatus.NOT_WINNER;
            gameOver(); //玩家血量归零 结束游戏
        }

        //排雷及升级
        if (level > 0 && !cell.isFlag()) { //有雷 雷未排
            if (player.getLevel() >= level) { //玩家等级高于雷等级
                cellMatrix.decreaseMinesUnclear(level); //剩余雷数更新
                cell.setIsFlag(true); //排雷
                if (player.getLevel() == level) { //玩家等级等于雷等级
                    player.updateWhenClearedOneMine(level); //玩家升级
                }
            }
        }

        //胜利条件
        if (cellMatrix.isAllMinesCleared()) {
            winnerStatus = WinnerStatus.IS_WINNER;
            gameOver(); //所有雷都被清除 结束游戏
        }
    }
}
