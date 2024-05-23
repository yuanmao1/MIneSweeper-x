import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Main {
    static JFrame frame = new JFrame("MineSweeper");
    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    new Settings();
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    new Settings().settings2();
                }
            }
        });
    }
}
