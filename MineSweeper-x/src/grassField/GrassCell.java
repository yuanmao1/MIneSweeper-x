package grassField;

import javax.swing.*;
import java.awt.*;

public class GrassCell {
    //isFlag
    private boolean isFlag = false;
    public boolean isFlag() {
        return isFlag;
    }
    public void setIsFlag(final boolean isFlag) {
        this.isFlag = isFlag;
    }

    //row and col
    private int row = 1;
    public int getRow() {
        return row;
    }
    public void setRow(final int row) {
        this.row = row;
    }
    private int col = 1;
    public int getCol() {
        return col;
    }
    public void setCol(final int col) {
        this.col = col;
    }

    // is searched
    private boolean isSearched = false;
    public boolean isSearched() {
        return isSearched;
    }
    public void setSearched(final boolean isSearched) {
        this.isSearched = isSearched;
    }

    private int mineLevel = 0;
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

    //is covered or not
    private boolean isCovered = true
            ;
    public boolean isCovered() {
        return isCovered;
    }
    public void setCovered(final boolean isCovered) {
        this.isCovered = isCovered;
    }

    public GrassCell() {
        //nothing to do
    }

    public void paintSelf(Graphics g, int x, int y, int width, int height) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }

        do {
            ImageIcon imageIcon = ImageHolder.blank;
            if (isCovered) {
                imageIcon = ImageHolder.cover;
                g.drawImage(imageIcon.getImage(), x, y, null);
                break;
            }

            g.drawImage(imageIcon.getImage(), x, y, null);

            String text = "";
            if (numberMark > 0) {
                g.setColor(Color.BLUE);
                text = "<" + numberMark + ">";
            }
            if (mineLevel > 0) { //is mine
                g.setColor(Color.RED);
                text = "M" + mineLevel;
            }

            if (isFlag) {
                g.setColor(Color.CYAN);
            }

            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(text, x + width / 2 - g.getFontMetrics().stringWidth(text) / 2,
                    y + height / 2 + g.getFontMetrics().getAscent() / 2);
        } while (false);

        //System.out.println("paintSelf: at " + x + ", " + y + " with size " + width + "x" + height);
    }
}
