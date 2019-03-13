package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BetterArmorStands implements Initializable {

	private MainClass mc;

	public BetterArmorStands(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if(mc.getConfig().getBoolean("modules.BetterArmorStands.enabled")) {
				for(Entity entity : Bukkit.getWorld("world").getEntities()) {
					if(entity instanceof Item) {
					    ItemStack item = ((Item) entity).getItemStack();
						if(entity.isOnGround()) {
							for(Entity e : Helper.getNearbyEntities(entity.getLocation(), 1)) {
								if(e instanceof ArmorStand) {
									Material itemMat = ((Item) entity).getItemStack().getType();
									ArmorStand as = (ArmorStand) e;
									if(itemMat == Material.STICK && item.getAmount() >= 2 && !as.hasArms()) {
										as.setArms(true);
										for(int i = 0; i < 2; i++) {
											entity.remove();
										}
									}
									if(itemMat == Material.SHEARS && !as.isSmall()) {
										as.setSmall(true);
                                        Damageable itemMeta = (Damageable) item.getItemMeta();
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
