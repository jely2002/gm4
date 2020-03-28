package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class DesireLines implements Module, Listener {

	private MainClass mc;
	private Random random = new Random();
	private int max = 50;
	private int amount = 2;

	public DesireLines(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public void init(MainClass mc) {
		max = mc.getStorage().config().getInt("DesireLines.max");
		amount = mc.getStorage().config().getInt("DesireLines.amount");
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(!mc.getStorage().config().getBoolean("DesireLines.enabled")) return;
		if(e.getFrom().getBlock() == e.getTo().getBlock()) return;

		Player p = e.getPlayer();
		if(random.nextInt(max) <= amount) {
			Block below = p.getLocation().getBlock();
			if(!p.isSneaking() && (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE)) {
				if (!hasBoots(p)) {
					Material replace = below.getType();
					if (replace == Material.AIR) {
						below = p.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
					} else if (isPlant(replace)) {
						Material drop = replace;
						if (drop == Material.TALL_GRASS) drop = Material.GRASS;
						else if (drop == Material.LARGE_FERN) drop = Material.FERN;
						below.getWorld().dropItemNaturally(below.getLocation().add(0.5, 0.5, 0.5), new ItemStack(drop, 1));
					}
					replace = getReplacement(below.getType());
					below.setType(replace);
					below.getState().update();
				}
			}
		}
	}
	
	private Material getReplacement(Material i) {
		switch (i) {
			case GRASS_BLOCK:
				return Material.DIRT;
			case DIRT:
				return Material.COARSE_DIRT;
			case SAND:
				return Material.SANDSTONE;
			default:
				if(isPlant(i)) {
					return Material.AIR;
				}
				return i;
		}
	}

	private boolean isPlant(Material i) {
		return (i == Material.BROWN_MUSHROOM || i == Material.RED_MUSHROOM || i == Material.DEAD_BUSH || i == Material.GRASS || i == Material.FERN ||
				i == Material.DANDELION || i == Material.POPPY || i == Material.BLUE_ORCHID || i == Material.ALLIUM || i == Material.AZURE_BLUET || i == Material.OXEYE_DAISY ||
				i == Material.ORANGE_TULIP || i == Material.PINK_TULIP || i == Material.RED_TULIP || i == Material.WHITE_TULIP ||
				i == Material.SUNFLOWER || i == Material.LILAC || i == Material.ROSE_BUSH || i == Material.PEONY || i == Material.TALL_GRASS || i == Material.LARGE_FERN);
	}

	private boolean hasBoots(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getBoots() == null) return false;
		return p.getInventory().getBoots().equals(CustomItems.BOOTS_OF_OSTARA(1));
	}
}
