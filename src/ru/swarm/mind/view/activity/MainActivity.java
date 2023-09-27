package ru.swarm.mind.view.activity;

import ru.swarm.mind.Common;
import ru.swarm.mind.control.DrawPerTick;
import ru.swarm.mind.model.EventVersion;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.component.DrawPanel;
import ru.swarm.mind.view.component.UButton;
import ru.swarm.mind.view.component.UPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

import static java.awt.BorderLayout.*;
import static ru.swarm.mind.Common.*;
import static ru.swarm.mind.view.component.DrawPanel.Interact.*;
//import static ru.swarm.mind.view.component.DrawPanel.choose;

public class MainActivity extends JRootPane {

    public static DrawPanel draw    = new DrawPanel();
    public static javax.swing.JLabel info  = new javax.swing.JLabel();
    static {info.setForeground(Colors.textColor);activities.put("MainActivity", new MainActivity());}


    static class JLabel extends javax.swing.JLabel {

        public JLabel(String text) {
            setText(text);
            setForeground(Colors.textColor);
        }
    }

    static class JScrollPane extends javax.swing.JScrollPane {
        //public static Color background = new Color(50, 37,40);
        public static Color background = Colors.color;
        {
            setBackground(background);
            var h = getHorizontalScrollBar();
            h.setBackground(background);
            h.setBorder(new LineBorder(Colors.border_color));
            var v = getVerticalScrollBar();
            v.setBackground(background);
            v.setBorder(new LineBorder(Colors.border_color));
            setBorder(new LineBorder(Colors.border_color));
        }

        public JScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
            super(view, vsbPolicy, hsbPolicy);
        }

        public JScrollPane(Component view) {
            super(view);
        }

        public JScrollPane(int vsbPolicy, int hsbPolicy) {
            super(vsbPolicy, hsbPolicy);
        }

