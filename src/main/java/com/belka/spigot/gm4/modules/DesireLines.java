package com.belka.spigot.gm4.modules;

import java.util.Random;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class DesireLines implements Listener {

	private MainClass mc;

	public DesireLines(MainClass mc){
		this.mc = mc;
	}

    public int max = 50;
    public int amount = 2;
	public Random random = new Random();

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(mc.getConfig().getBoolean("modules.DesireLines.enabled")) {
			max = mc.getConfig().getInt("modules.DesireLines.max");
			amount = mc.getConfig().getInt("modules.DesireLines.amount");
			final Player p = e.getPlayer();
			if(e.getFrom().getBlock() == e.getTo().getBlock()) {
				return;
			}
			if(random.nextInt(max) <= amount) {
				Block below = p.getLocation().getBlock();
				if(below != null && (p.getGameMode() == GameMode.SURVIVAL ||  p.getGameMode() == GameMode.ADVENTURE)) {
					Material replace = below.getType();
					if(replace == Material.AIR) {
						below = p.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
					}
//					if(below.getType() == Material.GRASS_BLOCK || below.getType() == Material.DIRT || below.getType() == Material.SAND) {
					below.getWorld().spawnParticle(Particle.BLOCK_CRACK, below.getLocation().add(0.5, 0.5, 0.5), 10, below.getType());
//					}
//					if(replace == Material.TALL_GRASS || replace == Material.LEGACY_DOUBLE_PLANT || replace == Material.BROWN_MUSHROOM || replace == Material.RED_MUSHROOM || replace == Material.LEGACY_YELLOW_FLOWER || replace == Material.DEAD_BUSH) {
//						below.getWorld().spigot().playEffect(below.getLocation().add(0.5, 0.5, 0.5), Effect.TILE_BREAK, below, (int)below.getData(), 0.0f, 0.0f, 0.0f, 0.0f, 10, 16);
//						below.getWorld().dropItem(below.getLocation(), replace);
//					}
					replace = replacement(below.getType());
					below.setType(replace);
					below.getState().update();
				}
			}
		}
	}
	
	public Material replacement(Material i) {
		switch (i) {
			case GRASS_BLOCK:
				return Material.DIRT;
			case DIRT:
				return Material.COARSE_DIRT;
			case SAND:
				return Material.SANDSTONE;
			case TALL_GRASS:
				return Material.AIR;
			case BROWN_MUSHROOM:
				return Material.AIR;
			case RED_MUSHROOM:
				return Material.AIR;
			case DEAD_BUSH:
				return Material.AIR;
			case DANDELION:
				return Material.AIR;
			case POPPY:
				return Material.AIR;
			case BLUE_ORCHID:
				return Material.AIR;
			case ALLIUM:
				return Material.AIR;
			case AZURE_BLUET:
				return Material.AIR;
			case ORANGE_TULIP:
				return Material.AIR;
			case PINK_TULIP:
				return Material.AIR;
			case RED_TULIP:
				return Material.AIR;
			case WHITE_TULIP:
				return Material.AIR;
			case OXEYE_DAISY:
				return Material.AIR;
			case SUNFLOWER:
				return Material.AIR;
			case LILAC:
				return Material.AIR;
			case ROSE_BUSH:
				return Material.AIR;
			case PEONY:
				return Material.AIR;
			default:
				return i;
		}
	}
}
