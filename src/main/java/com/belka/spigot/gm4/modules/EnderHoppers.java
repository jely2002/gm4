package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import java.util.ArrayList;

public class EnderHoppers implements Listener, Initializable {

    private MainClass mc;
    private ArrayList<Item> droppedItems = new ArrayList<>();

    public EnderHoppers(MainClass mc) {
        this.mc = mc;
    }

    @Override
    public void init(MainClass mc) {
        mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
            for (Item i : droppedItems) {
                for (String key : mc.storage().data().getConfigurationSection("EnderHoppers").getKeys(false)) {
                    Location hopper = new Location(Bukkit.getWorld(mc.storage().data().getString("EnderHoppers." + key + ".world")), mc.storage().data().getInt("EnderHoppers." + key + ".x"), mc.storage().data().getInt("EnderHoppers." + key + ".y"), mc.storage().data().getInt("EnderHoppers." + key + ".z"));
                    Location item = i.getLocation();
                    Location min = hopper.subtract(4, 1, 4);
                    Location max = hopper.add(5, 2, 5);
                    if (min.getX() <= item.getX() && min.getY() <= item.getY() && min.getZ() <= item.getZ()) {
                        if (max.getX() >= item.getX() && max.getY() >= item.getY() && max.getZ() >= item.getZ()) {
                            i.teleport(hopper.add(0.5, 1, 0.5));
                            droppedItems.remove(i);
                        }
                    }
                }
            }
        }, 0, 20L);
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if(!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
        droppedItems.add(e.getEntity());
    }

    @EventHandler
    public void onMerge(ItemMergeEvent e) {
        if(!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
        droppedItems.remove(e.getEntity());
        droppedItems.remove(e.getTarget());
        droppedItems.add(e.getEntity());
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
        droppedItems.remove(e.getItem());
    }

    @EventHandler
    public void onDrop(ItemSpawnEvent e) {
        if(!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
        droppedItems.add(e.getEntity());
    }

}
