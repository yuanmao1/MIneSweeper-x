package grassField;

import javax.swing.*;
import java.awt.*;

public class DrawTool {
    public static void paintInfoBar(Graphics g, ImageIcon leftIcon, int x, int y, int number) {
        g.drawImage(leftIcon.getImage(), x, y, null);
        number = number % 100;
        int b1 = number % 10;
        int b2 = number / 10;
        x += 100;
        g.drawImage(ImageHolder.numList[b2].getImage(), x, y, null);
        x += 26;
        g.drawImage(ImageHolder.numList[b1].getImage(), x, y, null);
    }
}
