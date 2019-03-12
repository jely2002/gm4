package com.belka.spigot.gm4.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

public class BetterFire implements Listener {
	//Based of cadenzatb's Better Fire
	private MainClass mc;

	public BetterFire(MainClass mc) {
		this.mc = mc;
	}

	ArrayList<Arrow> fireArrows = new ArrayList<Arrow>();
	HashMap<UUID, Integer> checkArrow = new HashMap<UUID, Integer>();
	ArrayList<Arrow> deleteArrows = new ArrayList<Arrow>();

	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("BetterFire")) {
					for (Arrow arrow : fireArrows) {
						if (checkArrow.containsKey(arrow.getUniqueId())) {
							if (checkArrow.get(arrow.getUniqueId()).intValue() == arrow.getTicksLived()) {
								deleteArrows.add(arrow);
								checkArrow.remove(arrow.getUniqueId());
							} else {
								checkArrow.put(arrow.getUniqueId(), arrow.getTicksLived());
							}
						} else {
							checkArrow.put(arrow.getUniqueId(), arrow.getTicksLived());
						}
						Location loc = arrow.getLocation();
						if(loc.getBlock().getType() == Material.AIR) {
							arrow.getLocation().getBlock().setType(Material.FIRE);
						}
					}
					if(deleteArrows.size() > 0) {
						for(Arrow arrow : deleteArrows) {
							fireArrows.remove(arrow);
						}
					}
				}
			}
		}, 0, (long) 0.5 * 20L);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow && mc.getConfig().getBoolean("BetterFire")) {
			final Arrow arrow = (Arrow)e.getEntity();
			if (arrow.getFireTicks() == 0) {
				return;
			}
			World world = arrow.getWorld();
			BlockIterator iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0.0, 4);
			Block hitBlock = null;
			while (iterator.hasNext()) {
				hitBlock = iterator.next();
				if (hitBlock.getType() != Material.AIR) break;
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(mc, new Runnable(){
				@Override
				public void run() {
					Location loc = arrow.getLocation();
					if (arrow.getFireTicks() == 0) {
						return;
					}
					if (loc.getBlock().getType() == Material.AIR) {
						arrow.getLocation().getBlock().setType(Material.FIRE);
						fireArrows.add(arrow);
					}
				}
			}, (long) 0.25 * 20L);
		}
	}

	@EventHandler
	public void onCreeperTakeDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Creeper && e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK && mc.getConfig().getBoolean("BetterFire")) {
			Creeper c = (Creeper)e.getEntity();
			c.getWorld().createExplosion(c.getLocation(), 2.5f, true);
			c.setHealth(0.0);
		}
	}
}
