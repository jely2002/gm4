package com.belka.spigot.gm4.config;

import api.InventoryCreator;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class SettingsGUI implements Listener {

	private MainClass mc;
	private HashMap<Player, Integer> paging = new HashMap<>();

	public SettingsGUI(MainClass mc) {
		this.mc = mc;
	}

	public Inventory settings(Player p, int page) {
		paging.put(p, page);
		page--;
		ArrayList<String> modules = new ArrayList<>(mc.getStorage().data().getConfigurationSection("settings").getKeys(false));
		java.util.Collections.sort(modules);

		double x = (double) modules.size() / 45;
		int pages = (int) Math.ceil(x);

		String pagesStr = "";
		if (modules.size() > 45) pagesStr = " (" + page + "/" + pages + ")";

		if (page >= 2) {
			if (45 * (page - 1) > 0) modules.subList(0, 45 * (page - 1)).clear();
		}
		int rows = (int) Math.ceil(modules.size() / 9.0);
		if (rows > 5) rows = 5;

		Inventory inv = Bukkit.createInventory(null, 9*(rows+1), ChatColor.DARK_AQUA + "Gamemode 4 Modules" + ChatColor.GRAY + pagesStr);

		if (page > 1)
			inv.setItem(9*rows + 2, InventoryCreator.createGuiItem(Material.SUNFLOWER, "Previous page", "Go back to page " + (page - 1)));
		if (page < pages && modules.size() > 45)
			inv.setItem(9*rows + 6, InventoryCreator.createGuiItem(Material.SUNFLOWER, "Next page", "Go to page " + (page + 1)));

		inv.setItem(9*rows + 4, InventoryCreator.createGuiItem(Material.BARRIER, "Close the Menu", "Click to close the settings menu"));

		for(int i = 0; i < modules.size(); i++) {
			if (i == 45) return inv;
			String module = modules.get(i);
			Material mat = Material.valueOf(mc.getStorage().data().getString("settings." + module + ".type"));
			String name = mc.getStorage().data().getString("settings." + module + ".name");
			String path = mc.getStorage().data().getString("settings." + module + ".path");
			ItemStack item = InventoryCreator.createGuiItem(mat, name, mc.getStorage().config().getBoolean(path + module + ".enabled"));
			inv.setItem(i, item);
		}

		return inv;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		HumanEntity entity = e.getWhoClicked();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (e.getView().getTitle().contains("Gamemode 4 Modules") && paging.containsKey(p)) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					if (clicked.getType() == Material.BARRIER) {
						p.closeInventory();
						p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
						paging.remove(p);
					}
					else if (clicked.getType() == Material.SUNFLOWER) {
						p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
						if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("previous page")) {
							p.openInventory(settings(p, paging.get(p) - 1));
						}
						if (ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).equalsIgnoreCase("next page")) {
							p.openInventory(settings(p, paging.get(p) + 1));
						}
					}
					else {
						String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
						ArrayList<String> modules = new ArrayList<>(mc.getStorage().data().getConfigurationSection("settings").getKeys(false));
						for (String module: modules) {
							if (mc.getStorage().data().contains("settings." + module + ".name")) {
								if (mc.getStorage().data().getString("settings." + module + ".name").equalsIgnoreCase(displayName)) {
									Material mat = Material.valueOf(mc.getStorage().data().getString("settings." + module + ".type"));
									String path = mc.getStorage().data().getString("settings." + module + ".path");
									if (mc.getStorage().config().getBoolean(path + module + ".enabled")) {
										mc.getStorage().config().set(path + module + ".enabled", false);
										mc.saveConfig();
										p.getOpenInventory().setItem(e.getSlot(), InventoryCreator.createGuiItem(mat, displayName, false));
										p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
										p.sendMessage(ChatColor.RED + "Disabled " + displayName + ".");
										mc.getLogger().log(Level.INFO, p.getName() + " disabled the module " + displayName);
									}
									else {
										mc.getStorage().config().set(path + module + ".enabled", true);
										mc.saveConfig();
										p.getOpenInventory().setItem(e.getSlot(), InventoryCreator.createGuiItem(mat, displayName, true));
										p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
										p.sendMessage(ChatColor.GREEN + "Enabled " + displayName + ".");
										mc.getLogger().log(Level.INFO, p.getName() + " enabled the module " + displayName);
									}
									for(Module m : mc.getModules()) {
										if (m.hasSetting() && m.getSetting().getName().equalsIgnoreCase(displayName)) {
											m.reload();
											break;
										}
									}
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (paging.containsKey(p)) {
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
			paging.remove(p);
		}
	}
}
