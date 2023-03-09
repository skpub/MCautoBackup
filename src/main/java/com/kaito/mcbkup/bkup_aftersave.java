package com.kaito.mcbkup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.io.InputStreamReader;
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
                String[] command = new String[]{
                                "robocopy "
                                , Paths.get("").toAbsolutePath().toString()
                                + "\\"
                                + worldname
                                , MCbkup.setting.getProperty("destination")
                                + "\\"
                                + worldname
                                , "/MIR"
                                , "/XF"
                                , "session.lock"};
                for (String str: command) {
                    System.out.print(str + " ");
                }
                System.out.println(
                    "current_path: " + Paths.get("").toAbsolutePath()
                );
                System.out.println();
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();

                InputStream es = p.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(es)
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    System.out.println(line.toString());
                }
                br.close();
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
