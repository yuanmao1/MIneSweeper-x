package Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

import grassField.*;

abstract class Game{
    JFrame frame;
    JPanel gamePanel = new JPanel();
    JPanel menuPanel;
    //存储图片
    ImageIcon[] images = new ImageIcon[9];
    ImageIcon mineIcon = new ImageIcon("resources/images2/landmine.png");
    ImageIcon explodedMineIcon = new ImageIcon("resources/images2/explodedLandmine.png");
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
        gbc.insets = new Insets(15, 10, 15, 10);
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
        resumeButton.setPreferredSize(new Dimension(100, 50));
        panel.add(resumeButton, gbc);
        gbc.gridy = 10;
        exitButton.setPreferredSize(new Dimension(100, 50));
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
        GamePanelStarter.switchToGrassField(frame, 12, 18);
        gameInit();
    }
    void gameInit(){
        frame.revalidate();
        frame.repaint();
    }
}
class OrdinaryModeGame extends Game{
    int row = 16, col = 16;
    int rate = 10;
    OrdinaryModeGame(){
        super();
        frame.setTitle("普通模式");
        frame.setSize(500, 500);
        gamePanel.setLayout(null);
        JButton difficultyButton = new JButton("Difficulty");
        difficultyButton.setPreferredSize(new Dimension(100, 50));
        difficultyButton.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "Difficulty", true);
            dialog.setLayout(new GridBagLayout());
            dialog.setSize(300, 175);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLocationRelativeTo(null);
            JLabel label = new JLabel("Select Difficulty:");
            JRadioButton easyButton = new JRadioButton("Easy");
            JRadioButton mediumButton = new JRadioButton("Medium");
            JRadioButton hardButton = new JRadioButton("Hard");
            ButtonGroup group = new ButtonGroup();
            group.add(easyButton);
            group.add(mediumButton);
            group.add(hardButton);
            mediumButton.setSelected(true);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(20, 10, 20, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            dialog.add(label, gbc);
            gbc.gridy = 1;
            dialog.add(easyButton, gbc);
            gbc.gridx = 1;
            dialog.add(mediumButton, gbc);
            gbc.gridx = 2;
            dialog.add(hardButton, gbc);
            dialog.add(label);
            dialog.add(easyButton);
            dialog.add(mediumButton);
            dialog.add(hardButton);
            difficultyButton.setEnabled(false);
            JButton yesButton = new JButton("YES");
            yesButton.addActionListener(e1 -> {
                if (easyButton.isSelected()) {
                    row = 10;
                    col = 10;
                    rate = 12;
                } else if (mediumButton.isSelected()) {
                    row = 16;
                    col = 16;
                    rate = 10;
                } else if (hardButton.isSelected()) {
                    row = 20;
                    col = 20;
                    rate = 8;
                }
                difficultyButton.setEnabled(true);
                gameInit();
                dialog.dispose();
            });
            gbc.gridy = 2;
            gbc.gridx = 3;
            dialog.add(yesButton, gbc);
            dialog.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e) {
                    difficultyButton.setEnabled(true);
                }
            });
            dialog.setVisible(true);
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        menuPanel.add(difficultyButton, gbc);
        gameInit();
    }
    boolean[][] isMine;
    boolean isGameStart = false;
    int mineNum;
    int flagNum;

    JButton[][] buttons; // 按钮数组
    JLabel allMineLabel = new JLabel("总雷数：" + mineNum); // 总雷数标签
    JLabel mineLabel = new JLabel("剩余雷数：" + 0); // 剩余雷数标签
    void gameInit(){
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        gamePanel.removeAll();
        initialize();
        mineLabel.setText("剩余雷数：" + 0);
        allMineLabel.setForeground(new Color(255 - Settings.getBackGroundColor().getRed(), 255 - Settings.getBackGroundColor().getGreen(), 255 - Settings.getBackGroundColor().getBlue()));
        mineLabel.setForeground(new Color(255 - Settings.getBackGroundColor().getRed(), 255 - Settings.getBackGroundColor().getGreen(), 255 - Settings.getBackGroundColor().getBlue()));
        allMineLabel.setBounds(20, 10, 100, 20);
        mineLabel.setBounds(130, 10, 100, 20);
        gamePanel.add(allMineLabel);
        gamePanel.add(mineLabel);
        frame.getContentPane().setLayout(new CardLayout());
        int width = 400 / col, height = 400 / row;
        coverIcon = resizeIcon(coverIcon, width, height);
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                hmap[i][j] = false;
                buttons[i][j] = new JButton(coverIcon);
                buttons[i][j].setBounds(40 + height * i, 30 + width * j, width, height);
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
                                    Icon icon = resizeIcon(flagIcon, width, height);
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
                                        gbc.gridx = 0;
                                        gbc.gridy = 0;
                                        win.add(label, gbc);
                                        gbc.gridy = 1;
                                        win.add(restartButton, gbc);
                                        win.add(label);
                                        win.add(restartButton);
                                        win.setVisible(true);
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
    void initialize(){
        isMine = new boolean[row][col];
        buttons = new JButton[row][col];
        hmap = new boolean[row][col];
        mineNum = row * col / rate;
        flagNum = 0;
        isGameStart = false;
        allMineLabel.setText("总雷数： " + mineNum );
    }
    void placingMines(int x, int y){
        Random rand = new Random();
        int count = 0;
        while (count < mineNum) {
            int i = rand.nextInt(row);
            int j = rand.nextInt(col);
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
    boolean hmap[][];
    void sweep(int x, int y){
        if(isMine[x][y]) {
            Icon icon = resizeIcon(mineIcon, 400 / col, 400 / row);
            buttons[x][y].setIcon(icon);
            frame.revalidate();
            frame.repaint();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Icon icon = resizeIcon(explodedMineIcon, 400 / col, 400 / row);
                    buttons[x][y].setIcon(icon);
                    frame.revalidate();
                    frame.repaint();
                    gameOver();
                }
            },1000);
            return;
        }
        else{
            int count = 0;
            if(x > 0 && isMine[x-1][y]) count++;
            if(x < row - 1 && isMine[x+1][y]) count++;
            if(y > 0 && isMine[x][y-1]) count++;
            if(y < col - 1 && isMine[x][y+1]) count++;
            if(x > 0 && y > 0 && isMine[x-1][y-1]) count++;
            if(x < row - 1 && y > 0 && isMine[x+1][y-1]) count++;
            if(x > 0 && y < col - 1 && isMine[x-1][y+1]) count++;
            if(x < row - 1 && y < col - 1 && isMine[x+1][y+1]) count++;
            Icon icon = resizeIcon(images[count], 400 / col, 400 / row);
            buttons[x][y].setIcon(icon);
            hmap[x][y] = true;
            if(count == 0){
                if(x > 0 && !hmap[x-1][y]) sweep(x - 1, y);
                if(x < row -1 && !hmap[x+1][y]) sweep(x + 1, y);
                if(y > 0 && !hmap[x][y-1]) sweep(x, y - 1);
                if(y < col - 1 && !hmap[x][y+1]) sweep(x, y + 1);
                if(x > 0 && y > 0 && !hmap[x-1][y-1])  sweep(x - 1, y - 1);
                if(x < row - 1 && y > 0 && !hmap[x+1][y-1]) sweep(x + 1, y - 1);
                if(x > 0 && y < col - 1 && !hmap[x-1][y+1]) sweep(x - 1, y + 1);
                if(x < row - 1 && y < col - 1 && !hmap[x+1][y+1]) sweep(x + 1, y + 1);
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
