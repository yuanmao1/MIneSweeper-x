import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
public class Settings {
    static int backGroundColor = 0;
    Settings(JFrame frame){
        frame.getContentPane().removeAll();
        frame.repaint();
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
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
        JLabel color = new JLabel("背景颜色:");
        String[] colors = {"Red", "Green", "Blue", "Yellow", "Cyan", "Magenta", "Gray", "Black", "White"};
        JComboBox<String> colorChooser = new JComboBox<>(colors);
        colorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String selectedColor = (String)cb.getSelectedItem();
                switch (selectedColor) {
                    case "Red":
                        frame.getContentPane().setBackground(Color.RED);
                        break;
                    case "Green":
                        frame.getContentPane().setBackground(Color.GREEN);
                        break;
                    case "Blue":
                        frame.getContentPane().setBackground(Color.BLUE);
                        break;
                    case "Yellow":
                        frame.getContentPane().setBackground(Color.YELLOW);
                        break;
                    case "Cyan":
                        frame.getContentPane().setBackground(Color.CYAN);
                        break;
                    case "Magenta":
                        frame.getContentPane().setBackground(Color.MAGENTA);
                        break;
                    case "Gray":
                        frame.getContentPane().setBackground(Color.GRAY);
                        break;
                    case "Black":
                        frame.getContentPane().setBackground(Color.BLACK);
                        break;
                    case "White":
                        frame.getContentPane().setBackground(Color.WHITE);
                        break;
                }
            }
        }); //更改背景颜色
        JButton backButton = new JButton("返回");
        backButton.addActionListener(e -> {new StartPage(frame);});
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(color, gbc);
        gbc.gridx = 1;
        panel.add(colorChooser, gbc);
        gbc.gridx = 2;
        gbc.gridy = 10;
        panel.add(backButton, gbc);
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        new Settings(frame);
        frame.setVisible(true);
    }
}
