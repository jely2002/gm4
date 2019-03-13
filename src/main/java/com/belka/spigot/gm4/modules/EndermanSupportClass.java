package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EndermanSupportClass implements Initializable {

	private MainClass mc;

	public EndermanSupportClass(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if(mc.getConfig().getBoolean("modules.EndermanSupportClass.enabled")) {
				for(World w : Bukkit.getWorlds()) {
					for(Entity entity : w.getEntities()) {
						if(entity instanceof Enderman) {
							for(Entity e : Helper.getNearbyEntities(entity.getLocation(), 25)) {
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
									for(Entity p : Helper.getNearbyEntities(e.getLocation(), 4)) {
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
									List<EntityType> mobs = Helper.getNearbyEntityTypes(entity.getLocation(), 25);
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
		}, 0, 20L); // Seconds * 20L
	}
}
