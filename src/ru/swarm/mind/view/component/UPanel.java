package ru.swarm.mind.view.component;

import ru.swarm.mind.Common;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static ru.swarm.mind.Common.Colors.border_color;

public class UPanel extends JPanel {
    {
        setBorder(new LineBorder(border_color, 1));
        setBackground(Common.Colors.bgPanelColor);
    }
    public void add(JComponent... components) {
        for(JComponent component : components) {
            add(component);
        }
    }
}
