package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
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
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BetterFire implements Listener, Initializable {
	//Based of cadenzatb's Better Fire
	private MainClass mc;

	public BetterFire(MainClass mc) {
		this.mc = mc;
	}

	private ArrayList<Arrow> fireArrows = new ArrayList<>();
	private HashMap<UUID, Integer> checkArrow = new HashMap<>();
	private ArrayList<Arrow> deleteArrows = new ArrayList<>();

	public void init(MainClass mc) {
		if(!mc.getConfig().getBoolean("BetterFire.enabled")) return;
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			for (Arrow arrow : fireArrows) {
				if (checkArrow.containsKey(arrow.getUniqueId())) {
					if (checkArrow.get(arrow.getUniqueId()) == arrow.getTicksLived()) {
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

		}, 0, 10L);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		if(!mc.getConfig().getBoolean("BetterFire.enabled")) return;
		if (e.getEntity() instanceof Arrow) {
			final Arrow arrow = (Arrow)e.getEntity();
			if (arrow.getFireTicks() == 0) {
				return;
			}
			World world = arrow.getWorld();
			BlockIterator iterator = new BlockIterator(world, arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0.0, 4);
			Block hitBlock;
			while (iterator.hasNext()) {
				hitBlock = iterator.next();
				if (hitBlock.getType() != Material.AIR) break;
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(mc, () -> {
				Location loc = arrow.getLocation();
				if (arrow.getFireTicks() == 0) {
					return;
				}
				if (loc.getBlock().getType() == Material.AIR) {
					arrow.getLocation().getBlock().setType(Material.FIRE);
					fireArrows.add(arrow);
				}
			}, 10L);
		}
	}

	@EventHandler
	public void onCreeperTakeDamage(EntityDamageEvent e) {
		if(!mc.getConfig().getBoolean("BetterFire.enabled")) return;
		if (e.getEntity() instanceof Creeper && e.getCause() == DamageCause.FIRE_TICK) {
			Creeper c = (Creeper)e.getEntity();
			c.getWorld().createExplosion(c.getLocation(), 2.5f, true);
			c.setHealth(0.0);
		}
	}
}
