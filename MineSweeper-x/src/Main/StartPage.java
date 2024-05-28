package Main;

import javax.swing.*;
import java.awt.*;

public class StartPage {
    StartPage(JFrame frame) {
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 设置通用的布局属性
        gbc.insets = new Insets(20, 10, 20, 10); // 按钮之间的间距

        // 创建按钮
        JButton beginButton = new JButton("Begin");
        beginButton.addActionListener(e -> {
            new GameSelectPage(frame);
        });
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> {
            new Settings(frame);
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        //调整按钮大小
        beginButton.setPreferredSize(new Dimension(200, 75));
        settingsButton.setPreferredSize(new Dimension(200, 75));
        exitButton.setPreferredSize(new Dimension(200, 75));

        // 将按钮添加到面板并设置位置
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(beginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(settingsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(exitButton, gbc);

        // 将面板添加到框架中
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Start Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setVisible(true);

        new StartPage(frame);
    }
}
