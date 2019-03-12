package com.belka.spigot.gm4.modules;

import java.util.Arrays;

import api.SkullCreator;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeartCanisters implements Initializable {

	private MainClass mc;

	public HeartCanisters(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		if(!mc.getConfig().getBoolean("modules.heartcanisters.enabled")) {
			return;
		}
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			public void run() {
					for (Entity entity : Bukkit.getWorld("world").getEntities()) {
						if (entity instanceof Item) {
							if (entity.isOnGround()) {
								ItemStack item = ((Item) entity).getItemStack();
								ItemMeta meta = item.getItemMeta();
								for (int amount = 1; amount < item.getAmount(); amount++) {
									ItemStack c = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/53cf06d5b47b746d16ee631187cb857d8c16bfcd570a40f286b2c38867cfda");
									c.setAmount(amount);
									ItemMeta cM = c.getItemMeta();
									cM.setDisplayName(ChatColor.WHITE + "Heart Cansister");
									cM.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 1"));
									c.setItemMeta(meta);
									if (item.equals(c)) {
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
		}, 0, 20L); // Seconds * 20L
	}
}
