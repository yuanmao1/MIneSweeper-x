import javax.swing.*;
import java.awt.*;

public class GrassFieldTest {
    public static void main(String[] args) {
        JFrame grassField = new GrassField(10, 10);
    }
}

class GrassField extends JFrame {
    Cell cellArray[][] = null;
    final InnerJPanel panel = new InnerJPanel();

    public GrassField(int rows, int cols) {
        super("Grass Field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 25 * cols, 25 * rows);

        setLayout(null);
        cellArray = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellArray[i][j] = new Cell(0);
            }
        }

        panel.setCellArray(cellArray);
        panel.setPreferredSize(new Dimension(25 * cols, 25 * rows));

        setContentPane(panel);
        this.pack();

        setVisible(true);

        panel.repaint();
    }
}

class InnerJPanel extends JPanel {
    private Cell cellArray[][] = null;

    public void paint(Graphics g) {
        super.paint(g);
        try {
            for (int i = 0; i < cellArray.length; i++) {
                for (int j = 0; j < cellArray[i].length; j++) {
                    cellArray[i][j].paintSelf(g, i * 25, j * 25, 25, 25);
                }
            }
        } catch (NullPointerException e) {
            // ignore
        }
    }

    public void setCellArray(Cell cellArray[][]) {
        this.cellArray = cellArray;
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
        g.drawString(text, x + width / 2 - g.getFontMetrics().stringWidth(text),
                y + height / 2 - g.getFontMetrics().getAscent() / 2);

        System.out.println("paintSelf: " + text + " at " + x + ", " + y + " with size " + width + "x" + height);
    }
}

class ImageHolder {
    public static final ImageIcon blank = new ImageIcon("resources/images/num0.png");
}