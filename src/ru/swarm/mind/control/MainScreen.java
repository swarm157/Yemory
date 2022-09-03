package ru.swarm.mind.control;

import ru.swarm.mind.Common;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MainScreen extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Common.window.setRootActivity(Common.activities.get("MainActivity"));
    }
}
