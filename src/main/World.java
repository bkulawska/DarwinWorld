import viewClasses.SettingsPanel;
import javax.swing.*;

public class World {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        SettingsPanel panel = new SettingsPanel();

        frame.add(panel);
        frame.setVisible(true);
    }
}