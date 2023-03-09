package com.kaito.mcbkup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;


public class bkup_aftersave implements Listener {
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        String worldname = event.getWorld().getName();
        Bukkit.getLogger().info(worldname);
        if (!Files.exists(Paths.get(MCbkup.setting.getProperty("destination")))) {
            try {
                Files.createDirectory(Paths.get(MCbkup.setting.getProperty("destination")));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        if (!Files.exists(
                Paths.get(MCbkup.setting.getProperty("destination")
                    + "\\"
                    + worldname)
            ))
        {
            try {
                Process p = Runtime.getRuntime().exec( new String[]{
                        "robocopy "
                                , Paths.get("").toAbsolutePath().toString()
                                + "\\"
                                + worldname
                                , MCbkup.setting.getProperty("destination")
                                + "\\"
                                + worldname
                                , "/MIR"
                });
                p.waitFor();
                 InputStream is = p.getInputStream();
                 Bukkit.getLogger().info(is.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
//        try {
//            Process p = Runtime.getRuntime().exec("");
//            p.waitFor();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
    }
}
