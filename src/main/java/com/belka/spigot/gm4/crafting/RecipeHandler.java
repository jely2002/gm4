package com.belka.spigot.gm4.crafting;

import api.*;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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

	public void craft(Dropper dr, @Nullable Player p) {
		for (Recipe recipe : CustomRecipes.allRecipes)
			if (equalsRecipe(dr, recipe)) {
				int amount = getFirstAmount(dr.getInventory().getContents());
				dr.getInventory().clear();

				CustomBlock customBlock = CustomBlock.get(dr.getLocation());
				if (customBlock != null) {
					if (convertKeys.contains(recipe.getKey().getKey())) {//TODO check if can be converted from current
						if (p != null) p.getOpenInventory().close();
						CustomBlockType cbt = CustomBlockType.getById(recipe.getKey().getKey().replace("_", ""));
						assert cbt != null;
						customBlock.convert(cbt);
						if (p != null) switch (cbt) {
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
					switch (customBlock.getType()) {//TODO Sound effects
						case CUSTOM_CRAFTER:
							dr.getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
							break;
						case ALCHEMICAL_CRAFTER:
							dr.getWorld().playSound(dr.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
							break;
					}
				}

				ItemStack result = recipe.getResult();
				if (result.getType() == Material.STRUCTURE_VOID) result.setType(Material.AIR);
				int totAmount = result.getAmount() * amount;
				result.setAmount(totAmount);
				if (result.getAmount() <= result.getMaxStackSize()) dr.getInventory().setItem(4, result);
				else dr.getInventory().addItem(result);
				break;
			}
	}

	public boolean equalsRecipe(Dropper dr, Recipe recipe) {
		String customName = dr.getCustomName();
		String recipeName = recipe.getKey().getKey();
		if (customName == null) {
			if (!recipeName.equalsIgnoreCase("create")) return false;
		}
		else
			switch (customName) {
				case "Custom Crafter":
					if (CustomRecipes.ccRecipes.stream().noneMatch(o -> o.getKey().getKey().equalsIgnoreCase(recipeName))) return false;
					break;
				case "Master Crafter":
					if (CustomRecipes.mcRecipes.stream().noneMatch(o -> o.getKey().getKey().equalsIgnoreCase(recipeName))) return false;
					break;
				case "Disassembler":
					if (CustomRecipes.daRecipes.stream().noneMatch(o -> o.getKey().getKey().equalsIgnoreCase(recipeName))) return false;
					break;
				case "Alchemical Crafter":
					if (CustomRecipes.acRecipes.stream().noneMatch(o -> o.getKey().getKey().equalsIgnoreCase(recipeName))) return false;
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
			else if (recipe instanceof ShapedRecipe) singleDropperItems.add(null);
		}
		ArrayList<ItemStack> recipeItems = new ArrayList<>();
		if (recipe instanceof ShapedRecipe) {
			ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
			for (String chars : shapedRecipe.getShape()) {
				for (String character : chars.split("(?!^)")) {
					char c = character.charAt(0);
					recipeItems.add(shapedRecipe.getIngredientMap().get(c));
				}
			}
		}
		else if (recipe instanceof ShapelessRecipe) {
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
			recipeItems.addAll(shapelessRecipe.getIngredientList());
		}
		if (singleDropperItems.equals(recipeItems)) {
			return equalAmount(dr.getInventory().getContents());
		}
		return false;
	}

	private boolean equalAmount(ItemStack[] items) {
		int amount = getFirstAmount(items);
		for (ItemStack item : items) {
			if (item == null) continue;
			if (item.getAmount() != amount) return false;
			amount = item.getAmount();
		}
		return true;
	}

	private int getFirstAmount(ItemStack[] items) {
		for (ItemStack item : items)
			if (item != null) return item.getAmount();
		return 0;
	}
}


