package ru.swarm.mind;

import ru.swarm.mind.model.EventMaster;
import ru.swarm.mind.model.EventVersion;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.component.IconGen;
import ru.swarm.mind.view.component.MyWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
    * В данном классе хранятся все общие элементы, статичные, константы...
    */
public class Common {

    /**
     * Главное окно приложения, все изменения состояния идут через него
     */
    public static MyWindow window;


    /**
     * Все возможные состояния приложения хранятся здесь. Весь view
     */
    public static final Map<String, JRootPane> activities = new HashMap<>();

    public static final Color textColor = new Color(195, 60, 105);
    /**
     * Данная переменная хранит стандартные настройки приложения
     */
    public static final Properties default_properties = new Properties();
    static { // инициализация стандартных настроек приложения
        default_properties.put("default-width", "800");
        default_properties.put("default-height", "800");
        default_properties.put("enable-backups", "true");
    }
    /**
     * Вся информация, пользователя в программе, хранится здесь.
     */
    public static EventMaster data = new EventMaster();
    /**
     * Хранит историю изменений, помнит, только до первого перезапуска
     */
    public static ArrayList<EventMaster> buffer = new ArrayList<>();
    /**
     * Базовое значение, хранящее неадаптированный размер ширины окна
     */
    public static int DEFAULT_WIDTH = 800;

    /**
     * Базовое значение размера отрисовки воспоминаний
     */
    public static int DEFAULT_RADIUS = 30;
    /**
     * Базовое значение, хранящее неадаптированный размер высоты окна
     */
    public static int DEFAULT_HEIGHT = 800;
    /**
     * Номер загруженной версии
     */
    public static int version_loaded=0;
    /**
     * Отвечает за автоматическое создание бэкапов
     */
    public static boolean ENABLE_BACKUPS = false;
    /**
     * Все параметры приложения хранятся здесь.
     */
    public static Properties properties = new Properties();
    static { // инициализация настроек приложения
        try {
            properties.load(new FileReader("settings.properties"));
            DEFAULT_WIDTH = Integer.parseInt(properties.get("default-width").toString());
            DEFAULT_HEIGHT = Integer.parseInt(properties.get("default-height").toString());
            ENABLE_BACKUPS = Boolean.parseBoolean(properties.get("enable-backups").toString());
        } catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            properties = default_properties;
        }
    }

    static {
        // загрузка данных
        try {
            if (!data.load(new File("data.save"))) {
                data.setVersions(new ArrayList<>());
                data.getVersions().add(new EventVersion("Start"));
                Memory tutorial = new Memory("Tutorial", new Point2D.Float(200, 200));
                tutorial.setColor(new Color(250,  100, 200, 70));
                tutorial.getTags().add("tutorial");
                tutorial.setDescription("Добро пожаловать в Yemory \n" +
                        "Данная программа создана, как более эффективная замена обычному дневнику");
                data.getVersions().get(0).getData().add(tutorial);
            }
        } catch (Exception e) {
            data.setVersions(new ArrayList<>());
            data.getVersions().add(new EventVersion("Start"));
            Memory tutorial = new Memory("Tutorial", new Point2D.Float(200, 200));
            tutorial.setColor(new Color(250,  150, 200, 70));
            tutorial.getTags().add("tutorial");
            tutorial.setDescription("Добро пожаловать в Yemory \n" +
                    "Данная программа создана, как более эффективная замена обычному дневнику");
            data.getVersions().get(0).getData().add(tutorial);
            e.printStackTrace();
        }
    }

    static {
        //инициализация окна

        window = new MyWindow() {{
            Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((ss.width-DEFAULT_WIDTH)/2-(DEFAULT_WIDTH-ss.width)/4,
                    (ss.height-DEFAULT_HEIGHT)/2-(DEFAULT_HEIGHT-ss.height)/4);
            setSize( DEFAULT_WIDTH+(DEFAULT_WIDTH-ss.width)/2,
                    DEFAULT_HEIGHT+(DEFAULT_HEIGHT-ss.height)/2);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Yemory");
            setIconImage(IconGen.generateIcon());
            setVisible(true);
        }};
    }

}
