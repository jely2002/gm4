package com.belka.spigot.gm4.modules;

import api.InventoryCreator;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class SoulProbes {

	private MainClass mc;
	private InventoryCreator ic;
	private HashMap<EntityType, Integer> hostileOverworld = new HashMap<EntityType, Integer>() {{
        put(EntityType.CREEPER, 100);
        put(EntityType.SKELETON, 100);
        put(EntityType.SPIDER, 100);
        put(EntityType.ZOMBIE, 100);
        put(EntityType.SLIME, 100);
        put(EntityType.ENDERMAN, 100);
        put(EntityType.WITCH, 50);
        put(EntityType.GUARDIAN, 50);
        put(EntityType.CAVE_SPIDER, 100);
        put(EntityType.SILVERFISH, 100);
        put(EntityType.ENDERMITE, 25);
    }};
    private HashMap<EntityType, Integer> hostileNether = new HashMap<EntityType, Integer>() {{
        put(EntityType.GHAST, 25);
        put(EntityType.MAGMA_CUBE, 50);
        put(EntityType.PIG_ZOMBIE, 100);
        put(EntityType.BLAZE, 100);
    }};
    private HashMap<EntityType, Integer> passive = new HashMap<EntityType, Integer>() {{
        put(EntityType.COW, 100);
        put(EntityType.MUSHROOM_COW, 25);
        put(EntityType.SHEEP, 100);
        put(EntityType.PIG, 100);
        put(EntityType.CHICKEN, 100);
        put(EntityType.RABBIT, 25);
        put(EntityType.HORSE, 25);
        put(EntityType.OCELOT, 25);
        put(EntityType.WOLF, 25);
        put(EntityType.SQUID, 50);
        put(EntityType.VILLAGER, 25);
    }};

	public SoulProbes(MainClass mc, InventoryCreator ic) {
		this.mc = mc;
		this.ic = ic;
	}

	private Inventory soulProbesMenu() {
		Inventory inv = Bukkit.createInventory(null, 9*3, ChatColor.GOLD + "Soul Probes Menu");

		inv.setItem(2, ic.createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Hostile", "Overworld"));
		inv.setItem(4, ic.createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Hostile", "Nether"));
		inv.setItem(6, ic.createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.DARK_GREEN, "Passive"));

		inv.setItem(21, ic.createGuiItem(Material.BARRIER, ChatColor.DARK_AQUA, "Go back", "Click to close the main menu"));

//		for(int i = 0; i < mobs.size(); i++) {
//			if(i == 45) {
//				return inv;
//			}
//			String achievement = mobs.get(i);
//			Material mat = Material.LIME_TERRACOTTA;
//			ChatColor override = null;
//			if(!has.contains(achievement)) {
//				mat = Material.CYAN_TERRACOTTA;
//				override = ChatColor.GRAY;
//			}
//			String name = mc.getConfig().getString("achievements." + achievement + ".name");
//			String lore = mc.getConfig().getString("achievements." + achievement + ".lore");
//			String reward = "Reward: " + mc.getConfig().getString("achievements." + achievement + ".reward");
//			inv.setItem(i, ic.createGuiItem(mat, override, name, lore, reward));
//		}

		return inv;
	}

	private Inventory mobMenu(Player p, String type) {
		HashMap<EntityType, Integer> mobs = new HashMap<>();
		String name = "";

		switch (type) {
			case "ho":
				name += ChatColor.RED + "";
				mobs = hostileOverworld;
				break;
			case "hn":
				name += ChatColor.RED + "";
				mobs = hostileNether;
				break;
			case "p":
				name += ChatColor.DARK_GREEN + "";
				mobs = passive;
				break;
		}
		name += ChatColor.BOLD;

		Inventory inv = Bukkit.createInventory(null, 9*4, name);

		inv.setItem(21, ic.createGuiItem(Material.BARRIER, ChatColor.DARK_AQUA, "Go back", "Click to go back to the main menu"));

		for (Map.Entry<EntityType, Integer> mob : mobs.entrySet()) {
			inv.addItem(ic.createGuiSkull(mob.getKey(), mob.getValue(), p));
		}

		return inv;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
			ItemStack hand = p.getInventory().getItemInMainHand();
			if (hand != null && hand.getType() == Material.BOOK) {
				p.openInventory(soulProbesMenu());
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		HumanEntity entity = e.getWhoClicked();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (e.getInventory().getName().toLowerCase().contains("soul probes")) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					switch (clicked.getType()) {
						case BARRIER:
							p.closeInventory();
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
							break;
						case PLAYER_HEAD:
							break;
					}
				}
			}
		}
	}
}
