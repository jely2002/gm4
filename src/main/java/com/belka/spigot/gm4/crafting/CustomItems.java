package com.belka.spigot.gm4.crafting;

import java.util.Arrays;

import api.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class CustomItems {
	
    public static ItemStack VORPEL_SWORD() {
    	ItemStack VORPEL_SWORD = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta meta = VORPEL_SWORD.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Vorpel " + ChatColor.GOLD + "Sword");
        meta.setLore(Arrays.asList(ChatColor.YELLOW + "Decapitates mobs and players"));
        meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, false);
        meta.addEnchant(Enchantment.DURABILITY, 3, false);
        VORPEL_SWORD.setItemMeta(meta);
		return VORPEL_SWORD;
    }
    
	public static ItemStack HEART_CANISTER_TIER_1(int amount) {
    	ItemStack skull = getSkull("http://textures.minecraft.net/texture/53cf06d5b47b746d16ee631187cb857d8c16bfcd570a40f286b2c38867cfda", amount);
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Heart Cansister");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 1"));
        skull.setItemMeta(meta);
		return skull;
    }
	public static ItemStack HEART_CANISTER_TIER_2(int amount) {
    	ItemStack skull = getSkull("http://textures.minecraft.net/texture/455d9bf85ac565b35e1fa19247ca6541dc2e334bec115cb449efbe8e9b81022", amount);
    	ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Heart Cansister");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 2"));
        skull.setItemMeta(meta);
		return skull;
    }
    public static ItemStack BOOTS_OF_OSTARA(int amount) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, amount);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(56, 140, 43));
        boots.setItemMeta(bootsMeta);
        return boots;
    }

	public static ItemStack getSkull(String skinURL, int amount) {
		ItemStack skull = SkullCreator.itemFromUrl(skinURL);
		skull.setAmount(amount);
		return skull;
	}
}
