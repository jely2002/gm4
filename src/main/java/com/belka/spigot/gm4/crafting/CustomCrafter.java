package com.belka.spigot.gm4.crafting;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class CustomCrafter implements Listener {
	
	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		Item i = e.getItemDrop();
		Location loc = i.getLocation();
		if (loc.add(0,-1,0).getBlock().getBlockData().getMaterial().equals(Material.DROPPER)) {
			if (i.getItemStack().getData().getItemType().equals(Material.CRAFTING_TABLE)) {

			}
		}
	}

}
