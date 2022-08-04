package ru.swarm.mind.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * EventMaster используется в качестве ключего связующего и управляющего звена
 * между всеми помещенными в программу данными. Реализует функцию сохранения и загрузки
 */
public class EventMaster implements Cloneable, Serializable {

    /**
     * Содержит все существующие версии и структуры в программе.
     * Весь класс существует, только ради этого контейнера.
     */
    ArrayList<EventVersion> versions;

    public EventMaster(ArrayList<EventVersion> versions) {
        this.versions = versions;
    }

    public EventMaster() {
    }

    /**
     *
     * @param file файл в который будут сохраняться данные
     * @return результат исполнения, успех или провал
     */
    public boolean save(File file) {
        if (file.isDirectory()) return false;
        if (!file.canWrite()) return false;
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(this);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param file имя файла из которого будут загружаться данные
     * @return возвращает успех или провал операции.
     */
    public boolean load(File file) {
        if (file.isDirectory()) return false;
        if (!file.canRead()) return false;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            EventMaster temp = (EventMaster) inputStream.readObject();
            if (temp.versions==null) return false;
            versions = temp.versions;
            inputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "EventMaster{" +
                "versions=" + versions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventMaster that)) return false;
        return versions.equals(that.versions);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(versions);
    }

    public ArrayList<EventVersion> getVersions() {
        return versions;
    }

    public void setVersions(ArrayList<EventVersion> versions) {
        this.versions = versions;
    }
}
