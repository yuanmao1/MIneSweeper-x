package grassField;

import javax.swing.*;

public class GamePanelTest {
    //test code
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        switchToGrassField(frame, 10, 15);

        frame.setBounds(100, 100, frame.getPreferredSize().width, frame.getPreferredSize().height);
    }

    public static void switchToGrassField(JFrame frame, int rows, int cols) {
        GrassFieldPanel grassFieldPanel = new GrassFieldPanel(rows, cols, Difficulty.getEasy());
        ComponentAssembler.assemble(frame, grassFieldPanel);
    }
}

