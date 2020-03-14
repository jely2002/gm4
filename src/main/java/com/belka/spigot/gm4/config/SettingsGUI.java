package com.belka.spigot.gm4.config;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class SettingsGUI implements Listener {

	private Inventory inv;
	private MainClass mc;

	public SettingsGUI(MainClass mc) {
		this.mc = mc;
	}

	public void openInventory(Player p) {
		inv = Bukkit.createInventory(null, getInventorySize(), ChatColor.DARK_AQUA + "Gamemode 4 Modules");
		addModules();
		p.openInventory(inv);
	}

	private void addModules() {
		int count = 0;
		for(String module : mc.getStorage().config().getConfigurationSection("").getKeys(false)) {
			if (mc.getStorage().config().getBoolean(module + ".enabled")) {
				inv.setItem(count, createItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.DARK_GREEN + module.replaceAll("\\d+", "").replaceAll("(.)([A-Z])", "$1 $2")));
			}
			else {
				inv.setItem(count, createItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + module.replaceAll("\\d+", "").replaceAll("(.)([A-Z])", "$1 $2")));
			}
			count++;
		}
		inv.setItem(inv.getSize() - 1, createItem(Material.BARRIER, ChatColor.RED + "Close the settings"));
	}

	private ItemStack createItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> lore = new ArrayList<>();
		if (material == Material.BARRIER) {
			lore.add(ChatColor.GRAY + "Click to close the settings menu");
		}
		else {
			if (mc.getStorage().config().getBoolean(ChatColor.stripColor(name.replaceAll("\\s+","")) + ".enabled")) {
				lore.add(ChatColor.GRAY + "Click to disable this module");
			}
			else {
				lore.add(ChatColor.GRAY + "Click to enable this module");
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private ItemStack updateItem(String name, boolean state) {
		if (state) {
			ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RED + ChatColor.stripColor(name));
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "Click to enable this module");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
		else {
			ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN + ChatColor.stripColor(name));
			ArrayList<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "Click to disable this module");
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		}
	}

	private int getInventorySize() {
		int max = mc.getStorage().config().getConfigurationSection("").getKeys(false).size();
		if (max <= 0) return 9;
		int quotient = (int) Math.ceil(max / 9.0);
		return quotient * 9 + 9;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) return;
		Player p = (Player) e.getWhoClicked();
		ItemStack clicked = e.getCurrentItem();
		if (e.getView().getTitle().contains("Gamemode 4 Modules")) {
			if (clicked.getItemMeta() == null) return;
			String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
			if (mc.getStorage().config().getConfigurationSection("").getKeys(false).contains(displayName.replaceAll("\\s+",""))) {
				if (mc.getStorage().config().getBoolean(displayName.replaceAll("\\s+","") + ".enabled")) {
					p.sendMessage(ChatColor.RED + "Disabled " + displayName + ".");
					inv.setItem(e.getSlot(), updateItem(displayName, true));
				}
				else {
					p.sendMessage(ChatColor.GREEN + "Enabled " + displayName + ".");
					inv.setItem(e.getSlot() ,updateItem(displayName, false));
				}
				mc.getStorage().config().set(displayName.replaceAll("\\s+","") + ".enabled", !mc.getStorage().config().getBoolean(displayName.replaceAll("\\s+","") + ".enabled"));
				mc.saveConfig();
			}
			else if (clicked.getType() == Material.BARRIER) {
				p.closeInventory();
				p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
			}
			e.setCancelled(true);
		}
	}
}
