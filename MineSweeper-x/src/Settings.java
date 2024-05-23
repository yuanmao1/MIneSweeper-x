import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Settings {
    Settings(JFrame frame){
        frame.getContentPane().removeAll();
        frame.setTitle("Settings");
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }
    Settings(){
        JFrame frame = Main.frame;
        frame.setTitle("Settings");
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    public void settings2(){
        JFrame frame = Main.frame;
        frame.setTitle("Settings2");
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }
}
