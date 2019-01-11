package com.belka.spigot.gm4.modules;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InkSpittingSquid implements Listener {

	private MainClass mc;

	public InkSpittingSquid(MainClass mc) {
		this.mc = mc;
	}

	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("InkSpittingSquid") == true) {
					for(World w : Bukkit.getWorlds()) {
						for(Entity entity : w.getEntities()) {
							if(entity instanceof Player) {
								Player p = (Player) entity;
								for(Entity e : mc.getNearbyEntities(entity.getLocation(), 3)) {
									if(e instanceof Squid) {
										List<String> players = mc.getConfig().getStringList("achievements.InkSpittingSquid");
										if(!players.contains(p.getName())) {
											players.add(p.getName());
											mc.getConfig().set("achievements.InkSpittingSquid", players);
											mc.saveConfig();
										}
										p.removePotionEffect(PotionEffectType.CONFUSION);
										p.removePotionEffect(PotionEffectType.BLINDNESS);
										p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 1, true));// Seconds * 20
										p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 1, true));// Seconds * 20
										p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1, 0);
										Advancements.grantAdvancement("gm4/natural_defences", p);
									}
								}
							}
						}
					}
				}
			}
		}, 0, 1 * 20L); // Seconds * 20L
	}
}
