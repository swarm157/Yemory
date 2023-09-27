package ru.swarm.mind;

import ru.swarm.mind.model.EventMaster;
import ru.swarm.mind.model.EventVersion;
import ru.swarm.mind.model.Memory;
import ru.swarm.mind.view.component.IconGen;
import ru.swarm.mind.view.component.MyWindow;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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


    public static void run(String script) {
        Thread thread = new Thread(() -> {
            try {
                ScriptEngineManager manager = new ScriptEngineManager();
                var engineFactories = manager.getEngineFactories();

                for (ScriptEngineFactory factory : engineFactories) {
                    System.out.println("Engine Name: " + factory.getEngineName());
                    System.out.println("Engine Version: " + factory.getEngineVersion());
                    System.out.println("Language Name: " + factory.getLanguageName());
                    System.out.println("Language Version: " + factory.getLanguageVersion());
                    System.out.println("Extensions: " + factory.getExtensions());
                    System.out.println("Mime Types: " + factory.getMimeTypes());
                    System.out.println("-----");
                }
                ScriptEngine engine = manager.getEngineByName("js");
                engine.eval(script);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }


    /**
     * Главное окно приложения, все изменения состояния идут через него
     */
    public static MyWindow window;


    /**
     * Все возможные состояния приложения хранятся здесь. Весь view
     */
    public static final Map<String, JRootPane> activities = new HashMap<>();
    public static String getFirstElementWithoutPrefix(ArrayList<String> list, String prefix) {
        for (String element : list) {
            if (element.startsWith(prefix)) {
                return element.substring(prefix.length());
            }
        }
        return "";
    }
    public static void findAndReplace(ArrayList<String> list, String word, String value) {
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            String element = list.get(i);
            if (element.contains(word)) {
                found = true;
                if (value != null) {
                    int index = element.indexOf(word);
                    String newValue = element.substring(0, index + word.length()) + value;
                    list.set(i, newValue);
                }
            }
        }
        if (!found && value != null) {
            list.add(word + value);
        }
    }
    static double getValue(String var) {
        String temp = var;
            if (temp.startsWith("$")) {
                for (Memory memory : data.getVersions().get(version_loaded).getData()) {
                    if (memory.getTags().contains(temp.replace("$", ""))) {
                        var e = getFirstElementWithoutPrefix(memory.getTags(), "formResult");
                        if (!e.equals(""))
                            temp = e;
                        else
                            temp = memory.getName();
                        break;
                    }
                }
            } else if(temp.startsWith("#")) {
                var sum = 0.0;
                var temp2 = "0.0";
                for (Memory memory : data.getVersions().get(version_loaded).getData()) {
                    if (memory.getTags().contains(temp.replace("#", ""))) {
                        var e = getFirstElementWithoutPrefix(memory.getTags(), "formResult");
                        if (!e.equals(""))
                            temp2 = e;
                        else
                            temp2 = memory.getName();
                        try {
                            sum += Double.parseDouble(temp2);
                        } catch (Exception ignored) {}
                    }
                }
                temp = String.valueOf(sum);
            }
            double num2;
            try {
                num2 = Double.parseDouble(temp);
            } catch (Exception e) {
                num2 = 0.0;
            }
            return num2;
    }

    public static String calculate(String input, Memory memory) {
        double result;
        var mems = data.getVersions().get(version_loaded).getData();
        // Remove any spaces from the input string
        input = input.replaceAll("\\s+", "");

        // Split the input string into individual operands and operators
        //String[] tokens = input.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
        //String[] tokens = input.split("([+\\-*/$#]|\\d+)");
        String[] tokens = input.split("(?=[+\\-*/])|(?<=[+\\-*/])");

        // Perform the calculations
        double num1 = getValue(tokens[0]);
        result=num1;
        for (int i = 1; i < tokens.length-1; i += 2) {
            String operator = tokens[i];
            double num2 = getValue(tokens[i + 1]);


            switch (operator) {
                case "+":
                    result += num2;
                    break;
                case "-":
                    result -= num2;
                    break;
                case "*":
                    result *= num2;
                    break;
                case "/":
                    result /= num2;
                    break;
                default:
                    System.out.println("Invalid operator: " + operator);
                    break;
            }
        }
        var res = String.valueOf(result);
        findAndReplace(memory.getTags(), "formResult", res);
        return res;
    }

    public static class Colors {




        public static final Color textColor = new Color(255, 255, 115);
        public static final Color border_color = new Color(80, 80,78);
        public static final Color color = new Color(150, 150,150);
        //public static final Color bgColor = new Color(88, 88,88);
        public static final Color bgColor = new Color(178, 178,178);
        public static final Color bgPanelColor = new Color(158, 158,158);


        //public static final Color circleFill = new Color(25, 17, 35);
        //public static final Color circleDraw = new Color(25, 17, 35);
        public static final Color eraseColor = new Color(238, 238, 148);
        public static final Color select = new Color(245, 245, 158, 90);
        public static final Color groupSelect = new Color(245, 245, 230, 90);
        public static final Color groupSelectHold = new Color(215, 215, 188, 60);
        public static final Color mouseOver = new Color(230, 230, 230, 75);













        /*public static final Color textColor = new Color(195, 60, 105);
        public static final Color border_color = new Color(30, 10,25);
        public static final Color color = new Color(55, 30,50);
        public static final Color bgColor = new Color(70, 57,69);*/




    }

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
    public static int DEFAULT_RADIUS = 150;
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
                Memory tutorial = new Memory("Get started(Tutorial)", new Point2D.Float(300, 300));
                tutorial.setColor(new Color(250,  150, 200, 70));
                tutorial.getTags().add("tutorial");
                tutorial.setDescription("Добро пожаловать в Yemory \n" +
                        "Юра + memory, но читается, как Емори.\n" +
                        "Данная программа создана, как более эффективная замена обычному дневнику\n" +
                        "Главный упор делается на интуитивное управление мышью, лишь панели используют\n" +
                        "кнопки.\n" +
                        "Чтобы развернуть данный текст, нажмите кнопку description\n" +
                        "Правая кнопка мыши отвечает за единичное выделение и\n" +
                        "перемещение, клик в пустоту для снятия массового выделения.\n" +
                        "Левая кнопка мыли выделяет, как массово, так и единично\n" +
                        "Зажмите ее на обьекте и вы сможете создать связь с другим в\n" +
                        "порядке \"что от чего зависит\"\n" +
                        "Колесом мыши можно перемещаться, посредством зума\n" +
                        "Внимание, клик удаляет обьекты, по обьекту для единичного\n" +
                        "В пустоту, для удаления единичного, а зажатие режет связи.\n" +
                        "Автор данного программного обеспечения Курбатов Ю.М., оно\n" +
                        "распространяется под лицензией свободного ПО, кто угодно\n" +
                        "может им пользоваться, но, не может продавать или использовать\n" +
                        "код данного ПО в коммерческих целях\n" +
                        "Левый клик, для открытия меню\n" +
                        "clone - клонирует группу, может на другую страницу\n" +
                        "search - открывает окно, ведет линию к центру экрана от подходящих\n" +
                        "export - позволяет копировать выделенный фрагмент в файл\n" +
                        "import - загружает сохраненный фрагменты из выделенных файлов\n" +
                        "Верхние стрелки отвечают за перемещение по страницам,\n" +
                        "Их количество крайне огромно, фактически безгранично, а их создание\n" +
                        "контролируется автоматически.\n" +
                        "Это был краткий гайд по использованию данной программы\n" +
                        "Удачного опыта пользования!");
                data.getVersions().get(0).getData().add(tutorial);
            }
        } catch (Exception e) {
            data.setVersions(new ArrayList<>());
            data.getVersions().add(new EventVersion("Start"));
            Memory tutorial = new Memory("Get started(Tutorial)", new Point2D.Float(300, 300));
            tutorial.setColor(new Color(250,  150, 200, 70));
            tutorial.getTags().add("tutorial");
            tutorial.setDescription("Добро пожаловать в Yemory \n" +
                    "Юра + memory, но читается, как Емори.\n" +
                    "Данная программа создана, как более эффективная замена обычному дневнику\n" +
                    "Главный упор делается на интуитивное управление мышью, лишь панели используют\n" +
                    "кнопки.\n" +
                    "Чтобы развернуть данный текст, нажмите кнопку description\n" +
                    "Правая кнопка мыши отвечает за единичное выделение и\n" +
                    "перемещение, клик в пустоту для снятия массового выделения.\n" +
                    "Левая кнопка мыли выделяет, как массово, так и единично\n" +
                    "Зажмите ее на обьекте и вы сможете создать связь с другим в\n" +
                    "порядке \"что от чего зависит\"\n" +
                    "Колесом мыши можно перемещаться, посредством зума\n" +
                    "Внимание, клик удаляет обьекты, по обьекту для единичного\n" +
                    "В пустоту, для удаления единичного, а зажатие режет связи.\n" +
                    "Автор данного программного обеспечения Курбатов Ю.М., оно\n" +
                    "распространяется под лицензией свободного ПО, кто угодно\n" +
                    "может им пользоваться, но, не может продавать или использовать\n" +
                    "код данного ПО в коммерческих целях\n" +
                    "Левый клик, для открытия меню\n" +
                    "clone - клонирует группу, может на другую страницу\n" +
                    "search - открывает окно, ведет линию к центру экрана от подходящих\n" +
                    "export - позволяет копировать выделенный фрагмент в файл\n" +
                    "import - загружает сохраненный фрагменты из выделенных файлов\n" +
                    "Верхние стрелки отвечают за перемещение по страницам,\n" +
                    "Их количество крайне огромно, фактически безгранично, а их создание\n" +
                    "контролируется автоматически.\n" +
                    "Это был краткий гайд по использованию данной программы\n" +
                    "Удачного опыта пользования!");
            data.getVersions().get(0).getData().add(tutorial);
            e.printStackTrace();
        }
    }

    static {
        //инициализация окна

        window = new MyWindow() {{
            Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
            /*setLocation((ss.width-DEFAULT_WIDTH)/2-(DEFAULT_WIDTH-ss.width)/4,
                    (ss.height-DEFAULT_HEIGHT)/2-(DEFAULT_HEIGHT-ss.height)/4);
            setSize( DEFAULT_WIDTH+(DEFAULT_WIDTH-ss.width)/2,
                    DEFAULT_HEIGHT+(DEFAULT_HEIGHT-ss.height)/2);*/
            setLocation(0, 0);
            setSize(ss.getSize());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("Yemory");
            setIconImage(IconGen.generateIcon());
            setVisible(true);
        }};
    }

}
