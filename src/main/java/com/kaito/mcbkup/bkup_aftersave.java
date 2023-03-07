package com.kaito.mcbkup;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;

public class bkup_aftersave implements Listener {
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        Bukkit.getLogger().info("せーぶされた！");
    }
}
