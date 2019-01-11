package com.belka.spigot.gm4.crafting;

import com.belka.spigot.gm4.modules.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CustomRecipies implements Listener {

	private static MainClass mc;

	@SuppressWarnings("static-access")
	public CustomRecipies(MainClass mc){
		this.mc = mc;
	}
	
	public static void CRAFT(Dropper dr) {
		int amount = 0;
		for(int i = 1; i <= 64; i++) {
			if(mc.getConfig().getBoolean("options.CustomCrafter.StandardCrafting")) {
				if(GRAVEL(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.GRAVEL, amount, (byte) 0));
					playSound(dr);
				}
				else if(PACKED_ICE(dr, i)) {
					amount = 4 * i;
					dr.getInventory().clear();
					dr.getInventory().addItem(new ItemStack(Material.PACKED_ICE, amount, (byte) 0));
					playSound(dr);
				}
				else if(RED_SAND(dr, i)) {
					amount = 8 * i;
					dr.getInventory().clear();
					dr.getInventory().addItem(new ItemStack(Material.SAND, amount, (byte) 1));
					playSound(dr);
				}
				else if(SAND(dr, i, false)) {
					amount = 4 * i;
					dr.getInventory().clear();
					dr.getInventory().addItem(new ItemStack(Material.SAND, amount, (byte) 0));
					playSound(dr);
				}
				else if(SAND(dr, i, true)) {
					amount = 2 * i;
					dr.getInventory().clear();
					dr.getInventory().addItem(new ItemStack(Material.SAND, amount, (byte) 0));
					playSound(dr);
				}
				else if(COBWEB(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.WEB, amount, (byte) 0));
					playSound(dr);
				}
				else if(PODZOL(dr, i)) {
					amount = 3 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.DIRT, amount, (byte) 2));
					playSound(dr);
				}
				else if(HORSE_ARMOR(dr, i, "gold")) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.GOLD_BARDING, amount, (byte) 0));
					playSound(dr);
				}
				else if(HORSE_ARMOR(dr, i, "iron")) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.IRON_BARDING, amount, (byte) 0));
					playSound(dr);
				}
				else if(HORSE_ARMOR(dr, i, "diamond")) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.DIAMOND_BARDING, amount, (byte) 0));
					playSound(dr);
				}
				else if(NOTCH_APPLE(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.GOLDEN_APPLE, amount, (byte) 1));
					playSound(dr);
				}
			}
			if(mc.getConfig().getBoolean("options.CustomCrafter.RecordCrafting")) {
				if(RECORD_13(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.GOLD_RECORD, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_CAT(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.GREEN_RECORD, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_BLOCKS(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_3, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_CHIRP(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_4, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_FAR(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_5, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_MALL(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_6, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_MELLOHI(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_7, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_STAL(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_8, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_STRAD(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_9, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_WARD(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_10, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_11(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_11, amount, (byte) 0));
					playSound(dr);
				}
				else if(RECORD_WAIT(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, new ItemStack(Material.RECORD_12, amount, (byte) 0));
					playSound(dr);
				}
			}
			if(mc.getConfig().getBoolean("HeartCanisters")) {
				if(HEART_CANISTER_TIER_1(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, com.belka.spigot.gm4.modules.CustomItems.HEART_CANISTER_TIER_1(amount));
					playSound(dr);
				}
				if(HEART_CANISTER_TIER_2(dr, i)) {
					amount = 1 * i;
					dr.getInventory().clear();
					dr.getInventory().setItem(4, CustomItems.HEART_CANISTER_TIER_2(amount));
					playSound(dr);
				}
			}
			if(mc.getConfig().getBoolean("BlastFurnace")) {
				if(BLAST_FURNACE(dr)) {
					Bukkit.broadcastMessage("furnace");
				}
			}
		}
	}
	
	public static boolean CREATE(Dropper dr) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five == null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0)) && two.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0)) && three.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0)) &&
				four.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0)) && six.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0)) &&
				seven.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0)) && eight.equals(new ItemStack(Material.REDSTONE, 1, (byte) 0)) && nine.equals(new ItemStack(Material.COBBLESTONE, 1, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	//Standart Crafting
	public static boolean GRAVEL(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean PACKED_ICE(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.ICE, amount, (byte) 0)) && two.equals(new ItemStack(Material.ICE, amount, (byte) 0)) && three.equals(new ItemStack(Material.ICE, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.ICE, amount, (byte) 0)) && five.equals(new ItemStack(Material.SNOW_BLOCK, amount, (byte) 0)) && six.equals(new ItemStack(Material.ICE, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.ICE, amount, (byte) 0)) && eight.equals(new ItemStack(Material.ICE, amount, (byte) 0)) && nine.equals(new ItemStack(Material.ICE, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean SAND(Dropper dr, int amount, boolean slab) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one == null && two == null && three == null && four == null && five != null && six == null && seven == null && eight == null && nine == null) {
			if(slab == false) {
				if(five.equals(new ItemStack(Material.SANDSTONE, amount, (byte) 0))) {
					if(five.getAmount() == amount) {
						return true;
					}
				}
			}
			else {
				if(five.equals(new ItemStack(Material.STEP, amount, (byte) 1))) {
					if(five.getAmount() == amount) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean RED_SAND(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.SAND, amount, (byte) 0)) && two.equals(new ItemStack(Material.SAND, amount, (byte) 0)) && three.equals(new ItemStack(Material.SAND, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.SAND, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 1)) && six.equals(new ItemStack(Material.SAND, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.SAND, amount, (byte) 0)) && eight.equals(new ItemStack(Material.SAND, amount, (byte) 0)) && nine.equals(new ItemStack(Material.SAND, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean COBWEB(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.STRING, amount, (byte) 0)) && two.equals(new ItemStack(Material.STRING, amount, (byte) 0)) && three.equals(new ItemStack(Material.STRING, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.STRING, amount, (byte) 0)) && five.equals(new ItemStack(Material.SLIME_BALL, amount, (byte) 0)) && six.equals(new ItemStack(Material.STRING, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.STRING, amount, (byte) 0)) && eight.equals(new ItemStack(Material.STRING, amount, (byte) 0)) && nine.equals(new ItemStack(Material.STRING, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	public static boolean PODZOL(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one == null && two == null && three == null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if((four.equals(new ItemStack(Material.LEAVES, amount, (byte) 0)) ||
					four.equals(new ItemStack(Material.LEAVES, amount, (byte) 1)) ||
					four.equals(new ItemStack(Material.LEAVES, amount, (byte) 2)) ||
					four.equals(new ItemStack(Material.LEAVES, amount, (byte) 3)) ||
					four.equals(new ItemStack(Material.LEAVES_2, amount, (byte) 0)) ||
					four.equals(new ItemStack(Material.LEAVES_2, amount, (byte) 1))) &&
				
				(five.equals(new ItemStack(Material.LEAVES, amount, (byte) 0)) ||
					five.equals(new ItemStack(Material.LEAVES, amount, (byte) 1)) ||
					five.equals(new ItemStack(Material.LEAVES, amount, (byte) 2)) ||
					five.equals(new ItemStack(Material.LEAVES, amount, (byte) 3)) ||
					five.equals(new ItemStack(Material.LEAVES_2, amount, (byte) 0)) ||
					five.equals(new ItemStack(Material.LEAVES_2, amount, (byte) 1))) &&
				
				(six.equals(new ItemStack(Material.LEAVES, amount, (byte) 0)) ||
					six.equals(new ItemStack(Material.LEAVES, amount, (byte) 1)) ||
					six.equals(new ItemStack(Material.LEAVES, amount, (byte) 2)) ||
					six.equals(new ItemStack(Material.LEAVES, amount, (byte) 3)) ||
					six.equals(new ItemStack(Material.LEAVES_2, amount, (byte) 0)) ||
					six.equals(new ItemStack(Material.LEAVES_2, amount, (byte) 1))) &&
				
				seven.equals(new ItemStack(Material.GRASS, amount, (byte) 0)) && eight.equals(new ItemStack(Material.GRASS, amount, (byte) 0)) && nine.equals(new ItemStack(Material.GRASS, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	
	public static boolean HORSE_ARMOR(Dropper dr, int amount, String type) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two == null && three == null && four != null && five != null && six != null && seven != null && eight == null && nine != null) {
			if(type.equalsIgnoreCase("gold")) {
				if(one.equals(new ItemStack(Material.GOLD_INGOT, amount, (byte) 0)) &&
					four.equals(new ItemStack(Material.GOLD_INGOT, amount, (byte) 0)) && five.equals(new ItemStack(Material.LEATHER, amount, (byte) 0)) && six.equals(new ItemStack(Material.GOLD_INGOT, amount, (byte) 0)) &&
					seven.equals(new ItemStack(Material.GOLD_INGOT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.GOLD_INGOT, amount, (byte) 0))) {
					return true;
				}
				else {
					return false;
				}
			}
			else if(type.equalsIgnoreCase("iron")) {
				if(one.equals(new ItemStack(Material.IRON_INGOT, amount, (byte) 0)) &&
					four.equals(new ItemStack(Material.IRON_INGOT, amount, (byte) 0)) && five.equals(new ItemStack(Material.LEATHER, amount, (byte) 0)) && six.equals(new ItemStack(Material.IRON_INGOT, amount, (byte) 0)) &&
					seven.equals(new ItemStack(Material.IRON_INGOT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.IRON_INGOT, amount, (byte) 0))) {
					return true;
				}
				else {
					return false;
				}
			}
			else if(type.equalsIgnoreCase("diamond")) {
				if(one.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) &&
					four.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) && five.equals(new ItemStack(Material.LEATHER, amount, (byte) 0)) && six.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) &&
					seven.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) && nine.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0))) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}

	public static boolean NOTCH_APPLE(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) && two.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) && three.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) && five.equals(new ItemStack(Material.APPLE, amount, (byte) 0)) && six.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) && eight.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0)) && nine.equals(new ItemStack(Material.GOLD_BLOCK, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	//Record Crafting
	public static boolean RECORD_13(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 11)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_CAT(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 2)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_BLOCKS(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 14)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_CHIRP(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 1)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_FAR(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 10)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_MALL(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 5)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_MELLOHI(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 13)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_STAL(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 0)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_STRAD(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 15)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_WARD(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.EYE_OF_ENDER, amount, (byte) 0)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_11(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.COAL, amount, (byte) 0)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean RECORD_WAIT(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && two.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && three.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && five.equals(new ItemStack(Material.INK_SACK, amount, (byte) 12)) && six.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && eight.equals(new ItemStack(Material.FLINT, amount, (byte) 0)) && nine.equals(new ItemStack(Material.FLINT, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	//Heart Canisters
	public static boolean HEART_CANISTER_TIER_1(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.OBSIDIAN, amount, (byte) 0)) && two.equals(new ItemStack(Material.IRON_BLOCK, amount, (byte) 0)) && three.equals(new ItemStack(Material.OBSIDIAN, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) && five.equals(new ItemStack(Material.GOLDEN_APPLE, amount, (byte) 1)) && six.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.OBSIDIAN, amount, (byte) 0)) && eight.equals(new ItemStack(Material.IRON_BLOCK, amount, (byte) 0)) && nine.equals(new ItemStack(Material.OBSIDIAN, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static boolean HEART_CANISTER_TIER_2(Dropper dr, int amount) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four != null && five != null && six != null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.ENDER_STONE, amount, (byte) 0)) && two.equals(new ItemStack(Material.GOLDEN_APPLE, amount, (byte) 1)) && three.equals(new ItemStack(Material.ENDER_STONE, amount, (byte) 0)) &&
				four.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) && five.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.WHITE + "Heart Cansister") && five.getItemMeta().getLore().contains(ChatColor.DARK_PURPLE + "Tier 1") && six.equals(new ItemStack(Material.DIAMOND, amount, (byte) 0)) &&
				seven.equals(new ItemStack(Material.ENDER_STONE, amount, (byte) 0)) && eight.equals(new ItemStack(Material.NETHER_STAR, amount, (byte) 0)) && nine.equals(new ItemStack(Material.ENDER_STONE, amount, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	//Blast Furnace
	public static boolean BLAST_FURNACE(Dropper dr) {
		ItemStack one = dr.getInventory().getItem(0);
		ItemStack two = dr.getInventory().getItem(1);
		ItemStack three = dr.getInventory().getItem(2);
		ItemStack four = dr.getInventory().getItem(3);
		ItemStack five = dr.getInventory().getItem(4);
		ItemStack six = dr.getInventory().getItem(5);
		ItemStack seven = dr.getInventory().getItem(6);
		ItemStack eight = dr.getInventory().getItem(7);
		ItemStack nine = dr.getInventory().getItem(8);
		if(one != null && two != null && three != null && four == null && five != null && six == null && seven != null && eight != null && nine != null) {
			if(one.equals(new ItemStack(Material.IRON_FENCE, 1, (byte) 0)) && two.equals(new ItemStack(Material.IRON_BLOCK, 1, (byte) 0)) && three.equals(new ItemStack(Material.IRON_FENCE, 1, (byte) 0)) &&
				five.equals(new ItemStack(Material.PISTON_BASE, 1, (byte) 0)) &&
				seven.equals(new ItemStack(Material.REDSTONE_COMPARATOR, 1, (byte) 0)) && eight.equals(new ItemStack(Material.REDSTONE_TORCH_ON, 1, (byte) 0)) && nine.equals(new ItemStack(Material.REDSTONE_COMPARATOR, 1, (byte) 0))) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public static void playSound(Dropper dr) {
		for(Player p : mc.getNearbyPlayers(dr.getLocation(), 7)) {
			p.playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
		}
	}
}
