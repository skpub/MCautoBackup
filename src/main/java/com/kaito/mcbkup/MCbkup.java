package com.kaito.mcbkup;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;

public final class MCbkup extends JavaPlugin {
    private static final String data_file_path = "MCbkup.properties";
    private static Properties setting;
    @Override
    public void onEnable() {
        if ("\\".equals(System.getProperty("file.separator"))) {
            getLogger().info("OS detected. (Windows)");
            if (Files.exists(Paths.get(data_file_path))) { // initialize (If you are using this plugin for the first time)

            } else {
                getLogger().info("No configuration file was found.");
                getLogger().info("a default configuration file is created.");
                setting = new Properties();
                setting.setProperty("destination", "\"..\\BKUP\"");
                setting.setProperty("N(generation)", "20");
                setting.setProperty("interval(min)", "5");
                setting.setProperty("firsttime", "true");

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(data_file_path);
                    setting.store(out, "backup setting");
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Not yet implemented for linux, Mac and etc. ;-<");
        }
        // Continue to backup as before.
        if (setting == null) {
            setting = new Properties();
            FileInputStream in = null;
            try {
                in = new FileInputStream(data_file_path);
                setting.load(in);
                in.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        String n            = setting.getProperty("N");
        String interval     = setting.getProperty("interval");
        String dest         = setting.getProperty("dest");
        String firsttime    = setting.getProperty("firsttime");

        getLogger().info("##### backup setting #####");
        getLogger().info("destination:  "+dest);
        getLogger().info("interval(min):"+interval);
        getLogger().info("N(generation):"+n);
        getLogger().info("firsttime:    "+firsttime);
        getLogger().info("##########################");

        getLogger().info(
                Paths.get("")
                        .toAbsolutePath()
                        .toString()
        );

        bkup process = new bkup(
            Integer.parseInt(n),
            Integer.parseInt(interval),
            dest,
            Boolean.parseBoolean(firsttime)
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("プラグインが死亡");
    }
}