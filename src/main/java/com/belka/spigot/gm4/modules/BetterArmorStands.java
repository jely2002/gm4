package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class BetterArmorStands implements Listener {

	private MainClass mc;

	public BetterArmorStands(MainClass mc) {
		this.mc = mc;
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (!mc.getConfig().getBoolean("BetterArmorStands.enabled")) return;
		Item i = event.getItemDrop();
		ItemStack is = i.getItemStack();
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if (i.isOnGround()) {
				Bukkit.getScheduler().cancelTask(task[0]);
				for (Entity e : Helper.getNearbyEntities(i.getLocation(), 1)) {
					if (e instanceof ArmorStand) {
						Material itemMat = i.getItemStack().getType();
						ArmorStand as = (ArmorStand) e;
						if (itemMat == Material.STICK && is.getAmount() >= 2 && !as.hasArms()) {
							as.setArms(true);
							for (int x = 0; x < 2; x++) {
								e.remove();
							}
						}
						if (itemMat == Material.SHEARS && !as.isSmall()) {
							as.setSmall(true);
							Damageable itemMeta = (Damageable) is.getItemMeta();
							itemMeta.setDamage(itemMeta.getDamage() + 1);
							is.setItemMeta((ItemMeta) itemMeta);
						}
					}
				}
			}
		}, 0, 10L);
	}
}
