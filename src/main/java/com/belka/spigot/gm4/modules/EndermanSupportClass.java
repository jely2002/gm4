package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EndermanSupportClass implements Initializable {

	public void init(MainClass mc) {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if(mc.getStorage().config().getBoolean("EndermanSupportClass.enabled")) {
				for(World w : Bukkit.getWorlds()) {
					for(Entity entity : w.getEntities()) {
						if(entity instanceof Enderman) {
							for(Entity e : Helper.getNearbyEntities(entity.getLocation(), 25)) {
								if(e instanceof CaveSpider) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,.25,0), 2, .25, .1, .25, 0.1, null);
									((CaveSpider) e).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80, 1, false, true));
								}
								else if(e instanceof Creeper) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,1,0), 2, .25, .1, .25, 0.1, null);
									((Creeper) e).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1, false, true));
								}
								else if(e instanceof Silverfish) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,.15,0), 1, .1, 0, .1, 0.1, null);
									((Silverfish) e).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 1, false, true));
								}
								else if(e instanceof Skeleton) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,1,0), 2, .25, .1, .25, 0.1, null);
									for(Entity p : Helper.getNearbyEntities(e.getLocation(), 7)) {
										if(p instanceof Player) {
											((Player) p).removePotionEffect(PotionEffectType.WEAKNESS);
											((Player) p).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 80, 1, false, true));
										}
									}
								}
								else if(e instanceof Spider) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,.25,0), 2, .5, .1, .5, 0.1, null);
									((Spider) e).addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80, 0, false, true));
								}
								else if(e instanceof Zombie) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,1,0), 2, .25, .1, .25, 0.1, null);
									((Zombie) e).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1, false, true));
								}
								else if(e instanceof Player) {
									List<EntityType> mobs = Helper.getNearbyEntityTypes(entity.getLocation(), 25);
									if(mobs.contains(EntityType.CAVE_SPIDER) ||	mobs.contains(EntityType.CREEPER) || mobs.contains(EntityType.SILVERFISH) || mobs.contains(EntityType.SKELETON) || mobs.contains(EntityType.SPIDER) ||	mobs.contains(EntityType.ZOMBIE)) {
										Advancements.grantAdvancement("ender_aid", (Player) e);
									}
								}
								else if(e instanceof Shulker) {
									e.getWorld().spawnParticle(Particle.PORTAL, e.getLocation().add(0,1,0), 2, .25, .1, .25, 0.1, null);
									for(Entity p : Helper.getNearbyEntities(e.getLocation(), 7)) {
										if(p instanceof Player) {
											((Player) p).removePotionEffect(PotionEffectType.BLINDNESS);
											((Player) p).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, false, true));
										}
									}
								}
							}
						}
					}
				}
			}
		}, 0, 20L);
	}
}
