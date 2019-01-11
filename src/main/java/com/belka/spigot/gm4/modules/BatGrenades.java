package com.belka.spigot.gm4.modules;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class BatGrenades implements Listener {

	private MainClass mc;

	public BatGrenades(MainClass mc){
		this.mc = mc;
	}

	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("BatGrenades")) {
					for(World w : Bukkit.getWorlds()) {
						for(Entity entity : w.getEntities()) {
							if(entity instanceof Player) {
								Player p = (Player) entity;
								for(Entity e : mc.getNearbyEntities(entity.getLocation(), 7)) {
									if(e instanceof Bat) {
										p.playSound(e.getLocation(), Sound.ENTITY_BAT_AMBIENT, 2, 0);
									}
								}
								for(Entity e : mc.getNearbyEntities(entity.getLocation(), 3)) {
									if(e instanceof Bat && !e.isDead()) {
										if(p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL) {
											double x = e.getLocation().getX();
											double y = e.getLocation().getY();
											double z = e.getLocation().getZ();
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "minecraft:summon Creeper "+ x + " " + y + " " + z + " {CustomName:Bat,ExplosionRadius:1b,ignited:1b,Fuse:0s,ActiveEffects:[{Id:1,Amplifier:1,Duration:10,ShowParticles:0b}]}");
											Advancements.grantAdvancement("gm4/batboozled", p);
										}
									}
								}
							}
						}
					}
				}
			}
		}, 0, 1 * 20L);
	}
}
