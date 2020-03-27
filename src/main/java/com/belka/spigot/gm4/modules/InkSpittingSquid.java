package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InkSpittingSquid implements Module {

	private MainClass mc;

	public InkSpittingSquid(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public void init(MainClass mc) {
		if(!mc.getStorage().config().getBoolean("InkSpittingSquid.enabled")) return;
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			for(World w : Bukkit.getWorlds()) {
				for(Entity entity : w.getEntities()) {
					if(entity instanceof Player) {
						Player p = (Player) entity;
						for(Entity e : Helper.getNearbyEntities(entity.getLocation(), 3)) {
							if(e instanceof Squid) {
								p.removePotionEffect(PotionEffectType.CONFUSION);
								p.removePotionEffect(PotionEffectType.BLINDNESS);
								p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5 * 20, 1, true));// Seconds * 20
								p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 1, true));// Seconds * 20
								p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1, 0);
								Advancements.grantAdvancement("natural_defences", p);
							}
						}
					}
				}
			}
		}, 0, 20L); // Seconds * 20L
	}
}
