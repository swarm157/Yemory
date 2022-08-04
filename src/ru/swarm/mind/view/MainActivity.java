package ru.swarm.mind.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainActivity extends JFrame {

    JRootPane mainPane = new JRootPane() {{
        LayoutManager layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panel = new JPanel(layout);
        add(panel);
    }};
    MainActivity() {
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((ss.width-800)/2-(800-ss.width)/4, (ss.height-800)/2-(800-ss.height)/4);
        setSize(800+(800-ss.width)/2, 800+(800-ss.height)/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Yemory");
        setRootPane(mainPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainActivity window = new MainActivity();
    }
}
