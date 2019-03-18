package com.belka.spigot.gm4.crafting;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHandler {

	private static MainClass mc;

	public RecipeHandler(MainClass mc) {
		this.mc = mc;
	}

	public void craft(Dropper dr) {
		for (ShapedRecipe recipe : CustomRecipes.shapedRecipes)
			if (equalsRecipe(dr, recipe)) {
				if (dr.getCustomName().equalsIgnoreCase("Custom Crafter")) {
					String[] cases = {"master_crafter","blast_furnace","disassembler"};
					if (Arrays.asList(cases).contains(recipe.getKey().getKey())) {
						convert(dr, recipe.getKey().getKey());
					}
				}
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
				if (results.size() == 1) dr.getInventory().setItem(4, results.get(0));
				else dr.getInventory().addItem(results.toArray(new ItemStack[]{}));
				dr.getLocation().getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
			}
	}

	public void convert(Dropper dr, String convert) {
		for (Entity e : Helper.getNearbyEntities(dr.getLocation(), 1)) {
			if (e instanceof ArmorStand && e.getCustomName().equalsIgnoreCase("CustomCrafter")) {
				ArmorStand as = (ArmorStand) e;
				ItemStack helmet = new ItemStack(Material.AIR);
				EulerAngle pose = new EulerAngle(0f, 0f, 0f);
				Vector loc = new Vector(0f, 0f, 0f);
				switch (convert) {
					case "master_crafter":
						dr.setCustomName("Master Crafter");
						as.setCustomName("masterCrafter");
						helmet.setType(Material.PISTON);
						pose.setX(180f);
						loc.setY(0f);
						break;
					case "blast_furnace":
						Block block = dr.getBlock();
						block.setType(Material.HOPPER);
						Hopper hp = (Hopper) block.getState();
						hp.setCustomName("Blast Furnace Output");
						as.setCustomName("blastFurnace");
						helmet.setType(Material.AIR);
						break;
					case "disassembler":
						dr.setCustomName("Blast Furnace");
						as.setCustomName("disassembler");
						helmet.setType(Material.PISTON);
						pose.setX(180f);
						loc.setY(0f);
						break;
				}
				dr.update();
				as.setHelmet(helmet);
				as.setHeadPose(pose);
				Location asLoc = as.getLocation().add(loc);
				as.teleport(asLoc);
			}
		}
	}

	public boolean equalsRecipe(Dropper dr, ShapedRecipe recipe) {
		ArrayList<ItemStack> singleDropperItems = new ArrayList<>();
		for (ItemStack is : dr.getInventory().getContents()) {
			if (is != null)  {
				ItemStack item = new ItemStack(is.clone().getType());
				singleDropperItems.add(item);
			}
			else singleDropperItems.add(null);
		}
		ArrayList<ItemStack> recipeItems = new ArrayList<>();
		for (String chars : recipe.getShape()) {
			for (String character : chars.split("(?!^)")) {
				char c = character.charAt(0);
				ItemStack item = recipe.getIngredientMap().get(c);
				if (recipe.getIngredientMap().get(c).equals(new ItemStack(Material.PLAYER_HEAD))) {
					if (recipe.getKey().equals(new NamespacedKey(mc, "HEART_CANISTER_TIER_2"))) item = CustomItems.HEART_CANISTER_TIER_1(1);
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


