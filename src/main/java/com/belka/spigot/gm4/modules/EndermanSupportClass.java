package com.belka.spigot.gm4.modules;

import java.util.List;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EndermanSupportClass implements Listener {

	private MainClass mc;

	public EndermanSupportClass(MainClass mc) {
		this.mc = mc;
	}

	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("EndermanSupportClass")) {
					for(World w : Bukkit.getWorlds()) {
						for(Entity entity : w.getEntities()) {
							if(entity instanceof Enderman) {
								for(Entity e : mc.getNearbyEntities(entity.getLocation(), 25)) {
									if(e instanceof CaveSpider) {
										((CaveSpider) e).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 1), false);
										return;
									}
									else if(e instanceof Creeper) {
										((Creeper) e).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1), false);
										return;
									}
									else if(e instanceof Silverfish) {
										((Silverfish) e).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 1), false);
										return;
									}
									else if(e instanceof Skeleton) {
										for(Entity p : mc.getNearbyEntities(e.getLocation(), 4)) {
											if(p instanceof Player) {
												((Player) p).removePotionEffect(PotionEffectType.WEAKNESS);
												((Player) p).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 1), false);
											}
										}
										return;
									}
									else if(e instanceof Spider) {
										((Spider) e).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 0), false);
										return;
									}
									else if(e instanceof Zombie) {
										((Zombie) e).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1), false);
										return;
									}
									else if(e instanceof Player) {
										List<EntityType> mobs = mc.getNearbyEntityTypes(entity.getLocation(), 25);
										if(mobs.contains(EntityType.CAVE_SPIDER) ||	mobs.contains(EntityType.CREEPER) || mobs.contains(EntityType.SILVERFISH) || mobs.contains(EntityType.SKELETON) || mobs.contains(EntityType.SPIDER) ||	mobs.contains(EntityType.ZOMBIE)) {
											Advancements.grantAdvancement("gm4/ender_aid", (Player) e);
											return;
										}
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
