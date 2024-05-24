import javax.swing.*;
import java.awt.*;

public class GrassFieldFactory {
    public static void main(String[] args) {
         JFrame frame = new JFrame();
         switchToGrassField(frame, 10, 10);
    }

    public static void switchToGrassField(JFrame frame, int rows, int cols) {
        if (frame == null) {
            throw new IllegalArgumentException("Frame object cannot be null.");
        }
        frame.getContentPane().removeAll();
        frame.setTitle("Grass Field");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, config.cellSize * cols, config.cellSize * rows);
        frame.setLayout(null);

        frame.setContentPane(new InnerJPanel(rows, cols));

        frame.pack();
        frame.setVisible(true);
    }
}

class config {
    public static int cellSize = 40;
}

class InnerJPanel extends JPanel {
    private Cell cellArray[][] = null;

    public InnerJPanel(int rows, int cols) {
        cellArray = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellArray[i][j] = new Cell(0);
            }
        }

        this.setPreferredSize(new Dimension(config.cellSize * cols, config.cellSize * rows));
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            for (int i = 0; i < cellArray.length; i++) {
                for (int j = 0; j < cellArray[i].length; j++) {
                    cellArray[i][j].paintSelf(g, i * config.cellSize, j * config.cellSize, config.cellSize, config.cellSize);
                }
            }
        } catch (NullPointerException e) {
            // ignore
        }
    }
}

class Cell {
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

    public Cell(final int mineLevel) {
        this.setMineLevel(mineLevel);
    }

    public void paintSelf(Graphics g, int x, int y, int width, int height) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }

        g.drawImage(imageIcon.getImage(), x, y, null);

        String text = "NONE";
        if (mineLevel >=  0) {
            text = "M" + mineLevel;
        } else if (numberMark >= 0) {
            text = "+" + numberMark;
        }

        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        g.drawString(text, x + width / 2 - g.getFontMetrics().stringWidth(text) / 2,
                y + height / 2 - g.getFontMetrics().getAscent() / 2);

        System.out.println("paintSelf: " + text + " at " + x + ", " + y + " with size " + width + "x" + height);
    }
}

