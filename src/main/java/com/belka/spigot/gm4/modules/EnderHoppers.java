package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnderHoppers implements Listener, Initializable {

    private MainClass mc;
    private ArrayList<Item> droppedItems = new ArrayList<>();

    public EnderHoppers(MainClass mc) {
        this.mc = mc;
    }

    @Override
    public void init(MainClass mc) {
        mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
            for (Iterator<Item> it = droppedItems.iterator(); it.hasNext();) {
            	Item i = it.next();
				List<String> active = mc.storage.data().getStringList("EnderHoppers");
                for (String hopper : active) {
					Location loc = Helper.locFromConfig(hopper);
                    Location item = i.getLocation();
                    Location min = loc.clone().subtract(4, 1, 4);
                    Location max = loc.clone().add(4, 1, 4);
                    if (min.getX() <= item.getBlockX() && min.getY() <= item.getBlockY() && min.getZ() <= item.getBlockZ()) {
                        if (max.getX() >= item.getBlockX() && max.getY() >= item.getBlockY() && max.getZ() >= item.getBlockZ()) {
							it.remove();
                            i.teleport(loc.add(0.5, 1, 0.5));
                        }
                    }
                }
            }
        }, 0, 20L);
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if(!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
        droppedItems.remove(e.getEntity());
    }

    @EventHandler
    public void onMerge(ItemMergeEvent e) {
        if(!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
        droppedItems.remove(e.getEntity());
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

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (!mc.getConfig().getBoolean("EnderHoppers.enabled")) return;
		Item i = e.getItemDrop();
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if (i.isOnGround()) {
				Bukkit.getScheduler().cancelTask(task[0]);
				Location loc = i.getLocation();
				if (i.getItemStack().getType() == Material.ENDER_EYE) {
					Block b = loc.getBlock().getLocation().getBlock();
					if (b.getBlockData().getMaterial() == Material.HOPPER || b.getRelative(0, -1, 0).getBlockData().getMaterial() == Material.HOPPER) {
						Hopper h;
						if (b.getBlockData().getMaterial() == Material.HOPPER) h = (Hopper) b.getState();
						else h = (Hopper) b.getRelative(0, -1, 0).getState();
						List<String> active = mc.storage.data().getStringList("EnderHoppers");
						List<String> cc = mc.storage.data().getStringList("CustomCrafter.customCrafters");
						if (!active.contains("x:" + h.getX() + " y:" + h.getY() + " z:" + h.getZ() + " w:" + h.getWorld().getName()) && !cc.contains("x:" + h.getX() + " y:" + h.getY() + " z:" + h.getZ() + " w:" + h.getWorld().getName())) {
							Inventory inv = h.getInventory();
							if (containsRecipe(inv)) {
								h.setCustomName("Ender Hopper");
								h.update();
								inv.clear();

								e.getItemDrop().remove();
								active.add("x:" + h.getX() + " y:" + h.getY() + " z:" + h.getZ() + " w:" + h.getWorld().getName());
								mc.storage.data().set("EnderHoppers", active);
								mc.storage.saveData();
							}
						}
					}
				}
			}
		}, 0, 1L);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { // Remove Ender Hopper
		Block b = event.getBlock();
		List<String> active = mc.storage.data().getStringList("EnderHoppers");
		World w = b.getWorld();
		if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName())) {
			Location loc = b.getLocation().add(0.5, 0.5, 0.5);
			w.dropItem(loc, new ItemStack(Material.ENDER_EYE, 1));
			w.dropItem(loc, new ItemStack(Material.ENDER_PEARL, 4));
			w.dropItem(loc, new ItemStack(Material.IRON_BLOCK, 4));
			w.dropItem(loc, new ItemStack(Material.DIAMOND_BLOCK, 1));
			active.remove("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName());
			mc.storage.data().set("EnderHoppers", active);
			mc.storage.saveData();
		}
	}

	private boolean containsRecipe(Inventory inv) {
    	return (inv.getItem(0).getAmount() == 2 && inv.getItem(0).getType() == Material.ENDER_PEARL &&
				inv.getItem(1).getAmount() == 2 && inv.getItem(1).getType() == Material.IRON_BLOCK &&
				inv.getItem(2).getAmount() == 1 && inv.getItem(2).getType() == Material.DIAMOND_BLOCK &&
				inv.getItem(3).getAmount() == 2 && inv.getItem(3).getType() == Material.IRON_BLOCK &&
				inv.getItem(4).getAmount() == 2 && inv.getItem(4).getType() == Material.ENDER_PEARL);
	}
}
