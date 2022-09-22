package ru.swarm.mind.view.component;

import ru.swarm.mind.Common;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.activity.MainActivity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static ru.swarm.mind.Common.DEFAULT_RADIUS;
import static ru.swarm.mind.Common.window;

public class Draw extends JPanel {
    public static Memory choosed = null,
                         prevChoosed = null;
    private static boolean pressed = false;
    private static Point2D prevPoint = null;
    public static Point2D moved = null;
    private static Point2D click = null;
    public static Point2D speed = new Point2D.Float(0, 0);
    {

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                theZoom +=((float)mouseWheelEvent.getWheelRotation())/2;
                if (theZoom==0) theZoom=0.1f;
                if (theZoom<0) theZoom*=-1;
                repaint();

            }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if(choosed==null) {
                if (prevPoint==null)
                    prevPoint = mouseEvent.getPoint();
                Point2D current = mouseEvent.getPoint();
                speed.setLocation(speed.getX()+(current.getX()-prevPoint.getX())/theZoom, speed.getY()+(current.getY()-prevPoint.getY())/theZoom);
                prevPoint = current;
                } else {
                    float x = ((float)mouseEvent.getPoint().getX()-xCam)/theZoom*2-((float) DEFAULT_RADIUS);
                    float y = ((float)mouseEvent.getPoint().getY()-yCam)/theZoom*2-((float) DEFAULT_RADIUS);
                    choosed.setPoint(new Point2D.Float(x, y));
                    MainActivity.East.xPos.setText(String.valueOf(choosed.getPoint().getX()));
                    MainActivity.East.yPos.setText(String.valueOf(choosed.getPoint().getY()));
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                if (!pressed) {
                    moved = mouseEvent.getPoint();
                    prevPoint=null;
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                click=e.getPoint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                pressed=true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pressed=false;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                System.out.println("wheel");
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                System.out.println("moved");
            }
        });
    }

    public static float xCam = 0,
          yCam = 0,
          theZoom = 1f;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    float getRange(Point2D p1, Point2D p2) {
        float result = ((float) Math.abs(p1.getX()- p2.getX()))+((float) Math.abs(p1.getY()- p2.getY()));
        return result;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(25, 17, 35));
        g2.fillRect(0, 0, window.getWidth(), window.getHeight());
        boolean nothing = true;
        for (Memory memory : Common.data.getVersions().get(Common.version_loaded).getData()) {
            float radius = DEFAULT_RADIUS* theZoom+(memory.getDescription().length()+memory.getName().length())/150;
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()*theZoom+xCam-(memory.getPoint().getX()*theZoom/2)), (float) ((float) memory.getPoint().getY()*theZoom+yCam-(memory.getPoint().getY()*theZoom/2)), radius, radius);
            if (click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                nothing = false;
            }
        }
        if (click!=null&&nothing) {
            float x = ((float)click.getX()-xCam)/theZoom*2-((float) DEFAULT_RADIUS);
            float y = ((float)click.getY()-yCam)/theZoom*2-((float) DEFAULT_RADIUS);
            choosed = new Memory("", new Point2D.Float(x, y));
            Common.data.getVersions().get(Common.version_loaded).getData().
                    add(choosed);
            {
                JTextArea area = MainActivity.East.area;
                MainActivity.East.name.setText(choosed.getName());
                MainActivity.East.xPos.setText(String.valueOf(choosed.getPoint().getX()));
                MainActivity.East.yPos.setText(String.valueOf(choosed.getPoint().getY()));
                MainActivity.East.color.setBackground(choosed.getColor());
                JList<String> tags = MainActivity.East.tags;
                tags.setListData(choosed.getTags().toArray(new String[choosed.getTags().size()]));
                area.setText(choosed.getDescription());
                click = null;
            }
            MainActivity.West.update();
        }
        for (Memory memory : Common.data.getVersions().get(Common.version_loaded).getData()) {
            float radius = DEFAULT_RADIUS* theZoom+(memory.getDescription().length()+memory.getName().length())/150;
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()*theZoom+xCam-(memory.getPoint().getX()*theZoom/2)), (float) ((float) memory.getPoint().getY()*theZoom+yCam-(memory.getPoint().getY()*theZoom/2)), radius, radius);
            g2.setColor(memory.getColor());
            g2.fill(ellipse);
            if (choosed!=null&&click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                prevChoosed = choosed;
                choosed = null;
                click = null;

                if (prevChoosed!=null) {
                    prevChoosed.setName(MainActivity.East.name.getText());
                    JTextArea area = MainActivity.East.area;
                    prevChoosed.setDescription(area.getText());
                    var data = MainActivity.East.tags.getModel();
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < data.getSize(); i++) {
                        list.add((String) data.getElementAt(i));
                    }
                    prevChoosed.setTags(list);
                    prevChoosed.setColor(MainActivity.East.color.getBackground());
                    prevChoosed.setPoint(new Point2D.Float(Float.parseFloat(MainActivity.East.xPos.getText()), Float.parseFloat(MainActivity.East.yPos.getText())));
                }
            }
            if (memory!=choosed)
                if (click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {

                    choosed = memory;
                    JTextArea area = MainActivity.East.area;
                    MainActivity.East.name.setText(choosed.getName());
                    MainActivity.East.xPos.setText(String.valueOf(choosed.getPoint().getX()));
                    MainActivity.East.yPos.setText(String.valueOf(choosed.getPoint().getY()));
                    MainActivity.East.color.setBackground(choosed.getColor());
                    JList<String> tags = MainActivity.East.tags;
                    tags.setListData(choosed.getTags().toArray(new String[choosed.getTags().size()]));
                    area.setText(choosed.getDescription());
                    click = null;
                } else if (moved!=null&&getRange(moved, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) g2.setColor(Color.GRAY);
                else g2.setColor(Color.BLACK);
            else g2.setColor(Color.WHITE);
            g2.draw(ellipse);

        }
    }
}
