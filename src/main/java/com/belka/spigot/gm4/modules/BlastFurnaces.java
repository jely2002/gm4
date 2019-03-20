package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlastFurnaces implements Listener {

	private static MainClass mc;

	public BlastFurnaces(MainClass mc) {
		this.mc = mc;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { // Remove Custom Crafter
		if (!mc.getConfig().getBoolean("BlastFurnaces.enabled")) return;
		Block b = event.getBlock();
		Material mat = b.getType();
		if (mat != Material.IRON_BLOCK && mat != Material.FURNACE && mat != Material.GLASS) return;
		for (Entity e : Helper.getNearbyEntities(b.getLocation(), 2)) {
			if (e instanceof ArmorStand) {
				ArmorStand as = (ArmorStand) e;
				Location loc = as.getLocation();
			}
		}
	}
}
