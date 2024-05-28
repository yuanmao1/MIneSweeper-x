import javax.swing.*;
import java.awt.*;

public class GrassCell {
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
    private boolean isCovered = false;
    public boolean isCovered() {
        return isCovered;
    }
    public void setCovered(final boolean isCovered) {
        this.isCovered = isCovered;
    }

    private ImageIcon imageIcon = ImageHolder.blank;

    public GrassCell() {
        //nothing to do
    }

    public void paintSelf(Graphics g, int x, int y, int width, int height) {
        if (g == null) {
            throw new IllegalArgumentException("Graphics object cannot be null.");
        }

        do {
            if (isCovered) {
                imageIcon = ImageHolder.cover;
                g.drawImage(imageIcon.getImage(), x, y, null);
                break;
            }

            g.drawImage(imageIcon.getImage(), x, y, null);

            String text = ".";
            if (numberMark > 0) {
                text = "<" + numberMark + ">";
            }
            if (mineLevel > 0) { //is mine
                text = "M" + mineLevel;
            }

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(text, x + width / 2 - g.getFontMetrics().stringWidth(text) / 2,
                    y + height / 2 + g.getFontMetrics().getAscent() / 2);
        } while (false);

        System.out.println("paintSelf: at " + x + ", " + y + " with size " + width + "x" + height);
    }
}
