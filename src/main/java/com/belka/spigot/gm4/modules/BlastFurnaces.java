package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

import java.util.HashMap;

public class BlastFurnaces implements Listener {

	private static MainClass mc;
	public HashMap<Furnace, ArmorStand> activeBlastFurnace = new HashMap<>();

	public BlastFurnaces(MainClass mc) {
		this.mc = mc;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { // Create Blast Furnace
		if (!mc.getConfig().getBoolean("BlastFurnaces.enabled")) return;
		Block b = event.getBlock();
		Material mat = b.getType();
		if (mat != Material.IRON_BLOCK && mat != Material.FURNACE && mat != Material.GLASS) return;
		for (Entity e : Helper.getNearbyEntities(b.getLocation(), 2)) {
			if (e instanceof ArmorStand) {
				ArmorStand as = (ArmorStand) e;
				if (!as.getCustomName().equalsIgnoreCase("BlastFurnace")) return;
				Block north = b.getRelative(BlockFace.NORTH);
				Block northEast = b.getRelative(BlockFace.NORTH_EAST);
				Block northWest = b.getRelative(BlockFace.NORTH_WEST);
				Block east = b.getRelative(BlockFace.EAST);
				Block south = b.getRelative(BlockFace.SOUTH);
				Block southEast = b.getRelative(BlockFace.SOUTH_EAST);
				Block southWest = b.getRelative(BlockFace.SOUTH_WEST);
				Block west = b.getRelative(BlockFace.WEST);

				Block northUp = north.getRelative(BlockFace.UP);
				Block northEastUp = northEast.getRelative(BlockFace.UP);
				Block northWestUp = northWest.getRelative(BlockFace.UP);
				Block eastUp = east.getRelative(BlockFace.UP);
				Block southUp = south.getRelative(BlockFace.UP);
				Block southEastUp = southEast.getRelative(BlockFace.UP);
				Block southWestUp = southWest.getRelative(BlockFace.UP);
				Block westUp = west.getRelative(BlockFace.UP);
				if (northEast.getType() == Material.IRON_BLOCK && northWest.getType() == Material.IRON_BLOCK && southEast.getType() == Material.IRON_BLOCK && southWest.getType() == Material.IRON_BLOCK &&
				northEastUp.getType() == Material.IRON_BLOCK && northWestUp.getType() == Material.IRON_BLOCK && southEastUp.getType() == Material.IRON_BLOCK && southWestUp.getType() == Material.IRON_BLOCK) {
					if (north.getType() == Material.FURNACE && northUp.getType() == Material.GLASS &&
							east.getType() == Material.IRON_BLOCK && eastUp.getType() == Material.IRON_BLOCK &&
							south.getType() == Material.IRON_BLOCK && southUp.getType() == Material.IRON_BLOCK &&
							west.getType() == Material.IRON_BLOCK && westUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) north.getState();
						activeBlastFurnace.put(furnace, as);
					}
					else if (east.getType() == Material.FURNACE && eastUp.getType() == Material.GLASS &&
							south.getType() == Material.IRON_BLOCK && southUp.getType() == Material.IRON_BLOCK &&
							west.getType() == Material.IRON_BLOCK && westUp.getType() == Material.IRON_BLOCK &&
							north.getType() == Material.IRON_BLOCK && northUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) east.getState();
						activeBlastFurnace.put(furnace, as);
					}
					else if (south.getType() == Material.FURNACE && southUp.getType() == Material.GLASS &&
							west.getType() == Material.IRON_BLOCK && westUp.getType() == Material.IRON_BLOCK &&
							north.getType() == Material.IRON_BLOCK && northUp.getType() == Material.IRON_BLOCK &&
							east.getType() == Material.IRON_BLOCK && eastUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) south.getState();
						activeBlastFurnace.put(furnace, as);
					}
					else if (west.getType() == Material.FURNACE && westUp.getType() == Material.GLASS &&
							north.getType() == Material.IRON_BLOCK && northUp.getType() == Material.IRON_BLOCK &&
							east.getType() == Material.IRON_BLOCK && eastUp.getType() == Material.IRON_BLOCK &&
							south.getType() == Material.IRON_BLOCK && southUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) west.getState();
						activeBlastFurnace.put(furnace, as);
					}
				}
			}
		}
	}

	@EventHandler
	public void onSmelt(FurnaceSmeltEvent e) {
		Furnace furnace = (Furnace) e.getBlock().getState();
		if (activeBlastFurnace.containsKey(furnace)) {
			ArmorStand as = activeBlastFurnace.get(furnace);
			Block asBlock = as.getLocation().getBlock();
			if (asBlock.getType() == Material.HOPPER) {
				Hopper hopper = (Hopper) asBlock.getState();
				hopper.getInventory().addItem(e.getResult());
			}
		}
	}
}
