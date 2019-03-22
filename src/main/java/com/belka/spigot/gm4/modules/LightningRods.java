package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;

public class LightningRods implements Listener {

    private MainClass mc;
    private HashMap<Location, int[]> droppedRods = new HashMap<>();
    private ArrayList<String> players = new ArrayList<>();

    public LightningRods(MainClass mc) {
        this.mc = mc;
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if(!(e.getItem().getItemStack().getType() == Material.BLAZE_ROD)) return;
        if(!(e.getItem().getItemStack().getItemMeta().getLore().get(0).contains("boom"))) return;
        for(Location l : droppedRods.keySet()) {
            if(l.getBlockX() == e.getItem().getLocation().getBlockX() && l.getBlockZ() == e.getItem().getLocation().getBlockZ() && l.getBlockY() == e.getItem().getLocation().getBlockY()) {
                int[] task = new int[]{-1};
                Location itemRound = new Location(e.getItem().getWorld(), e.getItem().getLocation().getBlockX(), e.getItem().getLocation().getBlockY(), e.getItem().getLocation().getBlockZ());
                task[0] = droppedRods.get(itemRound)[0];
                Bukkit.getScheduler().cancelTask(task[0]);
            }
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!mc.getConfig().getBoolean("LightningRods.enabled")) return;
        Item i = event.getItemDrop();
        ItemStack is = i.getItemStack();
        if(!(is.getType() == Material.BLAZE_ROD)) return;
        if(!(is.getItemMeta().getDisplayName().contains("Lightning"))) return;
        if(!(is.getItemMeta().getLore().get(0).contains("boom"))) return;
        if(players.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
            return;
        }
        players.add(event.getPlayer().getName());
        mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> players.remove(event.getPlayer().getName()), 3*20L);
        final int[] task = new int[]{-1};
        task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
            if (i.isOnGround()) {
                Bukkit.getScheduler().cancelTask(task[0]);
                int[] t = new int[]{-1};
                Location locRound = new Location(i.getWorld(), i.getLocation().getBlockX(), i.getLocation().getBlockY(), i.getLocation().getBlockZ());
                droppedRods.put(locRound, t);
                t[0] = Bukkit.getScheduler().scheduleSyncDelayedTask(mc, () -> i.getWorld().strikeLightning(i.getLocation()), 3*20L);
            }
        }, 0, 10L);
    }

}
