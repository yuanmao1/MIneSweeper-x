package Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import java.util.random.*;
import grassField.*;

abstract class Game{
    JFrame frame;
    JPanel gamePanel = new JPanel();
    JPanel menuPanel;
    //存储图片
    ImageIcon[] images = new ImageIcon[9];
    ImageIcon mineIcon = new ImageIcon("resources/images2/landmine.png");
    ImageIcon flagIcon = new ImageIcon("resources/images2/flag.png");
    ImageIcon coverIcon = new ImageIcon("resources/images2/cover.png");
    Game(){
        for(int i=0;i<9;i++){
            images[i] = new ImageIcon("resources/images2/num"+i+".png");
        }
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
        menuPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ToggleMenu");
        menuPanel.getActionMap().put("ToggleMenu", new AbstractAction() {
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
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> toggleMenu());
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            frame.dispose();
            Main.frame.setVisible(true);
            Main.frame.toFront();
            Main.frame.requestFocus();
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(resumeButton, gbc);
        gbc.gridy = 1;
        panel.add(exitButton, gbc);
        return panel;
    }
    public ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
class GrassField extends Game{
    GrassField(){
        GamePanelTest.switchToGrassField(frame, 12, 18);
        gameInit();
    }
    void gameInit(){
        frame.revalidate();
        frame.repaint();
    }
}
class OrdinaryModeGame extends Game{
    OrdinaryModeGame(){
        super();
        frame.setTitle("普通模式");
        frame.setSize(500, 500);
        gamePanel.setLayout(null);
        gameInit();
    }
    boolean[][] isMine = new boolean[20][20];
    boolean isGameStart = false;
    int mineNum = 40;
    int flagNum = 0;

    JButton[][] buttons = new JButton[20][20]; // 按钮数组
    JLabel allMineLabel = new JLabel("总雷数：" + mineNum); // 总雷数标签
    JLabel mineLabel = new JLabel("剩余雷数：" + 0); // 剩余雷数标签
    void gameInit(){
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        gamePanel.removeAll();
        mineLabel.setText("剩余雷数：" + 0);
        flagNum = 0;
        allMineLabel.setForeground(new Color(255 - Settings.getBackGroundColor().getRed(), 255 - Settings.getBackGroundColor().getGreen(), 255 - Settings.getBackGroundColor().getBlue()));
        mineLabel.setForeground(new Color(255 - Settings.getBackGroundColor().getRed(), 255 - Settings.getBackGroundColor().getGreen(), 255 - Settings.getBackGroundColor().getBlue()));
        allMineLabel.setBounds(20, 10, 100, 20);
        mineLabel.setBounds(130, 10, 100, 20);
        gamePanel.add(allMineLabel);
        gamePanel.add(mineLabel);
        frame.getContentPane().setLayout(new CardLayout());
        coverIcon = resizeIcon(coverIcon, 20, 20);
        for(int i = 0; i < 20; i++){
            for(int j = 0; j < 20; j++){
                hmap[i][j] = false;
                buttons[i][j] = new JButton(coverIcon);
                buttons[i][j].setBounds(40 + 20 * i, 30 + 20 * j, 20, 20);
                int finalI = i;
                int finalJ = j;
                buttons[i][j].addActionListener(e -> {
                    if(buttons[finalI][finalJ].getIcon() == coverIcon) {
                        if(!isGameStart){
                           placingMines(finalI, finalJ);
                           sweep(finalI, finalJ);
                           isGameStart = true;
                        }
                        else sweep(finalI, finalJ);
                        frame.revalidate();
                        frame.repaint();
                    }
                        });
                buttons[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (buttons[finalI][finalJ].getIcon() == coverIcon) {
                                if (isMine[finalI][finalJ]) {
                                    Icon icon = resizeIcon(flagIcon, 20, 20);
                                    buttons[finalI][finalJ].setIcon(icon);
                                    flagNum++;
                                    mineLabel.setText("剩余雷数：" + (mineNum - flagNum));
                                    if (flagNum == mineNum) {
                                        JDialog win = new JDialog(frame, "You Win", true);
                                        win.setLayout(new GridBagLayout());
                                        win.setSize(300, 100);
                                        win.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                                        win.setLocationRelativeTo(null);
                                        JLabel label = new JLabel("You Win");
                                        JButton restartButton = new JButton("Restart");
                                        restartButton.addActionListener(e1 -> {
                                            gameInit();
                                            win.dispose();
                                        });
                                        GridBagConstraints gbc = new GridBagConstraints();
                                        gbc.insets = new Insets(20, 10, 20, 10);
                                        gbc.fill = GridBagConstraints.HORIZONTAL;
                                        gbc.gridx = 1;
                                        gbc.gridy = 0;
                                        win.add(label, gbc);
                                        gbc.gridx = 0;
                                        gbc.gridy = 1;
                                        win.add(restartButton, gbc);
                                        win.add(label);
                                        win.add(restartButton);
                                    }
                                }
                                else gameOver();
                                frame.revalidate();
                                frame.repaint();
                            }
                        }
                    }
                });
                gamePanel.add(buttons[i][j]);
            }
        }
        frame.getContentPane().add(gamePanel, "gamePanel");
        frame.getContentPane().add(menuPanel, "menuPanel");
        frame.revalidate();
        frame.repaint();
    }
    void placingMines(int x, int y){
        Random rand = new Random();
        int count = 0;
        while (count < mineNum) {
            int i = rand.nextInt(20);
            int j = rand.nextInt(20);
            if ((i != x || j != y) && !isMine[i][j]) {
                isMine[i][j] = true;
                count++;
            }
        }
    }
    void gameOver(){
        JDialog gameOver = new JDialog(frame, "Game Over", true);
        gameOver.setLayout(new GridBagLayout());
        gameOver.setSize(300, 100);
        gameOver.setLocationRelativeTo(null);
        JLabel label = new JLabel("Game Over");
        JButton restartButton = new JButton("Restart");
        JButton exitButton = new JButton("Exit");
        restartButton.addActionListener(e -> {
            gameInit();
            gameOver.dispose();
        });
        exitButton.addActionListener(e -> {
            frame.dispose();
            Main.frame.setVisible(true);
            Main.frame.toFront();
            Main.frame.requestFocus();
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gameOver.add(label, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gameOver.add(restartButton, gbc);
        gbc.gridx = 2;
        gameOver.add(exitButton, gbc);
        gameOver.add(label);
        gameOver.add(restartButton);
        gameOver.add(exitButton);
        gameOver.setVisible(true);
    }
    boolean hmap[][] = new boolean[20][20];
    void sweep(int x, int y){
        if(isMine[x][y]) {
            buttons[x][y].setIcon(mineIcon);
            gameOver();
            return;
        }
        else{
            int count = 0;
            if(x > 0 && isMine[x-1][y]) count++;
            if(x < 19 && isMine[x+1][y]) count++;
            if(y > 0 && isMine[x][y-1]) count++;
            if(y < 19 && isMine[x][y+1]) count++;
            if(x > 0 && y > 0 && isMine[x-1][y-1]) count++;
            if(x < 19 && y > 0 && isMine[x+1][y-1]) count++;
            if(x > 0 && y < 19 && isMine[x-1][y+1]) count++;
            if(x < 19 && y < 19 && isMine[x+1][y+1]) count++;
            Icon icon = resizeIcon(images[count], 20, 20);
            buttons[x][y].setIcon(icon);
            hmap[x][y] = true;
            if(count == 0){
                if(x > 0 && !hmap[x-1][y]) sweep(x - 1, y);
                if(x < 19 && !hmap[x+1][y]) sweep(x + 1, y);
                if(y > 0 && !hmap[x][y-1]) sweep(x, y - 1);
                if(y < 19 && !hmap[x][y+1]) sweep(x, y + 1);
                if(x > 0 && y > 0 && !hmap[x-1][y-1])  sweep(x - 1, y - 1);
                if(x < 19 && y > 0 && !hmap[x+1][y-1]) sweep(x + 1, y - 1);
                if(x > 0 && y < 19 && !hmap[x-1][y+1]) sweep(x - 1, y + 1);
                if(x < 19 && y < 19 && !hmap[x+1][y+1]) sweep(x + 1, y + 1);
            }
        }
        frame.revalidate();
        frame.repaint();
    }
}

public class GameFactory {
    public static Game creatGame(String gameType) {
        switch (gameType) {
            case "普通模式":
                return new OrdinaryModeGame();
            case "除草模式":
                return new GrassField();
            default:
                return null;
        }
    }
}
