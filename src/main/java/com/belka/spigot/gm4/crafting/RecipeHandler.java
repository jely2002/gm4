package com.belka.spigot.gm4.crafting;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHandler implements Initializable {

	private static MainClass mc;
	private ArrayList<String> convertKeys = new ArrayList<>();

	public RecipeHandler(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		if (mc.getConfig().getBoolean("CustomCrafter.MasterCrafting")) convertKeys.add("master_crafter");
		if (mc.getConfig().getBoolean("BlastFurnaces.enabled")) convertKeys.add("blast_furnace");
		if (mc.getConfig().getBoolean("CustomCrafter.Disassembler")) convertKeys.add("disassembler");
		if (mc.getConfig().getBoolean("CustomCrafter.EquivalentExchange")) convertKeys.add("alchemical_crafter");
	}

	public void craft(Dropper dr, Player p) {
		for (ShapedRecipe recipe : CustomRecipes.allShapedRecipes)
			if (equalsRecipe(dr, recipe)) {
				if (dr.getCustomName().equalsIgnoreCase("Custom Crafter")) { // If it's a Custom Crafter
					if (convertKeys.contains(recipe.getKey().getKey())) convert(dr, recipe.getKey().getKey(), p);
				}
				int amount = getFirstAmount(new ArrayList<>(Arrays.asList(dr.getInventory().getContents())));
				dr.getInventory().clear();

				ItemStack result = recipe.getResult();
				if (result.getType() == Material.BARRIER) result.setType(Material.AIR);
				int totAmount = result.getAmount() * amount;
				result.setAmount(totAmount);
				if (result.getAmount() <= result.getMaxStackSize()) dr.getInventory().setItem(4, result);
				else dr.getInventory().addItem(result);
				dr.getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
			}
	}

	private void convert(Dropper dr, String convert, Player p) {
		for (Entity e : Helper.getNearbyEntities(dr.getLocation(), 1)) {
			if (e instanceof ArmorStand && e.getCustomName().equalsIgnoreCase("CustomCrafter")) {
				Bukkit.broadcastMessage("Convert to " + convert);
				ArmorStand as = (ArmorStand) e;
				ItemStack helmet = new ItemStack(Material.AIR);
				EulerAngle pose = new EulerAngle(0f, 0f, 0f);
				Vector loc = new Vector(0f, 0f, 0f);
				switch (convert) {
					case "master_crafter":
						dr.setCustomName("Master Crafter");
						dr.update();
						as.setCustomName("MasterCrafter");
						helmet.setType(Material.PISTON);
						pose = new EulerAngle(Helper.degToRad(180f), 0f, 0f);
						loc.setY(0.595f);
						Advancements.grantAdvancement("clever_crafting", p);
						break;
					case "blast_furnace":
						dr.getInventory().clear();

						Block block = dr.getBlock();
						block.setType(Material.HOPPER);
						Hopper hp = (Hopper) block.getState();
						hp.setCustomName("Blast Furnace Output");
						hp.update();
						as.setCustomName("BlastFurnace");
						helmet.setType(Material.AIR);
						Advancements.grantAdvancement("clever_smelting", p);
						break;
					case "disassembler":
						dr.setCustomName("Disassembler");
						dr.update();
						as.setCustomName("Disassembler");
						helmet.setType(Material.TNT);
						dr.getWorld().spawnParticle(Particle.LAVA, dr.getBlock().getLocation().add(0.5f, 0.75f, 0.5f), 10);
						dr.getWorld().playSound(dr.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						Advancements.grantAdvancement("clever_decrafting", p);
						break;
					case "alchemical_crafter":
						dr.setCustomName("Alchemical Crafter");
						dr.update();
						as.setCustomName("AlchemicalCrafter");
						helmet.setType(Material.REDSTONE_BLOCK);
						helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
						dr.getWorld().strikeLightningEffect(dr.getBlock().getLocation().add(0.5f, 1f, 0.5f));
						break;
				}
				p.closeInventory();
				as.setHelmet(helmet);
				as.setHeadPose(pose);
				Location asLoc = as.getLocation().add(loc);
				as.teleport(asLoc);
			}
		}
	}

	public boolean equalsRecipe(Dropper dr, ShapedRecipe recipe) {
		String customName = dr.getCustomName();
		String recipeName = recipe.getKey().getKey();
		ArrayList<String> rNames = new ArrayList<>();
		if (customName == null) {
			if (!recipeName.equalsIgnoreCase("create")) return false;
		}
		else
			switch (customName) {
				case "Custom Crafter":
					for (ShapedRecipe sr : CustomRecipes.ccShapedRecipes) {
						rNames.add(sr.getKey().getKey());
					}
					if (!rNames.contains(recipeName)) return false;
					break;
				case "Master Crafter":
					for (ShapedRecipe sr : CustomRecipes.mcShapedRecipes) {
						rNames.add(sr.getKey().getKey());
					}
					if (!rNames.contains(recipeName)) return false;
					break;
				case "Disassembler":
					for (ShapedRecipe sr : CustomRecipes.daShapedRecipes) {
						rNames.add(sr.getKey().getKey());
					}
					if (!rNames.contains(recipeName)) return false;
					break;
				case "Alchemical Crafter":
					for (ShapedRecipe sr : CustomRecipes.acShapedRecipes) {
						rNames.add(sr.getKey().getKey());
					}
					if (!rNames.contains(recipeName)) return false;
					break;
				default:
					if (!recipeName.equalsIgnoreCase("create")) return false;
					break;
			}

		ArrayList<ItemStack> singleDropperItems = new ArrayList<>();
		for (ItemStack is : dr.getInventory().getContents()) {
			if (is != null)  {
				ItemStack item = is.clone();
				item.setAmount(1);
				singleDropperItems.add(item);
			}
			else singleDropperItems.add(null);
		}
		ArrayList<ItemStack> recipeItems = new ArrayList<>();
		for (String chars : recipe.getShape()) {
			for (String character : chars.split("(?!^)")) {
				char c = character.charAt(0);
				ItemStack item = recipe.getIngredientMap().get(c);
				if (item != null) {
					if (item.getType() == Material.PLAYER_HEAD && recipe.getKey().equals(new NamespacedKey(mc, "HEART_CANISTER_TIER_2"))) item = CustomItems.HEART_CANISTER_TIER_1(1);
					else if (item.getType() == Material.RED_DYE && (recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE")) || recipe.getKey().equals(new NamespacedKey(mc, "alchemical_crafter")))) item = CustomItems.MINIUM_DUST(1);
					else if (item.getType() == Material.FIREWORK_STAR && recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE"))) item = CustomItems.INERT_STONE(1);
					else if (item.getType() == Material.REDSTONE_BLOCK && CustomRecipes.acShapedRecipes.contains(recipe) && recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE_MKII"))) item = CustomItems.PHILOSOPHERS_STONE(1);
					else if (item.getType() == Material.REDSTONE_BLOCK && CustomRecipes.acShapedRecipes.contains(recipe) && recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE_MKIII"))) item = CustomItems.PHILOSOPHERS_STONE_MKII(1);
					else if (item.getType() == Material.REDSTONE_BLOCK && CustomRecipes.acShapedRecipes.contains(recipe) && recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE_MKIV"))) item = CustomItems.PHILOSOPHERS_STONE_MKIII(1);
					else if (item.getType() == Material.REDSTONE_BLOCK && CustomRecipes.acShapedRecipes.contains(recipe) && recipe.getKey().equals(new NamespacedKey(mc, "AC_ERROR"))) item = CustomItems.PHILOSOPHERS_STONE_MKIV(1);
					else if (item.getType() == Material.REDSTONE_BLOCK && CustomRecipes.acShapedRecipes.contains(recipe)) item = CustomItems.PHILOSOPHERS_STONE(1);
				}
				recipeItems.add(item);
			}
		}
		if (singleDropperItems.equals(recipeItems)) {
			return equalAmount(new ArrayList<>(Arrays.asList(dr.getInventory().getContents())));
		}
		return false;
	}

	private boolean equalAmount(ArrayList<ItemStack> items) {
		int amount = getFirstAmount(items);
		for (ItemStack item : items) {
			if (item == null) continue;
			if (item.getAmount() != amount) return false;
			amount = item.getAmount();
		}
		return true;
	}

	private int getFirstAmount(ArrayList<ItemStack> items) {
		for (ItemStack item : items)
			if (item != null) return item.getAmount();
		return 0;
	}
}


