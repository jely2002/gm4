package com.belka.spigot.gm4.modules;

import api.InventoryCreator;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoulProbes {

	private MainClass mc;
	private InventoryCreator ic;
	private HashMap<Player,Integer> paging = new HashMap<>();

	public SoulProbes(MainClass mc, InventoryCreator ic) {
		this.mc = mc;
		this.ic = ic;
	}

	private Inventory soulProbesMenu(Player p, int page) {
		paging.put(p, page);

		List<String> mobs = new ArrayList<>(mc.getConfig().getConfigurationSection("achievements").getKeys(false));

		String pagesStr = "";
		double x = (double) mobs.size() / 45;
		int pages = (int) Math.ceil(x);

		List<String> has = new ArrayList<>();
		for(String achievement : mobs) {
			if(mc.getConfig().getStringList("players." + p.getName() + ".achievements").contains(achievement)) {
				has.add(achievement);
			}
		}
		double y = (double) has.size() / mobs.size() * 100;
		double percent = round(y, 1);

		if(mobs.size() > 45) {
			pagesStr = " (" + page + "/" + pages + ")";
		}

		Inventory inv = Bukkit.createInventory(null, 9*6, ChatColor.GOLD + "Achievements " + ChatColor.GRAY + percent + "%" + pagesStr);

		if(page > 1) {
			inv.setItem(47, ic.createGuiItem(Material.SUNFLOWER, "Previous page", "Go back to page " + (page - 1)));
		}
		if(page < pages) {
			inv.setItem(51, ic.createGuiItem(Material.SUNFLOWER, "Next page", "Go to page " + (page + 1)));
		}

		inv.setItem(49, ic.createGuiItem(Material.BARRIER, "Go back", "Click to go back to the main menu"));

		if(page >= 2) {
			for(int i = 0; i < 45 * (page - 1); i++) {
				mobs.remove(0);
			}
		}

		for(int i = 0; i < mobs.size(); i++) {
			if(i == 45) {
				return inv;
			}
			String achievement = mobs.get(i);
			Material mat = Material.LIME_TERRACOTTA;
			ChatColor override = null;
			if(!has.contains(achievement)) {
				mat = Material.CYAN_TERRACOTTA;
				override = ChatColor.GRAY;
			}
			String name = mc.getConfig().getString("achievements." + achievement + ".name");
			String lore = mc.getConfig().getString("achievements." + achievement + ".lore");
			String reward = "Reward: " + mc.getConfig().getString("achievements." + achievement + ".reward");
			inv.setItem(i, ic.createGuiItem(mat, override, name, lore, reward));
		}

		return inv;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action action = event.getAction();
		if (action==Action.RIGHT_CLICK_BLOCK || action==Action.RIGHT_CLICK_AIR){
			ItemStack hand = p.getInventory().getItemInMainHand();
			if (hand != null && hand.getType() == Material.BOOK){
				p.openInventory(soulProbesMenu(p, 1));
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		HumanEntity entity = e.getWhoClicked();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (e.getInventory().getName().equals(ChatColor.GOLD + "MagicalParks")) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					switch (clicked.getType()) {
						case BEACON:
							if (!p.isInsideVehicle()) {
								World w = Bukkit.getWorld("world");
								double x = mc.getConfig().getDouble("spawn.X");
								double y = mc.getConfig().getDouble("spawn.Y");
								double z = mc.getConfig().getDouble("spawn.Z");
								Location l = new Location(w, x, y, z);
								l.setYaw(0);
								l.setPitch(0);

								p.teleport(l);
								p.sendMessage(ChatColor.GOLD + "You were teleported to " + ChatColor.BOLD + "Spawn!");
								p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
								p.closeInventory();
							}
							break;
						case PLAYER_HEAD:
							p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
							p.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
							p.closeInventory();
							break;
					}
				}
			}
			if (e.getInventory().getName().toLowerCase().contains("achievements")) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					if (clicked.getType() == Material.BARRIER) {
//						p.openInventory(mc.pm.mainMenu());
						p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
					}
					else if (clicked.getType() == Material.SUNFLOWER) {
						p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
						if(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("previous page")) {
//							p.openInventory(achievementMenu(p, paging.get(p) - 1));
						}
						if(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("next page")) {
//							p.openInventory(achievementMenu(p, paging.get(p) + 1));
						}
					}
				}
			}
		}
	}

	private double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}
}
