package ru.swarm.mind.view.component;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static ru.swarm.mind.Common.textColor;

public class UButton extends JButton {
    public static final Color color = new Color(55, 30,50);
    public static final Color border_color = new Color(30, 10,25);

    {
        setBackground(color);
        setForeground(textColor);
        setBorder(new LineBorder(border_color, 2));

    }

    public UButton(String text) {
        setText(text);
    }
}
