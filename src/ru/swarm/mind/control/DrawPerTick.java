package ru.swarm.mind.control;

import ru.swarm.mind.Common;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.component.Draw;
import ru.swarm.mind.view.activity.MainActivity;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static ru.swarm.mind.view.component.Draw.*;

public class DrawAction extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (Draw.speed.getX()<0.25f& Draw.speed.getX()>-0.25f) {
            Draw.speed.setLocation(0f, 0f);
        } else {
            if (Draw.speed.getX()<0) Draw.speed.setLocation(Draw.speed.getX()/1.15, Draw.speed.getY());
            if (Draw.speed.getX()>0) Draw.speed.setLocation(Draw.speed.getX()/1.15, Draw.speed.getY());
            if (Draw.speed.getY()<0) Draw.speed.setLocation(Draw.speed.getX(), Draw.speed.getY()/1.15);
            if (Draw.speed.getY()>0) Draw.speed.setLocation(Draw.speed.getX(), Draw.speed.getY()/1.15);
        }
        if (Draw.speed.getX()>10) Draw.speed.setLocation(9, Draw.speed.getY());
        if (Draw.speed.getX()<-10) Draw.speed.setLocation(-9, Draw.speed.getY());
        if (Draw.speed.getY()>10) Draw.speed.setLocation(Draw.speed.getX(), 9);
        if (Draw.speed.getY()<-10) Draw.speed.setLocation(Draw.speed.getX(), -9);
        xCam+=Draw.speed.getX();
        yCam+=Draw.speed.getY();
        Memory memory = Common.data.getVersions().get(Common.version_loaded).getData().get(0);
        MainActivity.info.setText("information: x="+ xCam+" y="+ yCam+" zoom="+ theZoom+"; memories="+Common.data.getVersions().get(Common.version_loaded).getData().size()+" versions="+Common.data.getVersions().size());
        Common.window.getRootPane().repaint();
    }
}
