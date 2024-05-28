import javax.swing.*;
import java.awt.event.*;
import java.awt.event.*;
import java.awt.*;

abstract class Game{
    JFrame frame;
    JPanel gamePanel = new JPanel();
    JPanel menuPanel;
    Game(){
        frame = new JFrame();
        menuPanel = createMenuPanel();
        gamePanel.setOpaque(false);
        // 捕捉 ESC 键
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ToggleMenu");
        gamePanel.getActionMap().put("ToggleMenu", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMenu();
            }
        });

        menuPanel.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 自定义关闭逻辑
                Main.frame.setVisible(true);
                Main.frame.toFront();
                Main.frame.requestFocus();
            }
        });
        frame.getContentPane().setBackground(Settings.getBackGroundColor());
        frame.setVisible(true);
    }
    abstract void gameInit();
    private void toggleMenu() {
        boolean isMenuVisible = menuPanel.isVisible();
        menuPanel.setVisible(!isMenuVisible);
        gamePanel.setVisible(isMenuVisible);
        frame.revalidate();
        frame.repaint();
    }
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> toggleMenu());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            frame.dispose();
            Main.frame.setVisible(true);
            Main.frame.toFront();
            Main.frame.requestFocus();
        });

        panel.add(resumeButton);
        panel.add(exitButton);

        return panel;
    }
}

class OrdinaryModeGame extends Game{
    OrdinaryModeGame(){
        super();
        frame.setTitle("普通模式");
        frame.setSize(500, 500);
        gamePanel.setLayout(null);
        JLabel label = new JLabel("普通模式");
        label.setBounds(0, 0, 100, 25);
        gamePanel.add(label);
    }
    void gameInit(){
        frame.getContentPane().add(gamePanel);
        frame.getContentPane().add(menuPanel);
        frame.revalidate();
        frame.repaint();
    }
}

public class GameFactory {
    public static Game creatGame(String gameType) {
        switch (gameType) {
            case "普通模式":
                return new OrdinaryModeGame();
            default:
                return null;
        }
    }
}
