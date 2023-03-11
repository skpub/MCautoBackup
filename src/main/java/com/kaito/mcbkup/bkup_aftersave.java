package com.kaito.mcbkup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class bkup_aftersave implements Listener {
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        String worldname = event.getWorld().getName();
        Bukkit.getLogger().info(worldname);
        if (!Files.exists(Paths.get(MCbkup.setting.getProperty("destination")))) {
            try {
                Files.createDirectory(Paths.get(MCbkup.setting.getProperty("destination")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        String source_world = Paths.get("").toAbsolutePath().toString()
                + "\\" + worldname;
        String dest_world = MCbkup.setting.getProperty("destination")
                + "\\" + worldname;
        String unixtime = String.valueOf(System.currentTimeMillis());
        String dest_world_with_time = dest_world + "\\" + worldname + "_" + unixtime;

        if (Files.exists(Paths.get(dest_world)))
        {   // incremental backup of previous (full|incremental) backup .
            try {
                // execute incremental backup.
                String[] command = new String[] {
                        "robocopy"
                        , source_world
                        , dest_world_with_time
                        , "/MIR"
                        , "/M"
                        , "/XF"
                        , "session.lock"
                };
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
                // ------------ end of incremental backup.

                // make an index file that contains list of files deleted as compared with previous backup to rollback
                Set<Path> source_files_rec;
                Set<Path> dest_files_rec;

                File bkup_dir = new File(dest_world);
                String recent_full_bkup_dir = Arrays.stream(Objects.requireNonNull(bkup_dir.listFiles()))
                        .map(File::toString)
                        .filter(x -> x.contains("_FULL_"))
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList())
                        .get(0);

                File index_file;
//                try {
                    index_file = new File(dest_world_with_time + "\\" + ".index");
//                    if (!index_file.createNewFile()) {
//                        System.out.println("can't create index file.");
//                    }
//                } catch (IOException e) {
//                    throw new IOException(e);
//                }

                try (
                    Stream<Path> source_files_rec_pre
                        = Files.walk(Paths.get(source_world));
                    Stream<Path> dest_files_rec_pre
                        = Files.walk(Paths.get(recent_full_bkup_dir));
                    FileWriter filewriter = new FileWriter(index_file)
                ) {
                    source_files_rec = source_files_rec_pre.collect(Collectors.toSet());
                    dest_files_rec = dest_files_rec_pre.collect(Collectors.toSet());

                    source_files_rec.removeAll(dest_files_rec);
                    source_files_rec.forEach(e -> {
                        try {
                            filewriter.write(e.toString());
                            filewriter.write("\r\n");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // ----- end of making an index file.
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else
        {   // FULL backup.
            try {
                Files.createDirectory(Paths.get(dest_world));
                String[] command = new String[]{
                    "robocopy "
                        , source_world
                        , dest_world_with_time + "_FULL_"
                        , "/MIR"
                        , "/XF"
                        , "session.lock"
                };
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();
            } catch (Exception e) {
                throw new RuntimeException(e);
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
                throw new RuntimeException(e);
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
