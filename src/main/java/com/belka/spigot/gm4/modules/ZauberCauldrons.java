package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class ZauberCauldrons implements Module, Listener{

	private MainClass mc;
	private List<String> active = new ArrayList<>();

	public ZauberCauldrons(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public void init(MainClass mc) {
		active = mc.getStorage().data().getStringList("ZauberCauldrons");
		checkCauldrons();
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
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (!mc.getStorage().config().getBoolean("ZauberCauldrons.enabled")) return;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) { //Start a Zauber Cauldron
			Block clicked = e.getClickedBlock();
			if (e.getMaterial() == Material.FLINT_AND_STEEL) {
				if (clicked.getType() == Material.NETHERRACK) {
					Block c = clicked.getRelative(0, 2, 0);
					if (c.getType() == Material.CAULDRON) {
						Levelled cauldron = (Levelled) c.getBlockData();
						if (cauldron.getLevel() == cauldron.getMaximumLevel()) {
							if (!active.contains("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName())) {
								active.add("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName());
								mc.getStorage().data().set("ZauberCauldrons", active);
								mc.getStorage().saveData();
							}
						}
					}
				}
			}
		}
		else if (e.getAction() == Action.LEFT_CLICK_BLOCK) { //Stop a Zauber Cauldron
			Block target = e.getPlayer().getTargetBlock(null, 5);
			if (target.getType() == Material.FIRE) {
				Block c = target.getRelative(0, 1, 0);
				if (c.getType() == Material.CAULDRON) {
					if (active.contains("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName())) {
						active.remove("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName());
						mc.getStorage().data().set("ZauberCauldrons", active);
						mc.getStorage().saveData();
					}
				}
			}
		}
	}

	private void checkCauldrons() {
		for (String cauldron : active) {
			Block c = Helper.locFromConfig(cauldron).getBlock();
			if (c.getRelative(BlockFace.DOWN).getType() != Material.FIRE) {
				active.remove("x:" + c.getX() + " y:" + c.getY() + " z:" + c.getZ() + " w:" + c.getWorld().getName());
				mc.getStorage().data().set("ZauberCauldrons", active);
				mc.getStorage().saveData();
			}
		}
	}
}
