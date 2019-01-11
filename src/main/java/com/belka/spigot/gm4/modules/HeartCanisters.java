package com.belka.spigot.gm4.modules;

import java.util.Arrays;

import com.belka.spigot.gm4.crafting.CustomPlayerSkull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeartCanisters implements Listener {

	private MainClass mc;

	public HeartCanisters(MainClass mc) {
		this.mc = mc;
	}

	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("HeartCanisters") == true) {
					for(Entity entity : Bukkit.getWorld("world").getEntities()) {
						if(entity instanceof Item) {
							if(entity.isOnGround()) {
								ItemStack item = ((Item) entity).getItemStack();
								ItemMeta meta = item.getItemMeta();
								for(int amount = 1; amount < item.getAmount(); amount++) {
									ItemStack c = CustomPlayerSkull.getSkull("http://textures.minecraft.net/texture/53cf06d5b47b746d16ee631187cb857d8c16bfcd570a40f286b2c38867cfda", amount);
									ItemMeta cM = c.getItemMeta();
									cM.setDisplayName(ChatColor.WHITE + "Heart Cansister");
									cM.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 1"));
									c.setItemMeta(meta);
									if(item.equals(c)) {
										Bukkit.broadcastMessage("dsa");
										meta.setDisplayName(ChatColor.WHITE + "Heart Cansister");
										meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 1"));
										item.setItemMeta(meta);
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