import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GameSelectPage {
    GameSelectPage(JFrame frame){
        frame.getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Close");
        panel.getActionMap().put("Close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Closing Settings");
                new StartPage(frame);
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        JButton eazyButton = new JButton("简单模式");
        JButton mediumButton = new JButton("普通模式");
        JButton diffcultButton = new JButton("困难模式");
        eazyButton.setPreferredSize(new Dimension(200, 75));
        mediumButton.setPreferredSize(new Dimension(200, 75));
        diffcultButton.setPreferredSize(new Dimension(200, 75));
        eazyButton.addActionListener(e -> {});
        mediumButton.addActionListener(e -> {});
        diffcultButton.addActionListener(e -> {});
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(eazyButton, gbc);
        gbc.gridy = 1;
        panel.add(mediumButton, gbc);
        gbc.gridy = 2;
        panel.add(diffcultButton, gbc);
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }
}
