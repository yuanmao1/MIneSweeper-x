import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
public class Settings {
    static int backGroundColor = 8;
    static public Color getBackGroundColor() {
        /*return switch (backGroundColor) {
            case 0 -> Color.RED;
            case 1 -> Color.GREEN;
            case 2 -> Color.BLUE;
            case 3 -> Color.YELLOW;
            case 4 -> Color.CYAN;
            case 5 -> Color.MAGENTA;
            case 6 -> Color.GRAY;
            case 7 -> Color.BLACK;
            default -> Color.WHITE;
        };*/
        Color result;
        switch (backGroundColor) {
            case 0:
                result = Color.RED;
                break;
            case 1:
                result = Color.GREEN;
                break;
            case 2:
                result = Color.BLUE;
                break;
            case 3:
                result = Color.YELLOW;
                break;
            case 4:
                result = Color.CYAN;
                break;
            case 5:
                result = Color.MAGENTA;
                break;
            case 6:
                result = Color.GRAY;
                break;
            case 7:
                result = Color.BLACK;
                break;
            default:
                result = Color.WHITE;
                break;
        }
        return result;
    }
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
                        backGroundColor = 0;
                        break;
                    case "Green":
                        frame.getContentPane().setBackground(Color.GREEN);
                        backGroundColor = 1;
                        break;
                    case "Blue":
                        frame.getContentPane().setBackground(Color.BLUE);
                        backGroundColor = 2;
                        break;
                    case "Yellow":
                        frame.getContentPane().setBackground(Color.YELLOW);
                        backGroundColor = 3;
                        break;
                    case "Cyan":
                        frame.getContentPane().setBackground(Color.CYAN);
                        backGroundColor = 4;
                        break;
                    case "Magenta":
                        frame.getContentPane().setBackground(Color.MAGENTA);
                        backGroundColor = 5;
                        break;
                    case "Gray":
                        frame.getContentPane().setBackground(Color.GRAY);
                        backGroundColor = 6;
                        break;
                    case "Black":
                        frame.getContentPane().setBackground(Color.BLACK);
                        backGroundColor = 7;
                        break;
                    case "White":
                        frame.getContentPane().setBackground(Color.WHITE);
                        backGroundColor = 8;
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
