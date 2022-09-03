package ru.swarm.mind.control;

import ru.swarm.mind.Common;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Save extends AbstractAction {
    public Save() {

    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        save();
    }
    public static void save() {
        if (!new File("backups").exists())
            new File("backups").mkdir();
        if (Common.data.save(new File("data.save")))
            System.out.println("data was saved");
        else System.out.println("cannot save data");
        if (Common.properties.get("enable-backups").equals("true")) {
            Common.data.save(new File("backups/"+System.currentTimeMillis()+".save"));
        }
        try {
            Common.properties.store(new FileWriter("settings.properties"), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
