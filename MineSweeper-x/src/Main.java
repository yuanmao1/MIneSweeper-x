import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Main {
    static JFrame frame = new JFrame("MineSweeper");
    public static void main(String[] args) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setResizable(false);
        new StartPage(frame);
    }
}
