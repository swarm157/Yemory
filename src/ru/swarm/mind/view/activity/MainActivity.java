package ru.swarm.mind.view.activity;

import ru.swarm.mind.Common;
import ru.swarm.mind.control.DrawAction;
import ru.swarm.mind.view.component.Draw;
import ru.swarm.mind.view.component.UButton;
import ru.swarm.mind.view.component.UPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

import static java.awt.BorderLayout.*;
import static ru.swarm.mind.Common.*;
import static ru.swarm.mind.view.component.Draw.choosed;

public class MainActivity extends JRootPane {

    public static javax.swing.JLabel info  = new javax.swing.JLabel();
    static {info.setForeground(textColor);activities.put("MainActivity", new MainActivity());}
    Draw draw    = new Draw  ();

    static class JLabel extends javax.swing.JLabel {

        public JLabel(String text) {
            setText(text);
            setForeground(textColor);
        }
    }

    static class JScrollPane extends javax.swing.JScrollPane {
        //public static Color background = new Color(50, 37,40);
        public static Color background = UButton.color;
        {
            setBackground(background);
            var h = getHorizontalScrollBar();
            h.setBackground(background);
            h.setBorder(new LineBorder(UButton.border_color));
            var v = getVerticalScrollBar();
            v.setBackground(background);
            v.setBorder(new LineBorder(UButton.border_color));
            setBorder(new LineBorder(UButton.border_color));
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

        {
            setBackground(JTextArea.background);
            setForeground(Color.white);
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
            setForeground(textColor);
            setBorder(new LineBorder(UButton.border_color));
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
        private static Color background = new Color(52, 43,50);
        {
            setBackground(background);
            setForeground(textColor);
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
                cancel = new UButton("cancel");
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
            scroll.setPreferredSize(new Dimension(200, 250));
            //scroll.setSize(new Dimension(400, 200));

            tagsPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            tagsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            tagsPane.setPreferredSize(new Dimension(200, 150));
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
            line.setBorder(null);
            line.add(cancel);
            cancel.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    javax.swing.JTextArea area = MainActivity.East.area;
                    MainActivity.East.name.setText(choosed.getName());
                    MainActivity.East.xPos.setText(String.valueOf(choosed.getPoint().getX()));
                    MainActivity.East.yPos.setText(String.valueOf(choosed.getPoint().getY()));
                    color.setBackground(choosed.getColor());
                    javax.swing.JList<String> tags = MainActivity.East.tags;
                    tags.setListData(choosed.getTags().toArray(new String[choosed.getTags().size()]));
                    area.setText(choosed.getDescription());
                }
            });
            line.add(done);
            done.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    choosed.setName(name.getText());
                    choosed.setDescription(area.getText());
                    var data = tags.getModel();
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < data.getSize(); i++) {
                        list.add((String) data.getElementAt(i));
                    }
                    choosed.setTags(list);
                    choosed.setColor(color.getBackground());
                    choosed.setPoint(new Point2D.Float(Float.parseFloat(xPos.getText()), Float.parseFloat(yPos.getText())));
                }
            });
            gbc.gridx = 2;
            gbc.gridy = 9;
            add(line, gbc);
            color.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    class ColorChoose extends JDialog {
                        JColorChooser chooser = new JColorChooser();
                        public ColorChoose() {
                            super(window, "choose color");
                            setLocation(window.getLocation());
                            setSize(window.getSize());
                            add(chooser);
                            setBackground(UPanel.color);
                            setVisible(true);
                            addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {
                                    super.windowClosed(e);
                                    color.setBackground(chooser.getColor());
                                }
                            });
                        }
                    }
                    new ColorChoose();
                }
            });
        }
    }

    public class West extends UPanel {
        public static DefaultMutableTreeNode root = new DefaultMutableTreeNode("versions");
        public static JTree tree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);
        {
            tree.setBackground(JTextArea.background);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(160, 550));
            add(scrollPane);
        }
    }

    public static class North extends UPanel {
        UButton previous = new UButton("previous"),
                next     = new UButton("next"),
                add      = new UButton("add"),
                settings = new UButton("settings");
        {
            UPanel line = new UPanel();
            line.setBorder(null);
            line.add(previous,next, add, settings);
            setLayout(new BorderLayout());
            add(line, WEST);
        }
    }
    JPanel east  = new East(),
           west  = new West(),
           north = new North(),
           south = new UPanel() {{
                add(info);
           }};
    public MainActivity() {
        var layout = new BorderLayout();
        setLayout(layout);
        add(draw, CENTER);
        add(east, EAST);
        add(west, WEST);
        add(north, NORTH);
        add(south, SOUTH);
        Timer timer = new Timer(25, new DrawAction());
        timer.start();
    }

    public static void main(String[] args) throws InterruptedException {
        var window = Common.window;
        window.setRootActivity(activities.get("MainActivity"));
        Thread.sleep(500l);
        window.setSize(window.getSize().width+1, window.getHeight()+1);
        window.repaint();
    }
}
