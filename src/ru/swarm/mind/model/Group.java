package ru.swarm.mind.model;

import ru.swarm.mind.Common;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class Group implements Serializable {
    public ArrayList<Memory> memories;
    public Group(ArrayList<Memory> memories) {
        this.memories = memories;
    }
    public Group(String filename) {
        File file = new File(filename);

        if (file.isDirectory()) return;
        if (!file.canRead()) return;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            Group temp = (Group) inputStream.readObject();
            if (temp.memories==null) return;
            memories = temp.memories;
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(String filename) {
        File file = new File(filename);
        if (file.isDirectory()) return;
        //if (!file.canWrite()) return false;
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(this);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