        public JScrollPane() {
            super();
        }
    }

    public static class JList<T> extends javax.swing.JList {
        private void copyToClipboard(String text) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(text);
            clipboard.setContents(selection, null);
        }
        {
            setBackground(JTextArea.background);
            setForeground(Colors.textColor);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        String selectedItem = (String) getSelectedValue();
                        if (selectedItem != null) {
                            copyToClipboard(selectedItem);
                        }
                    }
                }
            });
        }

        public JList(ListModel dataModel) {
            super(dataModel);
        }

        public JList(Object[] listData) {
            super(listData);
        }

        public JList(Vector listData) {
            super(listData);
        }

        public JList() {
            super();
        }
    }

    public static class JTextField extends javax.swing.JTextField {

        {
            setBackground(JScrollPane.background);
            setForeground(Colors.textColor);
            setBorder(new LineBorder(Colors.border_color));
        }

        public JTextField() {
            super();
        }

        public JTextField(String text) {
            super(text);
        }

        public JTextField(int columns) {
            super(columns);
        }

        public JTextField(String text, int columns) {
            super(text, columns);
        }

        public JTextField(Document doc, String text, int columns) {
            super(doc, text, columns);
        }
    }

    static class JTextArea extends javax.swing.JTextArea {
        private static Color background = Colors.bgColor;
        {
            setBackground(background);
            setForeground(Colors.textColor);
        }


    }

    public static class East extends UPanel {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel colorL = new JLabel("color"),
                descriptionL = new JLabel("description"),
                yL = new JLabel("y"),
                tagsL = new JLabel("tags"),
                xL = new JLabel("x"),
                nameL = new JLabel("name");
        public static JTextArea area = new JTextArea();
        JScrollPane scroll = new JScrollPane(area);
        UButton done = new UButton("done"),
                cancel = new UButton("cancel"),
                edit = new UButton("edit tags"),
                editD = new UButton("description");
        public static JTextField name = new JTextField(12),
                xPos = new JTextField(5),
                yPos = new JTextField(5);

        public static JList<String> tags = new JList<>();
        static JScrollPane tagsPane = new JScrollPane(tags);
        public static JButton color = new JButton("");
        {


            setLayout(new GridBagLayout());

            scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.setPreferredSize(new Dimension(250, 250));
            //scroll.setSize(new Dimension(400, 200));

            tagsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            tagsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            tagsPane.setPreferredSize(new Dimension(250, 150));

            gbc.ipadx = 5;
            gbc.ipady = 5;
            gbc.gridwidth = 225;

            gbc.gridx = 1;
            gbc.gridy = 1;
            add(colorL, gbc);
            gbc.gridx = 2;
            gbc.gridy = 1;
            add(color, gbc);
            gbc.gridx = 1;
            gbc.gridy = 2;
            add(nameL, gbc);
            gbc.gridx = 2;
            gbc.gridy = 2;
            add(name, gbc);
            gbc.gridx = 1;
            gbc.gridy = 3;
            add(xL, gbc);
            gbc.gridx = 2;
            gbc.gridy = 3;
            add(xPos, gbc);
            gbc.gridx = 1;
            gbc.gridy = 4;
            add(yL, gbc);
            gbc.gridx = 2;
            gbc.gridy = 4;
            add(yPos, gbc);
            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.gridx = 2;
            gbc.gridy = 5;
            add(new javax.swing.JLabel(), gbc);
            add(descriptionL, gbc);
            gbc.gridx = 1;
            gbc.gridy = 6;
            gbc.gridx = 2;
            gbc.gridy = 6;
            add(new javax.swing.JLabel(), gbc);
            add(scroll, gbc);
            gbc.gridx = 1;
            gbc.gridy = 7;
            gbc.gridx = 2;
            gbc.gridy = 7;
            add(new javax.swing.JLabel(), gbc);
            add(tagsL, gbc);
            gbc.gridx = 1;
            gbc.gridy = 8;
            gbc.gridx = 2;
            gbc.gridy = 8;
            add(new javax.swing.JLabel(), gbc);
            gbc.gridwidth = 225;
            add(tagsPane, gbc);
            UPanel line = new UPanel();
            UPanel line2 = new UPanel();
            line.setBorder(null);
            line.add(cancel);
            line2.setBorder(null);
            line2.add(edit);
            line2.add(editD);

            edit.addActionListener(actionEvent -> {
                if (choose!=null) {
                    draw.writeChangesToChoose();
                    JDialog frame = new JDialog(window, "tags");
                    frame.setSize(200, 300);
                    Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
                    frame.setLocation(new Point((ss.width-200)/2, (ss.height-300)/2));
                    frame.setVisible(true);
                    frame.setResizable(false);
                    JTextArea area1 = new JTextArea();
                    frame.add(area1);
                    for (String tag : choose.getTags()) {
                        area1.setText(area1.getText()+tag+";");
                    }
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            ArrayList<String> tagsL = new ArrayList<>(java.util.List.of(area1.getText().split(";")));
                            choose.setTags(tagsL);
                            draw.writeChooseToPanel();
                        }
                    });
                }
            });
            editD.addActionListener(actionEvent -> {
                if (choose!=null) {
                    draw.writeChangesToChoose();
                    JDialog frame = new JDialog(window, "description");
                    frame.setSize(600, 700);
                    Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
                    frame.setLocation(new Point((ss.width-600)/2, (ss.height-700)/2));
                    frame.setVisible(true);
                    frame.setResizable(false);
                    JTextArea area1 = new JTextArea();
                    JScrollPane scrollPane = new JScrollPane(area1);
                    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    frame.add(scrollPane);
                    area1.setText(choose.getDescription());
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            choose.setDescription(area1.getText());
                            draw.writeChooseToPanel();
                        }
                    });
                }
            });
            cancel.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    javax.swing.JTextArea area = MainActivity.East.area;
                    MainActivity.East.name.setText(choose.getName());
                    MainActivity.East.xPos.setText(String.valueOf(choose.getPoint().getX()));
                    MainActivity.East.yPos.setText(String.valueOf(choose.getPoint().getY()));
                    color.setBackground(choose.getColor());
                    javax.swing.JList<String> tags = MainActivity.East.tags;
                    tags.setListData(choose.getTags().toArray(new String[choose.getTags().size()]));
                    area.setText(choose.getDescription());
                }
            });
            line.add(done);

            done.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    choose.setName(name.getText());
                    choose.setDescription(area.getText());
                    var data = tags.getModel();
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < data.getSize(); i++) {
                        list.add((String) data.getElementAt(i));
                    }
                    choose.setTags(list);
                    choose.setColor(color.getBackground());
                    choose.setPoint(new Point2D.Float(Float.parseFloat(xPos.getText()), Float.parseFloat(yPos.getText())));
                }
            });
            gbc.gridx = 2;
            gbc.gridy = 9;
            add(line, gbc);
            gbc.gridx = 2;
            gbc.gridy = 10;
            add(line2, gbc);
            color.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    class ColorChoose extends JDialog {
                        JColorChooser chooser = new JColorChooser();
                        public ColorChoose() {
                            super(window, "choose color");
                            this.setSize(600, 400);
                            Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
                            this.setLocation(new Point((ss.width-600)/2, (ss.height-400)/2));
                            add(chooser);
                            setBackground(Colors.bgColor);
                            setVisible(true);
                            addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {
                                    super.windowClosed(e);
                                    color.setBackground(chooser.getColor());
                                    if (!groupSelection.isEmpty()) {
                                        for (Memory memory : groupSelection) {
                                            memory.setColor(chooser.getColor());
                                        }
                                    }
                                }
                            });
                        }
                    }
                    new ColorChoose();
                }
            });
        }
        {

            name.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (e.getKeyChar()=='\n'&&choose!=null) {
                            choose.setName(name.getText());
                            choose.setDescription(area.getText());
                            var data = tags.getModel();
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < data.getSize(); i++) {
                                list.add((String) data.getElementAt(i));
                            }
                            choose.setTags(list);
                            choose.setColor(color.getBackground());
                            choose.setPoint(new Point2D.Float(Float.parseFloat(xPos.getText()), Float.parseFloat(yPos.getText())));
                }
            }
        });}
    }



    public static class North extends UPanel {
        UButton settings = new UButton("settings"),
                back     = new UButton("<"),
                nextV    = new UButton(">");
        JLabel  versionNumber = new JLabel("page 1");
        {
            UPanel controlVersions = new UPanel();
            controlVersions.setLayout(new GridBagLayout());
            controlVersions.add(back, versionNumber, nextV);

            setLayout(new BorderLayout());
            add(controlVersions, CENTER);
            //add(controlVersions, CENTER);
            //add(settings, WEST);
            back.addActionListener(actionEvent -> {
                if (version_loaded>=1) version_loaded--;
                versionNumber.setText("page "+(version_loaded+1));
            });
            nextV.addActionListener(actionEvent -> {
                if (version_loaded >= data.getVersions().size()-1) {
                    var temp = new EventVersion();
                    //temp.getData().add(new Memory("Стартовая точка", new Point2D.Float(100, 100)));
                    data.getVersions().add(temp);
                }
                version_loaded++;
                versionNumber.setText("page "+(version_loaded+1));
            });

            settings.addActionListener(actionEvent -> {

            });
        }
    }
    public static East east;

    public JPanel north = new North();
    public JPanel south = new UPanel() {{
                add(info);
           }};
    public MainActivity() {

        var layout = new BorderLayout();
        east = new East();
        setLayout(layout);
        add(north, NORTH);
        add(draw, CENTER);
        add(east, WEST);
        add(south, SOUTH);
        Timer timer = new Timer(25, new DrawPerTick());
        timer.start();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Mady by Курбатов Юрий Михайлович(swarm)");
        System.setProperty("awt.useSystemAAFontSettings","on");
        var window = Common.window;
        window.setRootActivity(activities.get("MainActivity"));
        Thread.sleep(500l);
        window.setSize(window.getSize().width+1, window.getHeight()+1);


        window.repaint();
    }
}
