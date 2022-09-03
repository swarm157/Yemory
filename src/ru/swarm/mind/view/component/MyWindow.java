package ru.swarm.mind.view.component;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static ru.swarm.mind.control.Save.save;

public class MyWindow extends JFrame {
    {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                save();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                super.windowIconified(e);
                save();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                super.windowLostFocus(e);
                save();
            }
        });
    }
    public void setRootActivity(JRootPane rootPane) {
        setRootPane(rootPane);
    }
}
