package com.belka.spigot.gm4.crafting;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class CustomPlayerSkull implements Listener {
	
	public static ItemStack getSkull(String skinURL, int amount) {
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
