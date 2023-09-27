package ru.swarm.mind.view.component;

import ru.swarm.mind.Common;

import javax.swing.*;
import javax.swing.border.LineBorder;

import static ru.swarm.mind.Common.Colors.textColor;

public class UButton extends JButton {

    {
        setBackground(Common.Colors.color);
        setForeground(textColor);
        setBorder(new LineBorder(Common.Colors.border_color, 2));

    }

    public UButton(String text) {
        setText(text);
    }
}
