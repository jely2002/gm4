package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
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
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ZauberCauldrons implements Listener, Initializable {

	private MainClass mc;
	private List<String> active = new ArrayList<>();

	public ZauberCauldrons(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		active = mc.storage.data().getStringList("ZauberCauldrons");
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
		if (!mc.getConfig().getBoolean("ZauberCauldrons.enabled")) return;
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
								mc.storage.data().set("ZauberCauldrons", active);
								mc.storage.saveData();
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
						mc.storage.data().set("ZauberCauldrons", active);
						mc.storage.saveData();
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
				mc.storage.data().set("ZauberCauldrons", active);
				mc.storage.saveData();
			}
		}
	}
}
