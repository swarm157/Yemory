package ru.swarm.mind.view.component;

import ru.swarm.mind.Common;
import ru.swarm.mind.model.Group;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.activity.MainActivity;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;

import static ru.swarm.mind.Common.*;
import static ru.swarm.mind.view.component.Camera.*;
import static ru.swarm.mind.view.component.DrawPanel.Interact.*;

public class DrawPanel extends JPanel {


    private class MemoryDeleteAnimation extends Memory {
        public MemoryDeleteAnimation(Memory memory) {
            super(memory.getName(), memory.getTags(), memory.getDescription(), memory.getPoint(), memory.getColor(),memory.getLinkedMemories());
        }

        int i = 100;
        public boolean draw(Graphics2D g2) {
            i-=5;
            if (i<=0) return false;
            float radius = computeRadius(this);
            Color t = getColor();
            setColor(new Color(t.getRed(), t.getGreen(), t.getBlue(), i));
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) getPoint().getX()* zoom +xCam-(getPoint().getX()* zoom /2))-(radius*(100f/i)/2), (float) ((float) getPoint().getY()* zoom +yCam-(getPoint().getY()* zoom /2))-(radius*(100f/i)/2), radius*(100f/i), radius*(100f/i));
            g2.setColor(getColor());
            g2.draw(ellipse);
            g2.fill(ellipse);
            return true;
        }
    }

    private class MemoryAnimation extends Memory {
        @Serial
        private static final long serialVersionUID = 1L;
        int i = 0;

        Color realColor;
        public MemoryAnimation(String name, Point2D point) {

            super(name, point);
            //super(name, new Point2D.Float(((float)point.getX()-xCam)/ zoom *2-((float) DEFAULT_RADIUS/2), ((float)point.getY()-yCam)/ zoom *2-((float) DEFAULT_RADIUS/2)));
            //float x = ((float)mouseEvent.getPoint().getX()-xCam)/ zoom *2;
            //float x = ((float)mouseEvent.getPoint().getX()-xCam)/ zoom *2-((float) DEFAULT_RADIUS/2);
            //float y = ((float)mouseEvent.getPoint().getY()-yCam)/ zoom *2;
        }
        
        public boolean draw(Graphics2D g2) {
            i+=5;
            if (i>=100) {setColor(realColor); return false;}
            Color t = getColor();
            setColor(new Color(t.getRed(), t.getGreen(), t.getBlue(), i));
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) getPoint().getX()* zoom +xCam-(getPoint().getX()* zoom /2))-(computeRadius(this)/100*i/2), (float) ((float) getPoint().getY()* zoom +yCam-(getPoint().getY()* zoom /2))-(computeRadius(this)/100*i/2), computeRadius(this)/100*i, computeRadius(this)/100*i);
            g2.setColor(getColor());
            g2.draw(ellipse);g2.fill(ellipse);
            return true;
        }

        public MemoryAnimation(Memory memory) {
            super(memory.getName(), memory.getTags(), memory.getDescription(), memory.getPoint(), memory.getColor(), memory.getLinkedMemories());
            //super(memory.getName(), memory.getTags(), memory.getDescription(), new Point2D.Double(memory.getPoint().getX()+computeRadius(memory)/2, memory.getPoint().getY()+computeRadius(memory)/2), memory.getColor(), memory.getLinkedMemories());
            realColor = getColor();
        }
        
    }

    private class SelectionDissapair {

        Point2D p1;
        Point2D p2;
        int i = 100;

        public SelectionDissapair( Point2D p1, Point2D p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public boolean draw(Graphics2D g2) {
            if (i<=0) return false;
            if (p1!=null&&p2!=null) {
                i-=5;
                //g2.setColor(new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), i / 2));
                g2.setColor(createColorWithTransparency(Colors.groupSelect, i));
                g2.fillRect((int) p1.getX(), (int) p1.getY(), (int) ((int) p2.getX()-p1.getX()), (int) ((int) p2.getY()-p1.getY()));
                //g2.setColor(new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), i));
                g2.setColor(createColorWithTransparency(Colors.color, i));
                g2.drawRect((int) p1.getX(), (int) p1.getY(), (int) ((int) p2.getX()-p1.getX()), (int) ((int) p2.getY()-p1.getY()));
            } else {
                return false;
            }
            return true;
        }

    }

    private class MoveAnimation extends Memory {
        int i = 100;

        public MoveAnimation(String name, Point2D point) {
            super(name, point);
        }

        public boolean draw(Graphics2D g2) {
            i-=5;
            if (i<=0) return false;
            float radius = computeRadius(this);
            Color t = getColor();
            setColor(new Color(t.getRed(), t.getGreen(), t.getBlue(), i));
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) getPoint().getX()* zoom +xCam-(getPoint().getX()* zoom /2))-radius/2, (float) ((float) getPoint().getY()* zoom +yCam-(getPoint().getY()* zoom /2))-radius/2, radius, radius);
            g2.setColor(getColor());
            g2.draw(ellipse);
            return true;
        }
        public MoveAnimation(Memory memory) {
            super(memory.getName(), memory.getTags(), memory.getDescription(), memory.getPoint(), memory.getColor(), memory.getLinkedMemories());
        }
    }

    public static class Animation {
        public static boolean enableAnimation = true;
        public static ArrayList<MemoryAnimation> memoryAnimations = new ArrayList<>();
        public static ArrayList<MemoryDeleteAnimation> memoryDeleteAnimations = new ArrayList<>();
        public static ArrayList<SelectionDissapair> selectionDissapairs = new ArrayList<>();
        public static ArrayList<MoveAnimation> moveAnimations = new ArrayList<>();

    }


    public static ArrayList<Memory> found = new ArrayList<>();

    public static JWindow menu = null;

    public static class Interact {
        public static Point2D groupVector = new Point2D.Float(0, 0);

        public static Memory choose = null,
                prevChoose = null;
        public static ArrayList<Memory> groupSelection = new ArrayList<>();
        public static boolean pressed = false;
        public static Point2D prevPoint = null;
        public static Point2D moved = null;
        public static Point2D click = null;
        public static Point2D selectClick = null;

        public static boolean selectHold = false;
        public static Point2D selectStart = null;
        public static Point2D selectEnd = null;
        public static int mouseButtonNumber = 0;

        public static boolean lineHold = false;
        public static Memory selectLineStart = null;
        public static Point2D selectLineEnd = null;

        public static boolean eraseHold = false;
        public static Point2D eraseLineStart = null,
                               eraseLineEnd = null;

    }



    public Point2D getPanelCenter() {
        return new Point2D.Float(getSize().width/2, getSize().height/2);
    }
    public void camToMouse(Point2D mouse, double prevZ) {
        Point2D mustBe = reformMousePointToBlackboard(mouse);
        if (zoom>1) zoom=1;
        Point2D center = getPanelCenter();
        Point2D vector = new Point2D.Float((float) (center.getX()-mouse.getX()), (float) (center.getY()-mouse.getY()));
        //float fixer = (zoom>1)?1/zoom:10;
        //float oXC = xCam;
        //float oYC = yCam;
        //xCam+=vector.getX()/zoom/fixer;
        //yCam+=vector.getY()/zoom/fixer;
        Point2D current = reformMousePointToBlackboard(mouse);

        calibrateX(mouse, mustBe, current, false, prevZ);
        calibrateY(mouse, mustBe, current, false, prevZ);
    }
    public static Point2D mirrorPoint(Point2D point, Point2D center) {
        double dx = point.getX() - center.getX();
        double dy = point.getY() - center.getY();
        double mirroredX = center.getX() - dx;
        double mirroredY = center.getY() - dy;
        return new Point2D.Double(mirroredX, mirroredY);
    }
    public static double calculateDistance(double start, double end) {
        double distance = Math.abs(end - start);
        return distance;
    }
    public static Point2D calculateIntermediatePoint(Point2D start, Point2D end, double progress) {
        double deltaX = end.getX() - start.getX();
        double deltaY = end.getY() - start.getY();

        double intermediateX = start.getX() + (deltaX * progress);
        double intermediateY = start.getY() + (deltaY * progress);

        return new Point2D.Double(intermediateX, intermediateY);
    }
    private void calibrateX(Point2D mouse, Point2D mustBe, Point2D current, Boolean reversed, double prevZ) {
        Point2D screenCenter = reformMousePointToBlackboard(new Point2D.Float(this.getSize().width / 2, this.getSize().height / 2));
        var progress = calculateDistance(prevZ, zoom);
        if (reversed) {
            mustBe = mirrorPoint(mustBe, screenCenter);
            //mouse = mirrorPoint(mouse, screenCenter);
        }
        //System.out.println(mustBe);
        //System.out.println(screenCenter);
        //System.out.println(progress);
        mustBe = calculateIntermediatePoint(mustBe, screenCenter, progress);
        //System.out.println(mustBe);
        //int i = 0;
        while(true) {
            if (reversed) {

               // i++;
                //if (i>100)
                //    break;
            }
            screenCenter = reformMousePointToBlackboard(new Point2D.Float(this.getSize().width / 2, this.getSize().height / 2));
                //for(int i = 0; i < 100; i++) {
                if (mustBe.getX() > current.getX()) {
                    xCam -= 5 * zoom;
                } else {
                    xCam += 5 * zoom;
                }
                //current = reformMousePointToBlackboard(mouse);
                Point2D mCurrent = reformMousePointToBlackboard(mouse);
                //System.out.println(xCam + " " + yCam);
                //System.out.println(reversed);
                //System.out.println(mustBe + " " + current);
                //current = mCurrent;
                //current = getPointBetween(mCurrent, screenCenter);

            //current = getPointBetween(mCurrent, reformMousePointToBlackboard(mouse));
                current = screenCenter;
                if (Math.abs(mustBe.getX() - current.getX()) < 10)
                    break;

        }
        //System.out.println(screenCenter);
    }
    public static Point2D getPointBetween(Point2D point1, Point2D point2) {
        double x = (point1.getX() + point2.getX()) / 2;
        double y = (point1.getY() + point2.getY()) / 2;
        return new Point2D.Double(x, y);
    }
    private void calibrateY(Point2D mouse, Point2D mustBe, Point2D current, Boolean reversed, double prevZ) {
        Point2D screenCenter = reformMousePointToBlackboard(new Point2D.Float((float) (this.getSize().width / 2), (float) (this.getSize().height / 2)));
        //int i = 0;
        var progress = calculateDistance(prevZ, zoom);
        if (reversed) {
            mustBe = mirrorPoint(mustBe, screenCenter);
            //mouse = mirrorPoint(mouse, screenCenter);
        }
        //System.out.println(mustBe);
        //System.out.println(screenCenter);
        //System.out.println(progress);
        mustBe = calculateIntermediatePoint(mustBe, screenCenter, progress);
        //System.out.println(mustBe);
        while(true) {
            if (reversed) {
                //i++;
                //if (i>100)
                 //   break;
            }
            screenCenter = reformMousePointToBlackboard(new Point2D.Float(this.getSize().width / 2, this.getSize().height / 2));
                //for(int i = 0; i < 100; i++) {
                if (mustBe.getY() > current.getY()) {
                    yCam -= 5 * zoom;
                } else {
                    yCam += 5 * zoom;
                }
                //current = reformMousePointToBlackboard(mouse);
                Point2D mCurrent = reformMousePointToBlackboard(mouse);
                //System.out.println(xCam + " " + yCam);
                //System.out.println(reversed);
                //System.out.println(mustBe + " " + current);
                //current = getPointBetween(mCurrent, screenCenter);
                //current = mCurrent;
                current = screenCenter;
                if (Math.abs(mustBe.getY() - current.getY()) < 10)
                    break;

        }
        //System.out.println(screenCenter);
    }
    public void camFromMouse(Point2D mouse, double prevZ) {
        Point2D mustBe = reformMousePointToBlackboard(mouse);
        if (zoom>1) zoom=1;
        Point2D center = getPanelCenter();
        Point2D vector = new Point2D.Float((float) (center.getX()-mouse.getX()), (float) (center.getY()-mouse.getY()));
        float fixer = (zoom>1)?1/zoom:10;
        xCam-=vector.getX()/zoom/fixer;
        yCam-=vector.getY()/zoom/fixer;
        Point2D current = reformMousePointToBlackboard(mouse);
        calibrateX(mouse, mustBe, current, true, prevZ);
        calibrateY(mouse, mustBe, current, true, prevZ);
    }

    {

        setDoubleBuffered(true);
        addMouseWheelListener(mouseWheelEvent -> {
            if (menu!=null) {menu.setVisible(false); menu =null;}


            //zoom +=((float)mouseWheelEvent.getWheelRotation())/2;
            //if (zoom ==0) zoom =0.01f;
            //if (zoom <0) zoom *=-1;
            var point = reformMousePointToBlackboard(mouseWheelEvent.getPoint());
            //System.out.println(""+(xCam-point.getX())+" "+(yCam-point.getY()));
            var prevZ = zoom;
            if (!lineHold&&!eraseHold)
            if (((float)mouseWheelEvent.getWheelRotation())==1) {
                zoom+=0.05f*zoom;
                camToMouse(mouseWheelEvent.getPoint(), prevZ);
                //xCam=toValueWithZoom(xCam, (float) point.getX());
                //yCam=toValueWithZoom(yCam, (float) point.getY());
            } else {
                zoom-=0.05f*zoom;
                camFromMouse(mouseWheelEvent.getPoint(), prevZ);
                //xCam=fromValueWithZoom(xCam, (float) point.getX());
                //yCam=fromValueWithZoom(yCam, (float) point.getY());
            }
            repaint();

        });




        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if (menu!=null) {menu.setVisible(false); menu =null;}
                if (mouseButtonNumber==3) {
                    if ((IsClickToVoid(mouseEvent.getPoint())||selectStart!=null)&&!lineHold) {
                        if (!selectHold) {
                            selectStart = mouseEvent.getPoint();
                            selectHold = true;
                        }
                        selectEnd = mouseEvent.getPoint();
                    } else {

                        if (lineHold) {
                            selectLineEnd = mouseEvent.getPoint();
                        } else {
                            selectLineStart = getMemoryViaClick(mouseEvent.getPoint());
                            selectLineEnd = mouseEvent.getPoint();
                        }
                        lineHold = true;
                    }
                } else if (mouseButtonNumber==2&&IsClickToVoid(mouseEvent.getPoint())) {
                    if (eraseHold) {
                        eraseLineEnd = mouseEvent.getPoint();
                    } else {
                        eraseLineStart = mouseEvent.getPoint();
                        eraseLineEnd = mouseEvent.getPoint();
                    }
                    eraseHold = true;
                } else {
                    if (choose != null) {
                        try {
                            moveChoseToMouse(mouseEvent);
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (!groupSelection.isEmpty()) {
                        try {
                            moveGroupByMouse(mouseEvent);
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        moveCamByMouse(mouseEvent);
                    }
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                //if (menu!=null) {menu.setVisible(false); menu =null;}
                if (!pressed) {
                    moved = mouseEvent.getPoint();
                    prevPoint=null;
                }
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu!=null) {menu.setVisible(false); menu =null;}

                super.mouseClicked(e);

                if (e.getButton()==2&&choose!= null&&!IsClickToVoid(e.getPoint())) {
                    memDel();
                } else if(e.getButton()==2&&!groupSelection.isEmpty()) {
                    for (Memory memory : groupSelection) {
                        memory.setLinkedMemories(null);
                        for (Memory memory2 : data.getVersions().get(version_loaded).getData()) {
                            if (memory2.getLinkedMemories()==null) memory2.setLinkedMemories(new ArrayList<>());
                            memory2.getLinkedMemories().remove(memory);
                        }
                        Animation.memoryDeleteAnimations.add(new MemoryDeleteAnimation(memory));
                        data.getVersions().get(version_loaded).getData().remove(memory);
                    }
                    groupSelection=new ArrayList<>();
                } else if(e.getButton()==3&&IsClickToVoid(e.getPoint())) {
                    if (menu!=null) {menu.setVisible(false); menu =null;}
                    var menuL = new JWindow(window);
                    menu = menuL;
                    var panel = new UPanel();
                    menuL.setBackground(new Color(0, 0, 0, 0));
                    panel.setBackground(new Color(0, 0, 0, 0));
                    menuL.add(panel);

                    UButton cancel = new UButton("cancel"),
                            search = new UButton("search"),
                            clone = new UButton("clone"),
                            export = new UButton("export"),
                            importB = new UButton("import"),
                            delete = new UButton("delete"),
                            exec = new UButton("exec"),
                            script = new UButton("script"),
                            open = new UButton("open");
                    cancel.addActionListener(actionEvent -> {
                        menuL.setVisible(false);
                    });
                    search.addActionListener(actionEvent -> {
                        menuL.setVisible(false);
                        var sWindow = new JWindow(window);

                        var p = new UPanel();
                        sWindow.add(p);

                        var keyWords = new MainActivity.JTextField(16);
                        var keyWordsActive = new JCheckBox("search by keywords");
                        var keyWordsPanel = new UPanel();
                        keyWordsPanel.add(keyWords, keyWordsActive);
                        keyWords.setToolTipText("искомые фразы(через ;)");



                        var tags = new MainActivity.JTextField(16);
                        tags.setToolTipText("искомые теги(через ;)");
                        var tagsActive = new JCheckBox("search by tags");

                        var tagPanel = new UPanel();
                        tagPanel.add(tags, tagsActive);

                        var cPanel = new UPanel();
                        var colorB = new JButton();
                        colorB.setToolTipText("искомый цвет");
                        var cActive = new JCheckBox("use color as filter");
                        cPanel.add(colorB, cActive);
                        UButton find = new UButton("find"),
                                clean = new UButton("clean"),
                                cancelS = new UButton("cancel");
                        var choice = new UPanel();
                        choice.add(find, clean, cancelS);
                        cancelS.addActionListener(actionEvent1 -> {
                            sWindow.setVisible(false);
                        });
                        clean.addActionListener(actionEvent1 -> {
                            keyWords.setText("");
                            tags.setText("");
                            colorB.setBackground(new JButton().getBackground());

                            found = new ArrayList<>();
                            //setVisible(false);
                        });
                        colorB.addActionListener(actionEvent2 ->  {
                            class ColorChoose extends JDialog {
                                JColorChooser chooser = new JColorChooser();
                                public ColorChoose() {
                                    super();
                                    this.setSize(600, 400);
                                    setTitle("choose color");
                                    Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
                                    this.setLocation(new Point((ss.width-600)/2, (ss.height-400)/2));
                                    add(chooser);
                                    setBackground(Colors.bgColor);
                                    setVisible(true);
                                    addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosed(e);
                                            colorB.setBackground(chooser.getColor());
                                        }
                                    });
                                }
                            }
                            new ColorChoose();

                        });
                        find.addActionListener(actionEvent1 -> {
                            found = new ArrayList<>();
                            doNotAdd: for (Memory memory : data.getVersions().get(version_loaded).getData()) {
                                if (!keyWords.getText().equals(""))
                                    for (String phrase : keyWords.getText().split(";")) {
                                        if (!(memory.getDescription().contains(phrase)||memory.getName().contains(phrase))) continue doNotAdd;
                                    }
                                if (!tags.getText().equals(""))
                                    for (String phrase : tags.getText().split(";")) {
                                        if (!(memory.getTags().contains(phrase))) continue doNotAdd;
                                    }
                                if (!colorB.getBackground().equals(new JButton().getBackground())&&!memory.getColor().equals(colorB.getBackground())) continue;
                                found.add(memory);
                            }
                        });
                        p.add(keyWordsPanel, tagPanel, cPanel, choice);
                        sWindow.setSize(200, 120);
                        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
                        sWindow.setLocation(new Point((ss.width-200)/2, (ss.height-120)/2));
                        sWindow.setVisible(true);
                    });
                    clone.addActionListener(actionEvent -> {
                        menuL.setVisible(false);
                        if (!groupSelection.isEmpty()) {
                            cloneGroup();
                        }
                    });
                    export.addActionListener(actionEvent -> {
                        menuL.setVisible(false);
                        if (groupSelection!=null) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setMultiSelectionEnabled(false);
                            fileChooser.showDialog(window, "save");
                            if (fileChooser.getSelectedFile() != null) {
                                new Group(groupSelection).save(fileChooser.getSelectedFile().getAbsolutePath());
                            }
                        }
                    });
                    importB.addActionListener(actionEvent -> {
                        menuL.setVisible(false);
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setMultiSelectionEnabled(true);
                        fileChooser.showDialog(window, "load");
                        if (fileChooser.getSelectedFiles()!=null)
                            for (File file : fileChooser.getSelectedFiles()) {
                                try {
                                    data.getVersions().get(version_loaded).getData().addAll(new Group(file.getAbsolutePath()).memories);
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                            }
                        else if (fileChooser.getSelectedFile()!=null) {
                            try {
                                Group group = new Group(fileChooser.getSelectedFile().getAbsolutePath());
                                data.getVersions().get(version_loaded).getData().addAll(group.memories);
                                groupSelection = group.memories;
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    });
                    delete.addActionListener(actionEvent -> {
                        menuL.setVisible(false);
                        if(choose!=null) {
                            memDel();
                        } else if(!groupSelection.isEmpty()) {
                            for (Memory memory : groupSelection) {
                                memory.setLinkedMemories(null);
                                for (Memory memory2 : data.getVersions().get(version_loaded).getData()) {
                                    if (memory2.getLinkedMemories()==null) memory2.setLinkedMemories(new ArrayList<>());
                                    memory2.getLinkedMemories().remove(memory);
                                }
                                Animation.memoryDeleteAnimations.add(new MemoryDeleteAnimation(memory));
                                data.getVersions().get(version_loaded).getData().remove(memory);
                            }
                            groupSelection=new ArrayList<>();
                        }

                    });
                    exec.addActionListener(e1 -> {
                        menuL.setVisible(false);
                        var text = choose.getTagProperty("exec");
                        if (!text.equals("")) {
                            try {
                                Runtime.getRuntime().exec(text);
                            } catch (IOException ignored) {}
                        }
                        for (Memory memory : groupSelection) {
                            text = memory.getTagProperty("exec");
                            if (!text.equals("")) {
                                try {
                                    Runtime.getRuntime().exec(text);
                                } catch (IOException ignored) {}
                            }
                        }
                    });
                    open.addActionListener(e1 -> {
                        menuL.setVisible(false);
                        var text = choose.getName();
                        if (!text.equals("")) {
                            try {
                                File file = new File(choose.getName());
                                Desktop desktop = Desktop.getDesktop();

                                if (file.exists()) {
                                    desktop.open(file);
                                } else {
                                    System.out.println("File does not exist.");
                                }
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        for (Memory memory : groupSelection) {
                            text = memory.getName();
                            if (!text.equals("")) {
                                try {
                                    File file = new File(memory.getName());
                                    Desktop desktop = Desktop.getDesktop();

                                    if (file.exists()) {
                                        desktop.open(file);
                                    } else {
                                        System.out.println("File does not exist.");
                                    }
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                            }
                        }
                    });
                    script.addActionListener(e1 -> {
                        menuL.setVisible(false);
                        boolean scriptB = false;
                        try {
                            scriptB = Boolean.parseBoolean(choose.getTagProperty("script"));
                        } catch (Exception e3) {}
                        if (scriptB) {
                            Common.run(choose.getDescription());
                        }
                        for (Memory memory : groupSelection) {
                            scriptB = false;
                            try {
                                scriptB = Boolean.parseBoolean(memory.getTagProperty("script"));
                            } catch (Exception e3) {}
                            if (scriptB) {
                                Common.run(memory.getDescription());
                            }
                        }
                    });
                    panel.add(exec, script, open, clone, delete, search, importB, export, cancel);
                    menuL.setSize(45, 178);
                    menuL.setLocation(MouseInfo.getPointerInfo().getLocation().x-5, MouseInfo.getPointerInfo().getLocation().y-10);

                    menuL.setVisible(true);
                } else if(e.getButton()==3)
                        selectClick=e.getPoint();
                    else
                        click=e.getPoint();
            }

            private void cloneGroup() {
                ArrayList<Memory> clones = new ArrayList<>();
                HashMap<Memory, Memory> oldToNew = new HashMap<>();
                for(Memory memory : groupSelection) {
                    Memory cloned = new Memory(memory.getName(), (ArrayList<String>) memory.getTags().clone(), memory.getDescription(), new Point2D.Float((float) memory.getPoint().getX(), (float) memory.getPoint().getY()), new Color(memory.getColor().getRGB()), (ArrayList<Memory>) memory.getLinkedMemories().clone());
                    clones.add(cloned);
                    oldToNew.put(memory, cloned);
                }
                for(Memory cloned : clones) {
                    ArrayList<Memory> copiedLinks = new ArrayList<>();
                    for (Memory link : cloned.getLinkedMemories()) {
                        if (oldToNew.get(link)!=null) {
                            copiedLinks.add(oldToNew.get(link));
                        }
                    }
                    groupSelection = clones;
                    cloned.setLinkedMemories(copiedLinks);
                }

                data.getVersions().get(version_loaded).getData().addAll(clones);
            }

            private void exportGroup(String filename) {
                ArrayList<Memory> clones = new ArrayList<>();
                HashMap<Memory, Memory> oldToNew = new HashMap<>();
                for(Memory memory : groupSelection) {
                    Memory cloned = new Memory(memory.getName(), (ArrayList<String>) memory.getTags().clone(), memory.getDescription(), new Point2D.Float((float) memory.getPoint().getX(), (float) memory.getPoint().getY()), new Color(memory.getColor().getRGB()), (ArrayList<Memory>) memory.getLinkedMemories().clone());
                    clones.add(cloned);
                    oldToNew.put(memory, cloned);
                }
                for(Memory cloned : clones) {
                    ArrayList<Memory> copiedLinks = new ArrayList<>();
                    for (Memory link : cloned.getLinkedMemories()) {
                        if (oldToNew.get(link)!=null) {
                            copiedLinks.add(oldToNew.get(link));
                        }
                    }
                    //groupSelection = clones;
                    cloned.setLinkedMemories(copiedLinks);
                }
                new Group(clones).save(filename);
                //data.getVersions().get(version_loaded).getData().addAll(clones);
            }
            private void importGroup(String filename) {
                Group group = new Group(filename);
                if (group.memories!=null)
                    data.getVersions().get(version_loaded).getData().addAll(group.memories);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (menu!=null) {menu.setVisible(false); menu =null;}

                pressed=true;
                mouseButtonNumber = e.getButton();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (menu!=null) {menu.setVisible(false); menu =null;}

                pressed=false;
            }
        });
    }

    private void memDel() {
        Animation.memoryDeleteAnimations.add(new MemoryDeleteAnimation(choose));
        data.getVersions().get(version_loaded).getData().remove(choose);
        choose.setLinkedMemories(null);
        for (Memory memory : data.getVersions().get(version_loaded).getData()) {
            memory.getLinkedMemories().remove(choose);
        }
        groupSelection.remove(choose);
        click=null;
        prevChoose =null;
        choose=null;
    }

    public void moveChoseToMouse(MouseEvent mouseEvent) throws CloneNotSupportedException {
        Animation.moveAnimations.add(new MoveAnimation(choose));
        float x = ((float)mouseEvent.getPoint().getX()-xCam)/ zoom *2;
        //float x = ((float)mouseEvent.getPoint().getX()-xCam)/ zoom *2-((float) DEFAULT_RADIUS/2);
        float y = ((float)mouseEvent.getPoint().getY()-yCam)/ zoom *2;
        //float y = ((float)mouseEvent.getPoint().getY()-yCam)/ zoom *2-((float) DEFAULT_RADIUS/2);
        choose.setPoint(new Point2D.Float(x, y));
        MainActivity.East.xPos.setText(String.valueOf((int)choose.getPoint().getX()));
        MainActivity.East.yPos.setText(String.valueOf((int)choose.getPoint().getY()));
    }
    public static Color createColorWithTransparency(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    private void moveCamByMouse(MouseEvent mouseEvent) {
        if(!eraseHold&&!lineHold) {
            if (prevPoint == null)
                prevPoint = mouseEvent.getPoint();
            Point2D current = mouseEvent.getPoint();
            //speedVector.setLocation(speedVector.getX() + (current.getX() - prevPoint.getX()) / zoom, speedVector.getY() + (current.getY() - prevPoint.getY()) / zoom);





            speedVector.setLocation(speedVector.getX() + (current.getX() - prevPoint.getX()) / zoom, speedVector.getY() + (current.getY() - prevPoint.getY()) / zoom);
            prevPoint = current;
        } else {
            prevPoint = null;
        }
    }
    private void moveGroupByMouse(MouseEvent mouseEvent) throws CloneNotSupportedException {

        if (prevPoint==null)
            prevPoint = mouseEvent.getPoint();
        Point2D current = mouseEvent.getPoint();
        groupVector.setLocation((groupVector.getX()+(current.getX()-prevPoint.getX())/ zoom), (groupVector.getY()+(current.getY()-prevPoint.getY())/ zoom));
        GroupMoving.speedVector.setLocation(speedVector.getX()+(current.getX()-prevPoint.getX())/ zoom, speedVector.getY()+(current.getY()-prevPoint.getY())/ zoom);
        for (Memory memory : groupSelection) {
            Animation.moveAnimations.add(new MoveAnimation(memory));
        }
        GroupMoving.compute();
        //oldCompute();
        prevPoint = current;
    }
    public void oldCompute() throws CloneNotSupportedException {
        if (groupVector.getX() < 0.25f & groupVector.getX() > -0.25f) {
            groupVector.setLocation(0f, 0f);
        } else {
            if (groupVector.getX() < 0) groupVector.setLocation(groupVector.getX() / 1.15, groupVector.getY());
            if (groupVector.getX() > 0) groupVector.setLocation(groupVector.getX() / 1.15, groupVector.getY());
            if (groupVector.getY() < 0) groupVector.setLocation(groupVector.getX(), groupVector.getY() / 1.15);
            if (groupVector.getY() > 0) groupVector.setLocation(groupVector.getX(), groupVector.getY() / 1.15);
        }
        if (groupVector.getX() > 10) groupVector.setLocation(9, groupVector.getY());
        if (groupVector.getX() < -10) groupVector.setLocation(-9, groupVector.getY());
        if (groupVector.getY() > 10) groupVector.setLocation(groupVector.getX(), 9);
        if (groupVector.getY() < -10) groupVector.setLocation(groupVector.getX(), -9);
        for (Memory memory : groupSelection) {
            Animation.moveAnimations.add(new MoveAnimation(memory));
            memory.setPoint(new Point2D.Float((float) ((float) memory.getPoint().getX()+groupVector.getX()), (float) ((float) memory.getPoint().getY()+groupVector.getY())));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    float getRange(Point2D p1, Point2D p2) {
        float result = ((float) Math.abs(p1.getX()- p2.getX()))+((float) Math.abs(p1.getY()- p2.getY()));
        return result;
    }

    public void drawBackground(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Colors.bgColor);
        g2.fillRect(0, 0, window.getWidth(), window.getHeight());
    }
    public static float computeRadius(Memory memory){
        return DEFAULT_RADIUS* zoom +(memory.getDescription().length()+memory.getName().length())/150*zoom;
    }


    public boolean IsIntersecting(Point a, Point b, Point c, Point d)
    {
        float denominator = (float) (((b.getX() - a.getX()) * (d.getY() - c.getY())) - ((b.getY() - a.getY()) * (d.getX() - c.getX())));
        float numerator1 = (float) (((a.getY() - c.getY()) * (d.getX() - c.getX())) - ((a.getX() - c.getX()) * (d.getY() - c.getY())));
        float numerator2 = (float) (((a.getY() - c.getY()) * (b.getX() - a.getX())) - ((a.getX() - c.getX()) * (b.getY() - a.getY())));

        // Detect coincident lines (has a problem, read below)
        if (denominator == 0) return numerator1 == 0 && numerator2 == 0;

        float r = numerator1 / denominator;
        float s = numerator2 / denominator;

        return (r >= 0 && r <= 1) && (s >= 0 && s <= 1);
    }

    public boolean IsClickToVoid() {
        boolean nothing = true;
        for (Memory memory : Common.data.getVersions().get(Common.version_loaded).getData()) {
            float radius = computeRadius(memory);
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()* zoom +xCam-(memory.getPoint().getX()* zoom /2))-radius/2, (float) ((float) memory.getPoint().getY()* zoom +yCam-(memory.getPoint().getY()* zoom /2))-radius/2, radius, radius);
            if (click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                nothing = false;
            }
        }
        return nothing;
    }
    public boolean IsClickToVoid(Point2D click) {
        boolean nothing = true;
        for (Memory memory : Common.data.getVersions().get(Common.version_loaded).getData()) {
            float radius = computeRadius(memory);
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()* zoom +xCam-(memory.getPoint().getX()* zoom /2))-radius/2, (float) ((float) memory.getPoint().getY()* zoom +yCam-(memory.getPoint().getY()* zoom /2))-radius/2, radius, radius);
            if (click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                nothing = false;
            }
        }
        return nothing;
    }

    public Memory getMemoryViaClick(Point2D click) {
        for (Memory memory : Common.data.getVersions().get(Common.version_loaded).getData()) {
            float radius = computeRadius(memory);
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()* zoom +xCam-(memory.getPoint().getX()* zoom /2))-radius/2, (float) ((float) memory.getPoint().getY()* zoom +yCam-(memory.getPoint().getY()* zoom /2))-radius/2, radius, radius);
            if (click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                return memory;
            }
        }
        return null;
    }

    public Point2D reformMousePointToBlackboard(Point2D point) {
        float x = ((float) point.getX() - xCam) / zoom * 2 - ((float) DEFAULT_RADIUS);
        float y = ((float) point.getY() - yCam) / zoom * 2 - ((float) DEFAULT_RADIUS);
        return new Point2D.Float(x, y);
    }
    public Point2D reformClickToBlackboard() {
        float x = ((float) click.getX() - xCam) / zoom * 2 - ((float) DEFAULT_RADIUS);
        float y = ((float) click.getY() - yCam) / zoom * 2 - ((float) DEFAULT_RADIUS);
        return new Point2D.Float(x, y);
    }

    public void createEmptyMemoryByClick() {
        choose = new Memory("", reformClickToBlackboard());
        choose.getTags().add(String.valueOf(System.currentTimeMillis()));
        choose.getTags().add("\nwidth");
        choose.getTags().add("\nheight");
        choose.getTags().add("\nnoDrawfalse");
        choose.getTags().add("\nnoDrawTextfalse");
        choose.getTags().add("\nexec");
        choose.getTags().add("\npage0");
        choose.getTags().add("\nscriptfalse");
        choose.getTags().add("\ndescDrawfalse");
        choose.getTags().add("\nclip"+0);
        Animation.memoryAnimations.add(new MemoryAnimation(choose));
        //Common.data.getVersions().get(Common.version_loaded).getData().
        //        add(choose);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        paint(g2);
    }

    public void paint(Graphics2D g2) {
        drawBackground(g2);
        for (Memory memory : data.getVersions().get(version_loaded).getData()) {
            float radius = computeRadius(memory);
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()* zoom +xCam-(memory.getPoint().getX()* zoom /2))-radius/2, (float) ((float) memory.getPoint().getY()* zoom +yCam-(memory.getPoint().getY()* zoom /2))-radius/2, radius, radius);
            ArrayList<Memory> onErasing = new ArrayList<>();
            if (memory.getLinkedMemories()==null) memory.setLinkedMemories(new ArrayList<>());
            for (Memory link : memory.getLinkedMemories()) {
                    float radius2 = computeRadius(link);
                    Ellipse2D ellipse2 = new Ellipse2D.Float((float) ((float) link.getPoint().getX() * zoom + xCam - (link.getPoint().getX() * zoom / 2))-radius/2, (float) ((float) link.getPoint().getY() * zoom + yCam - (link.getPoint().getY() * zoom / 2))-radius/2, radius2, radius2);
                    //if (moved!=null&&areLineHitted(moved, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()), new Point2D.Float((float) ellipse2.getCenterX(), (float) ellipse2.getCenterY())))
                        //g2.setColor(Color.white);
                    //else
                    if (eraseHold&&IsIntersecting((Point) eraseLineStart, (Point) eraseLineEnd, new Point((int) ellipse.getCenterX(), (int) ellipse.getCenterY()), new Point((int) ellipse2.getCenterX(), (int) ellipse2.getCenterY())))
                        g2.setColor(Colors.eraseColor);
                    else if (!pressed&&eraseLineStart!=null&&eraseLineEnd!=null&&IsIntersecting((Point) eraseLineStart, (Point) eraseLineEnd, new Point((int) ellipse.getCenterX(), (int) ellipse.getCenterY()), new Point((int) ellipse2.getCenterX(), (int) ellipse2.getCenterY())))
                        onErasing.add(link);
                    else
                        g2.setColor(memory.getColor());
                    g2.drawLine((int) ellipse.getCenterX(), (int) ellipse.getCenterY(), (int) ellipse2.getCenterX(), (int) ellipse2.getCenterY());
                }
            memory.getLinkedMemories().removeAll(onErasing);
        }
        if (!pressed) lineHold=false;
        if (eraseHold) {
            g2.setColor(Colors.eraseColor);
            g2.drawLine((int) eraseLineStart.getX(), (int) eraseLineStart.getY(), (int) eraseLineEnd.getX(), (int) eraseLineEnd.getY());
        } else {
            if (eraseLineEnd!=null&&eraseLineStart!=null) {
                eraseLineStart=null;
                eraseLineEnd=null;
            }
        }
        if (!pressed) eraseHold=false;
        if(lineHold) {
            float radius = computeRadius(selectLineStart);
            Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) selectLineStart.getPoint().getX()* zoom +xCam-(selectLineStart.getPoint().getX()* zoom /2))-radius/2, (float) ((float) selectLineStart.getPoint().getY()* zoom +yCam-(selectLineStart.getPoint().getY()* zoom /2))-radius/2, radius, radius);
            Memory selected = getMemoryViaClick(selectLineEnd);

            if (selected==null) {
                g2.setColor(Colors.select);
                g2.drawLine((int) ellipse.getCenterX(), (int) ellipse.getCenterY(), (int) selectLineEnd.getX(), (int) selectLineEnd.getY());
            } else {
                Ellipse2D ellipse2 = new Ellipse2D.Float((float) ((float) selected.getPoint().getX()* zoom +xCam-(selected.getPoint().getX()* zoom /2))-radius/2, (float) ((float) selected.getPoint().getY()* zoom +yCam-(selected.getPoint().getY()* zoom /2))-radius/2, radius, radius);
                g2.setColor(Colors.eraseColor);
                g2.drawLine((int) ellipse.getCenterX(), (int) ellipse.getCenterY(), (int) ellipse2.getCenterX(), (int) ellipse2.getCenterY());
            }
        } else {
            Memory selected = getMemoryViaClick(selectLineEnd);
            if (selected!=null) {
                //if (selectLineStart.getLinkedMemories()==null) selectLineStart.setLinkedMemories(new ArrayList<>());
                selectLineStart.getLinkedMemories().add(selected);
            }
            selected = null;
            selectLineEnd = null;
            selectLineStart = null;
        }
        ifClickToVoidCreateEmptyMemory();
        drawMemories(g2);
        drawSelectionField(g2);
    }


    private void ifClickToVoidCreateEmptyMemory() {
        if(click!=null) {
            boolean nothing = IsClickToVoid();
            if (nothing&&groupSelection.isEmpty()) {
                prevChoose = choose;
                writeChangesToChoose();
                createEmptyMemoryByClick();
                writeChooseToPanel();
            } else if (nothing) {
                groupSelection= new ArrayList<>();
                click=null;
            }

        }
    }

    private void drawSelectionField(Graphics2D g2) {
        if (selectHold) {
            Rectangle2D rect = new Rectangle2D.Float();
            rect.setRect(selectStart.getX(), selectStart.getY(), selectEnd.getX()-selectStart.getX(), selectEnd.getY()-selectStart.getY());
            g2.setColor(Colors.groupSelect);
            g2.fill(rect);
            g2.setColor(Colors.select);
            g2.draw(rect);
        } else {
            Animation.selectionDissapairs
                    .add(new SelectionDissapair(selectStart, selectEnd));
            selectStart = null;
            selectEnd = null;
        }
        if (!pressed) {
            selectHold=false;
        }
        ArrayList<SelectionDissapair> selectionDissapairsRemove = new ArrayList<>();
        for (SelectionDissapair selectionDissapair : Animation.selectionDissapairs) {
            if(!selectionDissapair.draw(g2)) selectionDissapairsRemove.add(selectionDissapair);
        }
        Animation.selectionDissapairs.removeAll(selectionDissapairsRemove);

        ArrayList<MemoryAnimation> memoryAnimationsRemove = new ArrayList<>();
        for (MemoryAnimation memoryAnimation : Animation.memoryAnimations) {
            if(!memoryAnimation.draw(g2)) memoryAnimationsRemove.add(memoryAnimation);
        }
        Animation.memoryAnimations.removeAll(memoryAnimationsRemove);
        data.getVersions().get(version_loaded).getData().addAll(memoryAnimationsRemove);
        if (!memoryAnimationsRemove.isEmpty())
            choose = memoryAnimationsRemove.get(memoryAnimationsRemove.size()-1);

        ArrayList<MemoryDeleteAnimation> memoryDeleteAnimations = new ArrayList<>();
        for (MemoryDeleteAnimation memoryDeleteAnimation : Animation.memoryDeleteAnimations) {
            if(!memoryDeleteAnimation.draw(g2)) memoryDeleteAnimations.add(memoryDeleteAnimation);
        }
        Animation.memoryDeleteAnimations.removeAll(memoryDeleteAnimations);


        ArrayList<MoveAnimation> moveAnimationsRemove = new ArrayList<>();
        for (MoveAnimation moveAnimation : Animation.moveAnimations) {
            if(!moveAnimation.draw(g2)) moveAnimationsRemove.add(moveAnimation);
        }
        Animation.moveAnimations.removeAll(moveAnimationsRemove);
    }

    private void drawMemories(Graphics2D g2) {
        for (Memory memory : Common.data.getVersions().get(Common.version_loaded).getData()) {
            drawMemory(memory, g2);
        }
    }
    private void drawMemory(Memory memory, Graphics2D g2) {
        float radius = computeRadius(memory);
        Ellipse2D ellipse = new Ellipse2D.Float((float) ((float) memory.getPoint().getX()* zoom +xCam-(memory.getPoint().getX()* zoom /2))-radius/2, (float) ((float) memory.getPoint().getY()* zoom +yCam-(memory.getPoint().getY()* zoom /2))-radius/2, radius, radius);
        g2.setColor(memory.getColor());
        boolean descDraw = false;
        try {
            descDraw = Boolean.parseBoolean(memory.getTagProperty("descDraw"));
        } catch (Exception e) {}
        boolean noDraw = false;
        try {
            noDraw = Boolean.parseBoolean(memory.getTagProperty("noDraw"));
        } catch (Exception e) {}
        boolean noDrawText = false;
        try {
            noDrawText = Boolean.parseBoolean(memory.getTagProperty("noDrawText"));
        } catch (Exception e) {}

        if (found.contains(memory)) g2.drawLine((int) ellipse.getCenterX(), (int) ellipse.getCenterY(), this.getWidth()/2, this.getHeight()/2);
        if (!noDraw)
        g2.fill(ellipse);
        if (choose !=null&&click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
            prevChoose = choose;
            choose = null;
            click = null;

            if (prevChoose !=null) {
                writeChangesToChoose();
            }
        }
        if (memory!= choose)
            if (click!=null&&getRange(click, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                writeChangesToChoose();
                choose = memory;
                writeChooseToPanel();
            } else if (selectClick!=null&&getRange(selectClick, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) {
                selectClick=null;
                if (groupSelection.contains(memory))
                    groupSelection.remove(memory);
                else
                    groupSelection.add(memory);
            }
            else if (moved!=null&&getRange(moved, new Point2D.Float((float) ellipse.getCenterX(), (float) ellipse.getCenterY()))<=radius/1.5) g2.setColor(Colors.mouseOver);
            else g2.setColor(new Color((memory.getColor().getRed()+Colors.border_color.getRed())/2, (memory.getColor().getGreen()+Colors.border_color.getGreen())/2, (memory.getColor().getBlue()+Colors.border_color.getBlue())/2));
            else {g2.setColor(Colors.select); if (!noDraw) g2.fill(ellipse); g2.setColor(Colors.color);}
        if (groupSelection.contains(memory)) {g2.setColor(Colors.groupSelect); if (!noDraw) g2.fill(ellipse);g2.setColor(memory.getColor());}
        if (!noDraw)
        g2.draw(ellipse);
        if (selectHold)
        if (ellipse.getCenterX()>selectStart.getX()&&ellipse.getCenterY()>selectStart.getY()) {
            if (ellipse.getCenterX()<selectEnd.getX()&&ellipse.getCenterY()<selectEnd.getY()) {
                g2.setColor(Colors.groupSelectHold);
                if (!noDraw)
                g2.fill(ellipse);
                g2.setColor(Colors.groupSelect);
                g2.draw(ellipse);
                if(!pressed) {
                    if (groupSelection.contains(memory)) groupSelection.remove(memory);
                    else groupSelection.add(memory);
                }
            }
        }
        if (zoom>0.2f||(memory.getDescription().length()>120&&zoom>0.05)||(memory.getDescription().length()>1000&&zoom>0.005)) {
            g2.setColor(Colors.textColor);
            if (new File(memory.getName()).exists()) {
                var f = new File(memory.getName());

                FileSystemView fileSystemView = FileSystemView.getFileSystemView();

                int width;
                int height;
                try {
                    width = Integer.parseInt(memory.getTagProperty("width"));
                } catch (Exception e) {
                    width = (int) radius;
                }
                try {
                    height = Integer.parseInt(memory.getTagProperty("height"));
                } catch (Exception e) {
                    height = (int) radius;
                }
                var clip = g2.getClip();
                if (memory.getTagProperty("clip").equals("1"))
                    g2.setClip(ellipse);
                if (memory.getImg()==null) {

                    ImageIcon icon = (ImageIcon) fileSystemView.getSystemIcon(f);
                    memory.setImg(icon.getImage());
                    //System.out.println(icon.getIconHeight());

                    //g2.drawImage(icon.getImage(), (int) ellipse.getCenterX(), (int) ellipse.getCenterY(), (int) radius, (int) radius, null);
                    g2.drawImage(icon.getImage(), (int) ellipse.getX(), (int) ellipse.getY(), width, height, null);
                    try {
                        Image img = ImageIO.read(f);
                        g2.drawImage(img, (int) ellipse.getX(), (int) ellipse.getY(), width, height, null);
                        memory.setImg(img);
                    } catch (IOException e) {

                    }


                } else {
                    g2.drawImage(memory.getImg(), (int) ellipse.getX(), (int) ellipse.getY(), width, height, null);
                }
                g2.setClip(clip);
                String text = f.getName();
                if (!noDrawText)
                g2.drawString(text, (int) ( ellipse.getCenterX()-zoom*text.length()-text.length()*3.25-1/zoom), (int) ( ellipse.getCenterY()-78*zoom));

            } else if(!memory.getName().startsWith("=")) {
                if (!noDrawText)
                    g2.drawString(memory.getName(), (int) (ellipse.getCenterX() - zoom * memory.getName().length() - memory.getName().length() * 3.25 - 1 / zoom), (int) (ellipse.getCenterY() - 78 * zoom));
            }else {
                if (!noDrawText) {
                    String text = Common.calculate(memory.getName().replace("=", ""), memory);
                    g2.drawString(text, (int) (ellipse.getCenterX() - zoom * text.length() - text.length() * 3.25 - 1 / zoom), (int) (ellipse.getCenterY() - 78 * zoom));
                }
                }
            if (descDraw) {
                var strings = memory.getDescription().split("\n");
                g2.setColor(memory.getColor());
                for (int i = 0; i < strings.length; i++) {
                    g2.drawString(strings[i], (int) (ellipse.getCenterX() - zoom * strings[i].length() - memory.getName().length()*3.25 - 1 / zoom), (int) (ellipse.getCenterY() - 78 * zoom+16+i*16));
                }
            }
        }
    }

    public void writeChangesToChoose() {
        if (prevChoose != null) {
            prevChoose.setName(MainActivity.East.name.getText());
            JTextArea area = MainActivity.East.area;
            prevChoose.setDescription(area.getText());
            var data = MainActivity.East.tags.getModel();
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < data.getSize(); i++) {
                list.add((String) data.getElementAt(i));
            }
            prevChoose.setTags(list);
            prevChoose.setColor(MainActivity.East.color.getBackground());
            prevChoose.setPoint(new Point2D.Float(Float.parseFloat(MainActivity.East.xPos.getText()), Float.parseFloat(MainActivity.East.yPos.getText())));
        }
    }

    public void writeChooseToPanel() {
            JTextArea area = MainActivity.East.area;
            MainActivity.East.name.grabFocus();
            MainActivity.East.name.setText(choose.getName());
            MainActivity.East.xPos.setText(String.valueOf((int)choose.getPoint().getX()));
            MainActivity.East.yPos.setText(String.valueOf((int)choose.getPoint().getY()));
            MainActivity.East.color.setBackground(choose.getColor());
            JList<String> tags = MainActivity.East.tags;
            tags.setListData(choose.getTags().toArray(new String[choose.getTags().size()]));
            area.setText(choose.getDescription());
            click = null;
    }
}
