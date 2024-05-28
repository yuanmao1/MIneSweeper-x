package grassField;

import javax.swing.*;


public class ComponentAssembler {
    public static void assemble(JFrame frame, IGamePanel gamePanel) {
        if (frame == null) {
            throw new IllegalArgumentException("Frame object cannot be null.");
        }
        if (gamePanel == null) {
            throw new IllegalArgumentException("GamePanel object cannot be null.");
        }
        if (!(gamePanel instanceof JPanel)) {
            throw new IllegalArgumentException("GamePanel object must be an instance of JPanel.");
        }

        frame.getContentPane().removeAll();
        frame.setTitle(gamePanel.getTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setContentPane((JPanel) gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}
