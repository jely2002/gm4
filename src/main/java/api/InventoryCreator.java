package api;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryCreator {

	public ItemStack createGuiItem(Material mat, ChatColor color, String name, String... lores) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(color + "" + ChatColor.BOLD + name);
		ArrayList<String> loreArr = new ArrayList<>();
		ChatColor lcc = ChatColor.GRAY;
		for(String lore : lores) {
			loreArr.add(lcc + lore);
		}
		meta.setLore(loreArr);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack createGuiSkull(EntityType mob, Integer amount, Player p) {
		String name = "";
		String url = "";
		switch (mob) {
			case CREEPER:
				name = "Creeper";
				url = "";
				break;
		}
		ItemStack item = SkullCreator.itemFromUrl(url);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "" + p.getStatistic(Statistic.MOB_KILLS) + "/" + amount);
		ArrayList<String> loreArr = new ArrayList<>(Arrays.asList(ChatColor.DARK_GRAY + name, ChatColor.GRAY + "Create " + name + " Spawn Egg"));
		meta.setLore(loreArr);
		item.setItemMeta(meta);
		return item;
	}
}
