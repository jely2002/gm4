package com.belka.spigot.gm4.crafting;

import api.SkullCreator;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class CustomItems {
	//TODO Handle custom item placing

	public static ItemStack CRAFTING_RECIPE_BOOK(int amount) {
		ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.GOLD + "Crafting Recipes");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.GRAY + "by " + ChatColor.DARK_AQUA + "Gamemode 4", ChatColor.GRAY + "Original")));
		item.setItemMeta(meta);
		return item;
	}

//	Heart Canisters
	public static ItemStack HEART_CANISTER_TIER_1(int amount) {
    	ItemStack skull = getSkull("http://textures.minecraft.net/texture/53cf06d5b47b746d16ee631187cb857d8c16bfcd570a40f286b2c38867cfda", amount);
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Heart Canister");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 1"));
        skull.setItemMeta(meta);
		return skull;
    }
	public static ItemStack HEART_CANISTER_TIER_2(int amount) {
    	ItemStack skull = getSkull("http://textures.minecraft.net/texture/455d9bf85ac565b35e1fa19247ca6541dc2e334bec115cb449efbe8e9b81022", amount);
    	ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + "Heart Canister");
        meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Tier 2"));
        skull.setItemMeta(meta);
		return skull;
    }

//  Equivalent Exchange
	public static ItemStack MINIUM_DUST(int amount) {
		ItemStack item = new ItemStack(Material.RED_DYE, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Minium Dust");
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack INERT_STONE(int amount) {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Inert Stone");
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack PHILOSOPHERS_STONE(int amount) {
		ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Philosopher's Stone");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Used to transmute things")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack PHILOSOPHERS_STONE_MKII(int amount) {
		ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Philosopher's Stone MkII");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Why not have more philosophy!")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack PHILOSOPHERS_STONE_MKIII(int amount) {
		ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Philosopher's Stone MkIII");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "I think this is going a little far...")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack PHILOSOPHERS_STONE_MKIV(int amount) {
		ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Philosopher's Stone MkIV");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "TOO MUCH PHILOSOPHY!!!")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack AC_ERROR(int amount) {
		ItemStack skull = getSkull("http://textures.minecraft.net/texture/967a2f218a6e6e38f2b545f6c17733f4ef9bbb288e75402949c052189ee", amount);
		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "ERROR");
		meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + ")@(^%%!)$)(&@%^&@"));
		skull.setItemMeta(meta);
		return skull;
	}
	public static ItemStack AC_INFINITY_TOOL(int amount) {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, Integer.MAX_VALUE, true);
		meta.setDisplayName(ChatColor.AQUA + "Infinity Tool");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "The equivalent of dividing by 0")));
		meta.setUnbreakable(true);

		AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 105, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);

		item.setItemMeta(meta);
		return item;
	}

//	Boots of Ostara
    public static ItemStack BOOTS_OF_OSTARA(int amount) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, amount);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(56, 140, 43));
        boots.setItemMeta(bootsMeta);
        return boots;
    }

//	Trapped Signs
	public static ItemStack OAK_TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.OAK_SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Oak Trapped Sign");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Place this item to make a trapped sign")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack SPRUCE_TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.SPRUCE_SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Spruce Trapped Sign");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Place this item to make a trapped sign")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack BIRCH_TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.BIRCH_SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Birch Trapped Sign");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Place this item to make a trapped sign")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack JUNGLE_TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.JUNGLE_SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Jungle Trapped Sign");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Place this item to make a trapped sign")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack ACACIA_TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.ACACIA_SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Acacia Trapped Sign");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Place this item to make a trapped sign")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack DARK_OAK_TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.DARK_OAK_SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Dark Oak Trapped Sign");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Place this item to make a trapped sign")));
		item.setItemMeta(meta);
		return item;
	}

//	Lightning rods
	public static ItemStack LIGHTNING_ROD(int amount) {
		ItemStack item = new ItemStack(Material.BLAZE_ROD, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Lightning rod");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.DARK_PURPLE + "Throw for boom!")));
		item.setItemMeta(meta);
		return item;
	}

//	Soul Glass
	public static ItemStack SOUL_GLASS(int amount) {
		ItemStack item = new ItemStack(Material.BROWN_STAINED_GLASS, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Soul Glass");
		item.setItemMeta(meta);
		return item;
	}

//	Soul Probes
	public static ItemStack SOUL_PROBES_BOOK(int amount) {
		ItemStack item = new ItemStack(Material.BOOK, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.GOLD + "Soul Probes");
		meta.setLore(new ArrayList<>(Arrays.asList(ChatColor.GRAY + "by " + ChatColor.DARK_AQUA + "Gamemode 4", ChatColor.GRAY + "Original")));
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack EMPTY_SPAWN_EGG(int amount) {
		ItemStack item = new ItemStack(Material.GHAST_SPAWN_EGG, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Empty Spawn Egg");
		item.setItemMeta(meta);
		return item;
	}

//	Ender Hopper
	public static ItemStack ENDER_HOPPER_SKULL(int amount) {
		ItemStack skull = getSkull("http://textures.minecraft.net/texture/78b9e215d3d8747c8df614fd8fe4d139de12124ffe19f2909d38cd4a732925d", amount);
		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName("Ender Hopper");
		skull.setItemMeta(meta);
		return skull;
	}

	private static ItemStack getSkull(String skinURL, int amount) {
		ItemStack skull = SkullCreator.itemFromUrl(skinURL);
		skull.setAmount(amount);
		return skull;
	}
}
