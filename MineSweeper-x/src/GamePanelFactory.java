import javax.swing.*;
import java.awt.*;

public class GamePanelFactory {
    //test code
    public static void main(String[] args) {
         JFrame frame = new JFrame();
         switchToGrassField(frame, 10, 10);
    }

    public static void switchToGrassField(JFrame frame, int rows, int cols) {
        GrassFieldPanel grassFieldPanel = new GrassFieldPanel(rows, cols, Difficulty.getEasy());
        ComponentAssembler.assemble(frame, grassFieldPanel);
    }
}

class config {
    public static int cellSize = 25;
}

class GrassFieldPanel extends JPanel implements IGamePanel {
    private GrassCell grassCellArray[][] = null;
    private int rowCount = 0;
    private int colCount = 0;

    private Player player;

    public GrassFieldPanel(int rows, int cols, Difficulty diff) {
        grassCellArray = new GrassCell[rows][cols];
        this.rowCount = rows;
        this.colCount = cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grassCellArray[i][j] = new GrassCell(0);
            }
        }

        this.setPreferredSize(new Dimension(config.cellSize * cols, config.cellSize * rows));

        if (diff == null) {
            throw new IllegalArgumentException("Difficulty cannot be null.");
        }
        setAllMine(diff);

        findASafePlaceToPutThePlayer();
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            for (int i = 0; i < grassCellArray.length; i++) {
                for (int j = 0; j < grassCellArray[i].length; j++) {
                    grassCellArray[i][j].paintSelf(g, i * config.cellSize, j * config.cellSize, config.cellSize, config.cellSize);
                }
            }


            if (player!= null) {
                player.paintSelf(g, player.getCol() * config.cellSize, player.getRow() * config.cellSize);
            }
        } catch (NullPointerException e) {
            // ignore
        }
    }

    public String getTitle() {
        return "Grass Field";
    }

    public void setAllMine(Difficulty diff) {
        int numberOfMines = (int) (diff.getMineProbability() * (rowCount * colCount));
        int currentMineLevel = 1;
        for (int i = 0; i < numberOfMines; i++) {
            int row = (int) (Math.random() * rowCount);
            int col = (int) (Math.random() * colCount);
            if (grassCellArray[row][col].getMineLevel() == 0) {
                grassCellArray[row][col].setMineLevel(currentMineLevel);
                currentMineLevel++;
                if (currentMineLevel > 7) {
                    currentMineLevel = 1;
                }
            } else {
                i--; // try again
            }
        }
    }

    public void findASafePlaceToPutThePlayer() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                if (grassCellArray[i][j].getMineLevel() == 0) {
                    player = new Player(rowCount, colCount);
                    player.setRow(i);
                    player.setCol(j);
                }
            }
        }
    }


}

class Player {
    private int row = -1;
    public int getRow() {
        return row;
    }
    public void setRow(final int row) {
        this.row = row;
    }
    private int col = -1;
    public int getCol() {
        return col;
    }
    public void setCol(final int col) {
        this.col = col;
    }

    private int rowMax;
    private int colMax;
    private void setMax(final int rowMax, final int colMax) {
        if (rowMax <= 0 || colMax <= 0) {
            throw new IllegalArgumentException("Row and column max must be greater than 0.");
        }
        this.rowMax = rowMax;
        this.colMax = colMax;
    }
    public Player(final int rowMax, final int colMax) {
        setMax(rowMax, colMax);
    }

    private ImageIcon imageIcon = ImageHolder.frontMan;

    public void paintSelf(Graphics g, int x, int y) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }

        g.drawImage(imageIcon.getImage(), x, y, null);

        System.out.println("paintSelf: " + " at " + x + ", " + y);
    }

    public void downMove() {
        if (row < rowMax - 1) {
            row++;
        }
        imageIcon = ImageHolder.frontMan;
    }

    public void upMove() {
        if (row > 0) {
            row--;
        }
        imageIcon = ImageHolder.frontMan;
    }

    public void leftMove() {
        if (col > 0) {
            col--;
        }
        imageIcon = ImageHolder.frontMan;
    }

    public void rightMove() {
        if (col < colMax - 1) {
            col++;
        }
        imageIcon = ImageHolder.frontMan;
    }


}

class Difficulty {
    private double mineProbability = 0;
    public double getMineProbability() {
        return mineProbability;
    }
    public void setMineProbability(final double mineProbability) {
        if (mineProbability < 0 || mineProbability > 0.90) {
            throw new IllegalArgumentException("Mine probability must be between 0 and 0.90.");
        }
        this.mineProbability = mineProbability;
    }

    public Difficulty(double mineProbability) {
        this.setMineProbability(mineProbability);
    }

    public static Difficulty getEasy() {
        return new Difficulty(0.12);
    }
    public static Difficulty getMedium() {
        return new Difficulty(0.15);
    }
    public static Difficulty getHard() {
        return new Difficulty(0.17);
    }

}

class GrassCell {
    private int mineLevel = -1;
    public int getMineLevel() {
        return mineLevel;
    }
    public void setMineLevel(final int mineLevel) {
        if (isMineLevelInvalid(mineLevel)) {
            throw new IllegalArgumentException("Mine level must be between 0 and 7.");
        }
        this.mineLevel = mineLevel;
    }
    private boolean isMineLevelInvalid(final int mineLevel) {
        return mineLevel < 0 || mineLevel > 7;
    }

    private int numberMark = -1;
    public int getNumberMark() {
        return numberMark;
    }
    public void setNumberMark(final int numberMark) {
        if (isNumberMarkInvalid(numberMark)) {
            throw new IllegalArgumentException("Number mark must be between 0 and 56.");
        }
        this.numberMark = numberMark;
    }
    private boolean isNumberMarkInvalid(final int numberMark) {
        return numberMark < 0 || numberMark > 56;
    }

    private ImageIcon imageIcon = ImageHolder.blank;

    public GrassCell(final int mineLevel) {
        this.setMineLevel(mineLevel);
    }

    public void paintSelf(Graphics g, int x, int y, int width, int height) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }

        g.drawImage(imageIcon.getImage(), x, y, null);

        String text = "";
        if (numberMark >= 0) {
            text = "<" + numberMark + ">";
        }
        if (mineLevel > 0) { //is mine
            text = "M" + mineLevel;
        }

        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(text, x + width / 2 - g.getFontMetrics().stringWidth(text) / 2,
                y + height / 2 + g.getFontMetrics().getAscent() / 2);

        System.out.println("paintSelf: " + text + " at " + x + ", " + y + " with size " + width + "x" + height);
    }
}

