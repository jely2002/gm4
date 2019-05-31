package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class ZauberCauldrons implements Listener, Initializable {

	private MainClass mc;
	private List<String> active = new ArrayList<>();

	public ZauberCauldrons(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		active = mc.storage().data().getStringList("ZauberCauldrons");
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			for (String cauldron : active) {
				Location loc = Helper.locFromConfig(cauldron);
				BlockData data = loc.getBlock().getBlockData();
				if (data instanceof Levelled) {
					Levelled c = (Levelled) data;
					if (c.getLevel() == c.getMaximumLevel()) {
						for (Player p : Helper.getNearbyPlayers(loc, 10)) {
							p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc.add(0.5, 1, 0.5), 0, 0, 1, 0, 0.05, null);
						}
					}
				}
			}
		}, 0, 5L);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!mc.getConfig().getBoolean("ZauberCauldrons.enabled")) return;
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
		Block block = e.getTo().getBlock();
		Block c = null;
		if (block.getRelative(BlockFace.NORTH).getType() == Material.CAULDRON)
			c = block.getRelative(BlockFace.NORTH);
		else if (block.getRelative(BlockFace.EAST).getType() == Material.CAULDRON)
			c = block.getRelative(BlockFace.EAST);
		else if (block.getRelative(BlockFace.SOUTH).getType() == Material.CAULDRON)
			c = block.getRelative(BlockFace.SOUTH);
		else if (block.getRelative(BlockFace.WEST).getType() == Material.CAULDRON)
			c = block.getRelative(BlockFace.WEST);
		if (c != null) {
			List<String> active = mc.storage().data().getStringList("ZauberCauldrons");
			if (c.getRelative(BlockFace.DOWN).getType() == Material.FIRE) {
				BlockData data = block.getBlockData();
				if (data instanceof Levelled) {
					Levelled cauldron = (Levelled) data;
					if (cauldron.getLevel() == cauldron.getMaximumLevel()) {
						if (!active.contains("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName())) {
							active.add("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName());
							mc.storage().data().set("ZauberCauldrons", active);
							mc.storage().saveData();
							reload();
						}
					}
				}
			}
			else {
				if (active.contains("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName())) {
				
				}
			}
		}
	}

	private void reload() {
		active = mc.storage().data().getStringList("ZauberCauldrons");
	}
}
