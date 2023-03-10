package com.kaito.mcbkup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.nio.file.Path;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
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
        String source_world = Paths.get("").toAbsolutePath().toString()
                + "\\" + worldname;
        String dest_world = MCbkup.setting.getProperty("destination")
                + "\\" + worldname;
        String unixtime = String.valueOf(System.currentTimeMillis());

        if (Files.exists(
                Paths.get(dest_world)))
        {
            try {
                String[] command = new String[] {
                    "robocopy"
                        , source_world
                        , dest_world + "\\" + worldname + "_" + unixtime
                        , "/MIR"
                        , "/M"
                        , "/XF"
                        , "session.lock"
                };
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                Files.createDirectory(Paths.get(dest_world));
                String[] command = new String[]{
                    "robocopy "
                        , source_world
                        , dest_world + "\\" + worldname + "_" + unixtime + "_FULL_"
                        , "/MIR"
                        , "/XF"
                        , "session.lock"
                };
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }

            try { // reset attribute.
                String[] command = new String[]{
                    "attrib"
                        , "-A"
                        , source_world + "\\" + "*"
                        , "/S"
                };
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public static void printInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(is)
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            System.out.println(line.toString());
        }
        br.close();
    }
}
