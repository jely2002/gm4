package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class DesireLines implements Listener {

	private MainClass mc;

	public DesireLines(MainClass mc) {
		this.mc = mc;
	}

	private Random random = new Random();

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(mc.getConfig().getBoolean("DesireLines.enabled")) {
			int max = mc.getConfig().getInt("DesireLines.max");
			int amount = mc.getConfig().getInt("DesireLines.amount");
			final Player p = e.getPlayer();
			if(e.getFrom().getBlock() == e.getTo().getBlock()) {
				return;
			}
			if(random.nextInt(max) <= amount) {
				Block below = p.getLocation().getBlock();
				if(below != null && (p.getGameMode() == GameMode.SURVIVAL ||  p.getGameMode() == GameMode.ADVENTURE) && !p.isSneaking()) {
					Material replace = below.getType();
					if(replace == Material.AIR) {
						below = p.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
					}
					if(plant(replace)) {
						Material drop = replace;
						if (drop == Material.TALL_GRASS) drop = Material.GRASS;
						else if (drop == Material.LARGE_FERN) drop = Material.FERN;
						below.getWorld().dropItemNaturally(below.getLocation().add(0.5, 0.5, 0.5), new ItemStack(drop, 1));
					}

					replace = replacement(below.getType());
					below.setType(replace);
					below.getState().update();
				}
			}
		}
	}
	
	private Material replacement(Material i) {
		switch (i) {
			case GRASS_BLOCK:
				return Material.DIRT;
			case DIRT:
				return Material.COARSE_DIRT;
			case SAND:
				return Material.SANDSTONE;
			default:
				if(plant(i)) {
					return Material.AIR;
				}
				return i;
		}
	}

	private boolean plant(Material i) {
		return (i == Material.BROWN_MUSHROOM || i == Material.RED_MUSHROOM || i == Material.DEAD_BUSH || i == Material.GRASS || i == Material.FERN ||
				i == Material.DANDELION || i == Material.POPPY || i == Material.BLUE_ORCHID || i == Material.ALLIUM || i == Material.AZURE_BLUET || i == Material.OXEYE_DAISY ||
				i == Material.ORANGE_TULIP || i == Material.PINK_TULIP || i == Material.RED_TULIP || i == Material.WHITE_TULIP ||
				i == Material.SUNFLOWER || i == Material.LILAC || i == Material.ROSE_BUSH || i == Material.PEONY || i == Material.TALL_GRASS || i == Material.LARGE_FERN);
	}
}
