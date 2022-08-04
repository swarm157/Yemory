package ru.swarm.mind.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Хранит версию событий в виде некоторой структуры воспоминаний.
 * Может быть клонировано или использовано для хранения каких-то отдельных вопросов, но
 * все зависит от желания пользователя.
 */
public class EventVersion implements Serializable, Cloneable {
    /**
     * Хранимый набор воспоминаний.
     */
    ArrayList<Memory> data;
    /**
     * Имя версии событий.
     */
    String name;
    public EventVersion() {
        data = new ArrayList<>();
    }
    public EventVersion(ArrayList<Memory> data, String name) {
        this.data = data; this.name = name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (EventVersion)super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventVersion that)) return false;
        return Objects.equals(data, that.data) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, name);
    }

    @Override
    public String toString() {
        return "EventVersion{" +
                "data=" + data +
                ", name='" + name + '\'' +
                '}';
    }

    public ArrayList<Memory> getData() {
        return data;
    }

    public void setData(ArrayList<Memory> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
