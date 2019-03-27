package api;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InventoryCreator {

	public ItemStack createGuiItem(Material mat, String name, String... lores) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + name);
		List<String> loreArr = new ArrayList<>();
		ChatColor lcc = ChatColor.BLUE;
		int i = 0;
		for(String lore : lores) {
			if(i >= 1) lcc = ChatColor.GRAY;
			loreArr.add(lcc + lore);
			i++;
		}
		meta.setLore(loreArr);
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack createGuiItem(Material mat, ChatColor override, String name, String... lores) {
		ChatColor ccName = ChatColor.GOLD;
		ChatColor ccLore = ChatColor.BLUE;
		if(override != null) {
			ccName = override;
			ccLore = override;
		}
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ccName + name);
		List<String> loreArr = new ArrayList<>();
		for(String lore : lores) loreArr.add(ccLore + lore);
		meta.setLore(loreArr);
		item.setItemMeta(meta);
		return item;
	}
}
