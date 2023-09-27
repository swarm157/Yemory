package ru.swarm.mind.model;

import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Класс Memory представляет из себя базовую единицу хранения,
 * В нем следует хранить: Воспоминания; Мысли; Чувства и тд... в единичных экземплярах.
 * Более сложные структуры должен определять сам пользователь с помощью выданного им
 * программой набора инструментов.
 */
public class Memory implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Служит в качестве заглавия воспоминания
     */
    String name;
    /**
     * Служит для организации воспоминаний по любым
     * , задаваемым  пользователем ,параметрам.
     * По ним следует осуществять поиск, организацию, анализ и тд...
     */
    ArrayList<String> tags;
    /**
     * Здесь должно храниться само воспоминание, его основаная суть,
     * события происходящие в нем, размышления и тд...
     */
    String description;
    /**
     * Положение на холсте. Должно использоваться пользователем для организации.
     */
    Point2D point;
    /**
     * Цвет воспоминания, служит инструментом организации для пользователя,
     * вероятнее всего в качестве пометки событий эмоциями или чем-то подобным.
     */
    Color color;

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Memory(String name, ArrayList<String> tags, String description, Point2D point, Color color, Image img, ArrayList<Memory> linkedMemories) {
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.point = point;
        this.color = color;
        this.img = img;
        this.linkedMemories = linkedMemories;
    }


    transient Image img;

    public Image getArt() {
        return art;
    }

    public void setArt(Image art) {
        this.art = art;
    }

    public Memory(String name, ArrayList<String> tags, String description, Point2D point, Color color, Image img, Image art, ArrayList<Memory> linkedMemories) {
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.point = point;
        this.color = color;
        this.img = img;
        this.art = art;
        this.linkedMemories = linkedMemories;
    }

    Image art;

    public ArrayList<Memory> getLinkedMemories() {
        return linkedMemories;
    }

    public void setLinkedMemories(ArrayList<Memory> linkedMemories) {
        this.linkedMemories = linkedMemories;
    }

    ArrayList<Memory> linkedMemories;

    public Memory(String name, ArrayList<String> tags, String description, Point2D point, Color color, ArrayList<Memory> linkedMemories) {
        this.name = name;
        this.tags = tags;
        this.description = description;
        this.point = point;
        this.color = color;
        this.linkedMemories = linkedMemories;
    }

    public Memory(String name, Point2D point) {
        tags = new ArrayList<>();
        description = "";
        color = Color.GRAY;
        this.point = point;
        this.name = name;
        this.linkedMemories = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Memory{" +
                "name='" + name + '\'' +
                ", tags=" + tags +
                ", description='" + description + '\'' +
                ", point=" + point +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Memory memory)) return false;
        return Objects.equals(name, memory.name) && tags.equals(memory.tags) && Objects.equals(description, memory.description) && point.equals(memory.point) && color.equals(memory.color);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (Memory)super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tags, description, point, color);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public Color getColor() {
        return color;
    }

    public String getTagProperty(String name) {
        ArrayList<String> list = tags;
        for (String element : list) {
            if (element.replace("\n", "").startsWith(name)) {
                return element.replace("\n", "").replaceFirst(name, "");
            }
        }
        return "";
    }

    public void setTagProperty(String name, String value) {
        ArrayList<String> list = tags;
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            String element = list.get(i);
            if (element.replace("\n", "").startsWith(name)) {
                found = true;
                if (value != null) {
                    int index = element.indexOf(name);
                    String newValue = element.substring(0, index + name.length()) + value;
                    list.set(i, newValue);
                }
            }
        }
        if (!found && value != null) {
            list.add(name + value);
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
