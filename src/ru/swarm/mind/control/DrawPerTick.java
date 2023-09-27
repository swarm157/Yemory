package ru.swarm.mind.control;

import ru.swarm.mind.Common;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.activity.MainActivity;
import ru.swarm.mind.view.component.Camera;
import ru.swarm.mind.view.component.DrawPanel;
import ru.swarm.mind.view.component.GroupMoving;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static ru.swarm.mind.Common.*;
import static ru.swarm.mind.view.component.Camera.*;
import static ru.swarm.mind.view.component.DrawPanel.computeRadius;
import static ru.swarm.mind.view.component.DrawPanel.found;


public class DrawPerTick extends AbstractAction {
    int i = 0;
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        compute();
        GroupMoving.compute();

        MainActivity.info.setText("information: x="+ xCam+" y="+ yCam+" zoom="+ zoom +"; memories="+Common.data.getVersions().get(Common.version_loaded).getData().size()+" versions="+Common.data.getVersions().size());

        i++;
        if (i>300) {
            i=0;


            ArrayList<Memory> toErasing = new ArrayList<>();
            for (Memory memory : data.getVersions().get(version_loaded).getData()) {
                for (Memory link : memory.getLinkedMemories()) {
                    if (!data.getVersions().get(version_loaded).getData().contains(link)) {
                        toErasing.add(link);
                    }
                }
            }
            data.getVersions().get(version_loaded).getData().removeAll(toErasing);
        }

        Common.window.getRootPane().repaint();
    }
}
