package ru.swarm.mind.view.component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class UPanel extends JPanel {
    public static final Color color = new Color(70, 57,69);
    public static final Color border_color = new Color(25, 10,25);
    {
        setBorder(new LineBorder(border_color, 1));
        setBackground(color);
    }
    public void add(JComponent... components) {
        for(JComponent component : components) {
            add(component);
        }
    }
}
