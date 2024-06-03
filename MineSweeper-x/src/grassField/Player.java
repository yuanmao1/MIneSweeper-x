package grassField;

import javax.swing.*;
import java.awt.*;

public class Player {
    //constructor...
    public Player(final GrassFieldPanel gamePanel, final CellMatrix cellMatrix, final int hp) {
        if (gamePanel == null) {
            throw new IllegalArgumentException("Game panel cannot be null.");
        }
        this.gamePanel = gamePanel;
        if (cellMatrix == null) {
            throw new IllegalArgumentException("Cell matrix cannot be null.");
        }
        this.cellMatrix = cellMatrix;
        setHitPoints(hp);
    }

    private final CellMatrix cellMatrix;
    //in which gamePanel
    private final GrassFieldPanel gamePanel;

    //hit points
    private int hitPoints;
    public int getHitPoints() {
        return hitPoints;
    }
    public void setHitPoints(final int hitPoints) {
        if (hitPoints < 0) {
            throw new IllegalArgumentException("Hit points cannot be negative.");
        }
        this.hitPoints = hitPoints;
    }
    public void decreaseHitPoints() {
        hitPoints = (hitPoints > 0) ? (hitPoints - 1) : 0;
        if (hitPoints == 0 && gamePanel!= null) {
            gamePanel.gameOver();
        }
    }

    //level
    private int maxXP = 1;
    public int getMaxXP() {
        return maxXP;
    }
    public void setMaxXP(final int maxXP) {
        this.maxXP = maxXP;
    }

    // level
    private int currXP = 0;
    private int level = 1;
    public int getLevel() {
        return level;
    }
    private void upLevel() {
        if (level <= 7) level++;
    }
    public void updateWhenClearedOneMine(final int mineLevelCleared) {
        if (mineLevelCleared > level) {
            return;
        } //
        currXP++;
        if (currXP >= maxXP) {
            currXP = 0;
            upLevel();
        }
    }






    //position on the board (row and column)
    public static final int NOT_A_ROW = -1002;
    private int row = NOT_A_ROW;
    public int getRow() {
        return row;
    }
    public void setRow(final int row) {
        if (gamePanel == null) {
            throw new IllegalStateException("Game panel must be set before row can be set.");
        }
        if (row < 0 || row >= cellMatrix.getRowTotal()) {
            throw new IllegalArgumentException("grassField.Player must be on a valid row.");
        }
        this.row = row;
    }
    public static final int NOT_A_COL = -1003;
    private int col = NOT_A_COL;
    public int getCol() {
        return col;
    }
    public void setCol(final int col) {
        if (gamePanel == null) {
            throw new IllegalStateException("Game panel must be set before column can be set.");
        }
        if (col < 0 || col >= cellMatrix.getColTotal()) {
            throw new IllegalArgumentException("grassField.Player must be on a valid column.");
        }
        this.col = col;
    }



    //paint self
    public void paintSelf(Graphics g, int x, int y) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }

        g.drawImage(imageIcon.getImage(), x, y, null);

        System.out.println("paint player" + " at " + x + ", " + y + "with row and col:" + row + ", " + col);
    }

    //image icon, will change when moving
    private ImageIcon imageIcon = ImageHolder.frontMan;

    //move the player
    public void downMove() {
        if (gamePanel == null) {
            throw new IllegalStateException("Game panel must be set before row can be set.");
        }
        if (row < cellMatrix.getRowTotal() -1) {
            row++;
        }
        imageIcon = ImageHolder.frontMan;
    }
    public void upMove() {
        if (row > 0) {
            row--;
        }
        imageIcon = ImageHolder.backMan;
    }
    public void rightMove() {
        if (gamePanel == null) {
            throw new IllegalStateException("Game panel must be set before column can be set.");
        }
        if (col < cellMatrix.getColTotal() - 1) {
            col++;
        }
        imageIcon = ImageHolder.rightMan;
    }
    public void leftMove() {
        if (col > 0) {
            col--;
        }
        imageIcon = ImageHolder.leftMan;
    }

}
