package com.belka.spigot.gm4.crafting;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Dropper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHandler {

	public void craft(Dropper dr) {
		for (ShapedRecipe recipe : CustomRecipes.shapedRecipes) {
			if (equalsRecipe(dr, recipe)) {
				int amount = dr.getInventory().getItem(0).getAmount();
				dr.getInventory().clear();
				ArrayList<ItemStack> results = new ArrayList<>();
				ItemStack result = recipe.getResult();
				if (recipe.getResult().getType() == Material.PLAYER_HEAD) {
					if (recipe.getKey().getKey().equalsIgnoreCase("HEART_CANISTER_TIER_2")) {
						result = CustomItems.HEART_CANISTER_TIER_2(1);
					}
				}
				int max = result.getMaxStackSize();
				int totAmount = result.getAmount() * amount;
				if (totAmount == max) {
					result.setAmount(totAmount);
					results.add(result);
				}
				else if (totAmount > max) {
					while (totAmount > max) {
						result.setAmount(max);
						results.add(result);
						totAmount = totAmount - max;
					}
					if (totAmount < max) {
						result.setAmount(totAmount);
						results.add(result);
					}
				}
				else {
					result.setAmount(totAmount);
					results.add(result);
				}
				if (results.size() == 1) dr.getInventory().setItem(4, results.get(0));
				else dr.getInventory().addItem(results.toArray(new ItemStack[]{}));
				dr.getLocation().getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
			}
		}
	}

	public boolean equalsRecipe(Dropper dr, ShapedRecipe recipe) {
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
				recipeItems.add(recipe.getIngredientMap().get(c));
			}
		}
		if (singleDropperItems.equals(recipeItems)) {
			return equalAmount(new ArrayList<>(Arrays.asList(dr.getInventory().getContents())));
		}
		return false;
	}

	private boolean equalAmount(ArrayList<ItemStack> items) {
		int amount = items.get(0).getAmount();
		for (ItemStack item : items) {
			if (item == null) continue;
			if (item.getAmount() != amount) {
				return false;
			}
			amount = item.getAmount();
		}
		return true;
	}
}


