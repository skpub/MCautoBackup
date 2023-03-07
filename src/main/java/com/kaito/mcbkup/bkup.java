package com.kaito.mcbkup;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.TimerTask;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

public class bkup {
    private int n;
    private int interval;
    private String dest;
    private boolean firsttime;
    private TimerTask task;
    private Timer timer;
    bkup(int n, int interval, String dest, boolean firsttime) {
        this.n = n;
        this.interval = interval;
        this.dest = dest;
        this.firsttime = firsttime;

        if (firsttime) {
            String current_dir = Paths.get("")
                    .toAbsolutePath()
                    .toString();

            MCbkup.save_all();
            try {
                Process p = Runtime.getRuntime().exec(
                        "robocopy "
                                + current_dir
                                + " "
                                + this.dest
                                + " /E /B /COPYALL /NP /R:3 /MIR"
                );
                p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }

            try { // reset archive attribute.
                Process p = Runtime.getRuntime().exec(
                        "attrib -A "
                                + current_dir
                                + "\\* "
                                + "/S"
                );
                p.waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        this.task = new TimerTask() {
            public void run() {
            }
        };
        this.timer = new Timer();
        timer.schedule(task, this.interval, this.interval*1000*60);
    }
}

