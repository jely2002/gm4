package com.belka.spigot.gm4.crafting;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class CustomItems {
	
    public ItemStack VORPEL_SWORD() {
    	ItemStack VORPEL_SWORD = new ItemStack(Material.DIAMOND_SWORD, 1, (short) 0);
        ItemMeta meta = VORPEL_SWORD.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Vorpel " + ChatColor.GOLD + "Sword");
        meta.setLore(Arrays.asList(ChatColor.YELLOW + "Decapitates mobs and players"));
        meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 3, false);
        meta.addEnchant(Enchantment.DURABILITY, 3, false);
        VORPEL_SWORD.setItemMeta(meta);
		return VORPEL_SWORD;
    }
    
	public ItemStack HEART_CANISTER_TIER_1(int amount) {
    	ItemStack SKULL = getSkull("http://textures.minecraft.net/texture/53cf06d5b47b746d16ee631187cb857d8c16bfcd570a40f286b2c38867cfda", amount);
        ItemMeta meta = SKULL.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Heart Cansister");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 1"));
        SKULL.setItemMeta(meta);
		return SKULL;
    }
	public ItemStack HEART_CANISTER_TIER_2(int amount) {
    	ItemStack SKULL = getSkull("http://textures.minecraft.net/texture/455d9bf85ac565b35e1fa19247ca6541dc2e334bec115cb449efbe8e9b81022", amount);
    	ItemMeta meta = SKULL.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Heart Cansister");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 2"));
        SKULL.setItemMeta(meta);
		return SKULL;
    }
	
	private ItemStack getSkull(String skinURL, int amount) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, amount, (short)3);
		if(skinURL.isEmpty())return head;
		
		ItemMeta headMeta = head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		Field profileField = null;
		try {
			profileField = headMeta.getClass().getDeclaredField("profile");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		profileField.setAccessible(true);
		try {
			profileField.set(headMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		head.setItemMeta(headMeta);
		return head;
	}
}
