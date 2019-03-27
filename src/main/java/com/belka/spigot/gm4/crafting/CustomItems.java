package com.belka.spigot.gm4.crafting;

import api.SkullCreator;
import de.tr7zw.itemnbtapi.NBTItem;
import net.minecraft.server.v1_13_R1.NBTTagCompound;
import net.minecraft.server.v1_13_R1.NBTTagList;
import net.minecraft.server.v1_13_R1.NBTTagString;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomItems {

//	Heart Canisters
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

//  Equivalent Exchange
	public static ItemStack MINIUM_DUST(int amount) {
		ItemStack item = new ItemStack(Material.ROSE_RED, amount);
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

//	Boots of Ostara
    public static ItemStack BOOTS_OF_OSTARA(int amount) {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, amount);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.fromRGB(56, 140, 43));
        boots.setItemMeta(bootsMeta);
        return boots;
    }

//	Trapped Signs
	public static ItemStack TRAPPED_SIGN(int amount) {
		ItemStack item = new ItemStack(Material.SIGN, amount);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setDisplayName(ChatColor.AQUA + "Trapped Sign");
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

//	Soul Probes
	public static ItemStack SOUL_PROBES_BOOK(int amount) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, amount);
		net.minecraft.server.v1_13_R1.ItemStack stack = CraftItemStack.asNMSCopy(book);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("title", "someBookTitle");
		tag.setString("author", "someAuthor");
		NBTTagList pages = new NBTTagList();
		pages.add(new NBTTagString("{text:\"Hello there!\",color:blue}"));
		pages.add(new NBTTagString("{text:\"Another page :O\"}"));
		tag.set("pages", pages);
		stack.setTag(tag);

		NBTItem nbtBook = new NBTItem(book);
		JSONParser parser = new JSONParser();
		String s = "[{\"Entity\":{\"id\":\"minecraft:\"}, \"Weight\":1}]";
		try {
			Object obj = parser.parse(s);
			JSONArray data = (JSONArray)obj;
			nbtBook.setObject("pages", data);
		} catch(ParseException pe) {
			System.out.println("Position: " + pe.getPosition());
		}

		return CraftItemStack.asCraftMirror(stack);
	}

	private static ItemStack getSkull(String skinURL, int amount) {
		ItemStack skull = SkullCreator.itemFromUrl(skinURL);
		skull.setAmount(amount);
		return skull;
	}
}
