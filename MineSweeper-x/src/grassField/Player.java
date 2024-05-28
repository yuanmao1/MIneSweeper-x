package grassField;

import javax.swing.*;
import java.awt.*;

public class Player {

    //level
    private int levelUpNeeded = 1;
    public int getLevelUpNeeded() {
        return levelUpNeeded;
    }
    public void setLevelUpNeeded(final int levelUpNeeded) {
        this.levelUpNeeded = levelUpNeeded;
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
        if (currXP >= levelUpNeeded) {
            currXP = 0;
            upLevel();
        }
    }

    // in which gamePanel
    private GrassFieldPanel gamePanel = null;
    private GrassFieldPanel getGamePanel() {
        return gamePanel;
    }
    private void setGamePanel(final GrassFieldPanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    //hit points
    public static final int NOT_A_HP = -1001;
    private int hitPoints = NOT_A_HP;
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
        if (hitPoints == NOT_A_HP) {
            throw new IllegalStateException("Hit points must be set before decreasing.");
        }
        if (hitPoints > 0) {
            hitPoints--;
        }
        if (hitPoints == 0 && gamePanel!= null) {
            gamePanel.gameOver();
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
        if (row < 0 || row >= gamePanel.getRowTotal()) {
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
        if (col < 0 || col >= gamePanel.getColTotal()) {
            throw new IllegalArgumentException("grassField.Player must be on a valid column.");
        }
        this.col = col;
    }

    //constructor
    public Player(final GrassFieldPanel gamePanel, final int hp) {
        setGamePanel(gamePanel);
        setHitPoints(hp);
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
        if (row < gamePanel.getRowTotal() -1) {
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
        if (col < gamePanel.getColTotal() - 1) {
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
