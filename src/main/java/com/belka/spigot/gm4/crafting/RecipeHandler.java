package com.belka.spigot.gm4.crafting;

import org.bukkit.Sound;
import org.bukkit.block.Dropper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHandler {

	public boolean equalsRecipe(Dropper dr, ShapedRecipe recipe) {
		ArrayList<ItemStack> singleDropperItems = new ArrayList<>();
		for (ItemStack is : dr.getInventory().getContents()) {
			if (is != null) is.setAmount(1);
			singleDropperItems.add(is);
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

	public void handleRecipe(Dropper dr, ShapedRecipe recipe) {
		if (equalsRecipe(dr, recipe)) {
			int amount = dr.getInventory().getItem(0).getAmount();
			dr.getInventory().clear();
			ArrayList<ItemStack> results = new ArrayList<>();
			ItemStack result = recipe.getResult();
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
			dr.getInventory().addItem(results.toArray(new ItemStack[]{}));
//			dr.getInventory().setItem(4, result);
			dr.getLocation().getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);

		}
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


