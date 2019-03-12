package com.belka.spigot.gm4.crafting;

import api.SkullCreator;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CustomPlayerSkull implements Listener {
	
	public static ItemStack getSkull(String skinURL, int amount) {
		ItemStack skull = SkullCreator.itemFromUrl(skinURL);
		skull.setAmount(amount);
		return skull;
	}
}
