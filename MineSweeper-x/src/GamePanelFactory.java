import javax.swing.*;
import java.awt.*;

public class GamePanelFactory {
    //test code
    public static void main(String[] args) {
         JFrame frame = new JFrame();
         switchToGrassField(frame, 10, 10);
    }

    public static void switchToGrassField(JFrame frame, int rows, int cols) {
        GrassFieldPanel grassFieldPanel = new GrassFieldPanel(rows, cols);
        ComponentAssembler.assemble(frame, grassFieldPanel);
    }
}

class config {
    public static int cellSize = 25;
}

class GrassFieldPanel extends JPanel implements IGamePanel {
    private GrassCell grassCellArray[][] = null;

    public GrassFieldPanel(int rows, int cols) {
        grassCellArray = new GrassCell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grassCellArray[i][j] = new GrassCell(0);
            }
        }

        this.setPreferredSize(new Dimension(config.cellSize * cols, config.cellSize * rows));
    }

    public void paint(Graphics g) {
        super.paint(g);
        try {
            for (int i = 0; i < grassCellArray.length; i++) {
                for (int j = 0; j < grassCellArray[i].length; j++) {
                    grassCellArray[i][j].paintSelf(g, i * config.cellSize, j * config.cellSize, config.cellSize, config.cellSize);
                }
            }
        } catch (NullPointerException e) {
            // ignore
        }
    }

    public String getTitle() {
        return "Grass Field";
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

