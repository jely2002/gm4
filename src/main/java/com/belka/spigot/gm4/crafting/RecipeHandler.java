package com.belka.spigot.gm4.crafting;

import api.CustomBlock;
import api.CustomBlockType;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHandler {

	private static MainClass mc;
	private ArrayList<String> convertKeys = new ArrayList<>();

	public RecipeHandler(MainClass mc) {
		RecipeHandler.mc = mc;
		init(mc);
	}

	public void init(MainClass mc) {
		if (mc.getStorage().config().getBoolean("CustomCrafter.MasterCrafting")) convertKeys.add("master_crafter");
		if (mc.getStorage().config().getBoolean("BlastFurnaces.enabled")) convertKeys.add("blast_furnace");
		if (mc.getStorage().config().getBoolean("CustomCrafter.Disassembler")) convertKeys.add("disassembler");
		if (mc.getStorage().config().getBoolean("CustomCrafter.EquivalentExchange")) convertKeys.add("alchemical_crafter");
	}

	public void craft(Dropper dr, Player p) {
		for (ShapedRecipe recipe : CustomRecipes.allShapedRecipes)
			if (equalsRecipe(dr, recipe)) {
				int amount = getFirstAmount(new ArrayList<>(Arrays.asList(dr.getInventory().getContents())));
				dr.getInventory().clear();

				CustomBlock customBlock = CustomBlock.get(dr.getLocation());
				if (customBlock != null) {
					if (convertKeys.contains(recipe.getKey().getKey())) {//TODO check if can be converted from current
						p.getOpenInventory().close();
						CustomBlockType cbt = CustomBlockType.getById(recipe.getKey().getKey().replace("_", ""));
						assert cbt != null;
						customBlock.convert(cbt);
						switch (cbt) {
							case MASTER_CRAFTER:
								Advancements.grantAdvancement("clever_crafting", p);
								break;
							case BLAST_FURNACE:
								Advancements.grantAdvancement("clever_smelting", p);
								break;
							case DISASSEMBLER:
								Advancements.grantAdvancement("clever_decrafting", p);
								break;
						}
					}
					if (customBlock.getType() == CustomBlockType.ALCHEMICAL_CRAFTER && recipe.getKey().getKey().toLowerCase().startsWith("ee_")) {
						dr.getInventory().addItem(CustomItems.PHILOSOPHERS_STONE(1));
					}
					switch (customBlock.getType()) {//Sound effects TODO
						case CUSTOM_CRAFTER:
							dr.getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
							break;
						case ALCHEMICAL_CRAFTER:
							dr.getWorld().playSound(dr.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
							break;
					}
				}

				ItemStack result = recipe.getResult();
				if (result.getType() == Material.BARRIER) result.setType(Material.AIR);
				int totAmount = result.getAmount() * amount;
				result.setAmount(totAmount);
				if (result.getAmount() <= result.getMaxStackSize()) dr.getInventory().setItem(4, result);
				else dr.getInventory().addItem(result);
				break;
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
					else if (item.getType() == Material.PLAYER_HEAD && CustomRecipes.acShapedRecipes.contains(recipe) && recipe.getKey().equals(new NamespacedKey(mc, "AC_INFINITY_TOOL"))) item = CustomItems.AC_ERROR(1);
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


