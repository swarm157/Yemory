package ru.swarm.mind.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class IconGen {
    public static Image generateIcon() {
        var image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        var g =  (Graphics2D)image.getGraphics();
        for (int i = 0; i < 10; i++) {
            //g.setColor(Color.blue);
            g.setColor(new Color((int)(Math.random()*100)+30, (int)(Math.random()*50)+1,(int) (Math.random()*100)+30));
            var size = (int)(Math.random()*40);
            g.fill(new Ellipse2D.Float((int)(Math.random()*30), (int)(Math.random()*30), size, size));
            //g.fill(new Ellipse2D.Float(0, 0, 32, 32));
        }
        return image;
    }
}
