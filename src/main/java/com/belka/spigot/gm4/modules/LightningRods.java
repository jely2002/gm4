package com.belka.spigot.gm4.modules;

import api.ConsoleColor;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LightningRods implements Listener, Initializable {

    private MainClass mc;
    private boolean enabled = true;
    private HashMap<Location, int[]> droppedRods = new HashMap<>();
    private ArrayList<String> players = new ArrayList<>();

    public LightningRods(MainClass mc) {
        this.mc = mc;
    }

    public void init(MainClass mc) {
        if(!mc.getStorage().config().getBoolean("LightningRods.enabled")) enabled = false;
        if(!mc.getStorage().config().getBoolean("CustomCrafter.enabled")) {
            System.out.println(ConsoleColor.RED + "Enable CustomCrafter in order for LightningRods to work!");
            mc.getStorage().config().set("LightningRods.enabled", false);
            mc.saveConfig();
            enabled = false;
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
		if (!enabled) return;
        if (!(e.getItem().getItemStack().getType() == Material.BLAZE_ROD)) return;
        if (!(e.getItem().getItemStack().getItemMeta().getLore().get(0).contains("boom"))) return;
        for (Location l : droppedRods.keySet()) {
            if (l.getBlockX() == e.getItem().getLocation().getBlockX() && l.getBlockZ() == e.getItem().getLocation().getBlockZ() && l.getBlockY() == e.getItem().getLocation().getBlockY()) {
                int[] task = new int[]{-1};
                Location itemRound = new Location(e.getItem().getWorld(), e.getItem().getLocation().getBlockX(), e.getItem().getLocation().getBlockY(), e.getItem().getLocation().getBlockZ());
                task[0] = droppedRods.get(itemRound)[0];
                Bukkit.getScheduler().cancelTask(task[0]);
            }
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!enabled) return;
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
                i.setCustomNameVisible(true);
                i.setCustomName("<3>");
                Block b = i.getLocation().getBlock();
                Location locRound = new Location(i.getWorld(), b.getX(), b.getY(), b.getZ());
				AtomicInteger count = new AtomicInteger();
				final int[] t = new int[]{-1};
				droppedRods.put(locRound, t);
				t[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
					i.setCustomName("<" + (3 - count.get()) + ">");
					if(count.get() >= 3) {
						Bukkit.getScheduler().cancelTask(t[0]);
						if (b.getRelative(BlockFace.DOWN).getType() == Material.PURPUR_BLOCK) {
							Advancements.grantAdvancement("dr_frankenstein", event.getPlayer());
							b.getRelative(BlockFace.DOWN).setType(Material.AIR);
							i.getWorld().spawnEntity(b.getLocation().add(0.5, -1, 0.5), EntityType.SHULKER);
						}
						i.getWorld().strikeLightning(i.getLocation());
					}
					count.getAndIncrement();
				}, 0, 20L);
            }
        }, 0, 10L);
    }
}
