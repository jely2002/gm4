package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;

public class BetterArmorStands implements Listener {

	private MainClass mc;

	public BetterArmorStands(MainClass mc) {
		this.mc = mc;
	}

	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("BetterArmorStands") == true) {
					for(Entity entity : Bukkit.getWorld("world").getEntities()) {
						if(entity instanceof Item) {
							if(entity.isOnGround()) {
								for(Entity e : mc.getNearbyEntities(entity.getLocation(), 1)) {
									if(e instanceof ArmorStand) {
										Material itemMat = ((Item) entity).getItemStack().getType();
										ArmorStand as = (ArmorStand) e;
										if(itemMat == Material.STICK && ((Item) entity).getItemStack().getAmount() >= 2 && !as.hasArms()) {
											as.setArms(true);
											for(int i = 0; i < 2; i++) {
												((Item) entity).remove();
											}
										}
										if(itemMat == Material.SHEARS && !as.isSmall()) {
											as.setSmall(true);
											((Item) entity).getItemStack().setDurability((short) (((Item) entity).getItemStack().getDurability() + 1));
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
