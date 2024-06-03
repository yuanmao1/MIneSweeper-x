package grassField;
import javax.swing.*;

public class GamePanelStarter {
    //test code
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        switchToGrassField(frame, 20, 40);

        frame.setBounds(100, 100, frame.getPreferredSize().width, frame.getPreferredSize().height);
    }

    public static void switchToGrassField(JFrame frame, int rows, int cols) {
        int numberOfMines = (int)(rows * cols * 0.12);
        numberOfMines = Math.max(numberOfMines, 7);
        GrassFieldPanel grassFieldPanel = new GrassFieldPanel(rows, cols, numberOfMines);
        grassFieldPanel.setFrame(frame);
        ComponentAssembler.assemble(frame, grassFieldPanel);
    }
}