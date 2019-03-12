package com.belka.spigot.gm4.crafting;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Dropper;
import org.bukkit.inventory.ItemStack;

public class RecipeHandler {

	private ArrayList<Recipe> recipes = new ArrayList<>();
	private ArrayList<Integer> amounts = new ArrayList<>();
	
	private CustomItems customItems = new CustomItems();

	public boolean checkRecipe(Dropper dr) { //TODO Extract blocks in dropper here.
		ArrayList<ItemStack> items = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			items.add(dr.getInventory().getItem(i));
		}
		if(recipes.size() > 0) {
			for(Recipe r : recipes) {
				if(r.getRecipe() == items) {
					for(ItemStack item : items) {
						if(!(item.getType() == Material.AIR)) {
							amounts.add(item.getAmount());
						}
						
					}
					//Check if all amounts are equal.
					if (!(amounts.stream().distinct().limit(2).count() <= 1)) {
						return false;
					}
					else {
						int amount = amounts.get(0);
						dr.getInventory().clear();
						ItemStack itemToGive = new ItemStack(r.getBlockToDrop());
						itemToGive.setAmount(itemToGive.getAmount() * amount);
						dr.getInventory().setItem(4, itemToGive);
						dr.getLocation().getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void registerRecipes() {
		Recipe gravel = new Recipe(
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				Material.FLINT,
				1, //Amount
				new ItemStack(Material.GRAVEL)); //Item to be crafted
		recipes.add(gravel);
		
		Recipe packedIce = new Recipe(
				Material.ICE,
				Material.ICE,
				Material.ICE,
				Material.ICE,
				Material.SNOW_BLOCK,
				Material.ICE,
				Material.ICE,
				Material.ICE,
				Material.ICE,
				4, //Amount
				new ItemStack(Material.PACKED_ICE)); //Item to be crafted
		recipes.add(packedIce);
		
		Recipe redSand = new Recipe(
				new ItemStack(Material.SAND),
				new ItemStack(Material.SAND),
				new ItemStack(Material.SAND),
				new ItemStack(Material.SAND),
				new ItemStack(Material.INK_SAC, 1),
				new ItemStack(Material.SAND),
				new ItemStack(Material.SAND),
				new ItemStack(Material.SAND),
				new ItemStack(Material.SAND),
				8, //Amount
				new ItemStack(Material.SAND, 1)); //Item to be crafted
		recipes.add(redSand);
		
		Recipe sand4 = new Recipe(
				Material.AIR,
				Material.AIR,
				Material.AIR,
				Material.AIR,
				Material.SANDSTONE,
				Material.AIR,
				Material.AIR,
				Material.AIR,
				Material.AIR,
				4, //Amount
				new ItemStack(Material.SAND)); //Item to be crafted
		recipes.add(sand4);
		
		Recipe sand2 = new Recipe(
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.SANDSTONE_SLAB, 1),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				new ItemStack(Material.AIR),
				2, //Amount
				new ItemStack(Material.SAND)); //Item to be crafted
		recipes.add(sand2);
		
		Recipe cobweb = new Recipe(
				Material.STRING,
				Material.STRING,
				Material.STRING,
				Material.STRING,
				Material.SLIME_BALL,
				Material.STRING,
				Material.STRING,
				Material.STRING,
				Material.STRING,
				1, //Amount
				new ItemStack(Material.COBWEB)); //Item to be crafted
		recipes.add(cobweb);
		
		Recipe ironHorseArmor = new Recipe(
				Material.AIR,
				Material.AIR,
				Material.IRON_INGOT,
				Material.IRON_INGOT,
				Material.LEATHER,
				Material.IRON_INGOT,
				Material.IRON_INGOT,
				Material.AIR,
				Material.IRON_INGOT,
				1, //Amount
				new ItemStack(Material.IRON_HORSE_ARMOR)); //Item to be crafted
		recipes.add(ironHorseArmor);
		
		Recipe goldHorseArmor = new Recipe(
				Material.AIR,
				Material.AIR,
				Material.GOLD_INGOT,
				Material.GOLD_INGOT,
				Material.LEATHER,
				Material.GOLD_INGOT,
				Material.GOLD_INGOT,
				Material.AIR,
				Material.GOLD_INGOT,
				1, //Amount
				new ItemStack(Material.GOLDEN_HORSE_ARMOR)); //Item to be crafted
		recipes.add(goldHorseArmor);
		
		Recipe diamondHorseArmor = new Recipe(
				Material.AIR,
				Material.AIR,
				Material.DIAMOND,
				Material.DIAMOND,
				Material.LEATHER,
				Material.DIAMOND,
				Material.DIAMOND,
				Material.AIR,
				Material.DIAMOND,
				1, //Amount
				new ItemStack(Material.DIAMOND_HORSE_ARMOR)); //Item to be crafted
		recipes.add(diamondHorseArmor);
		
		Recipe heartCannister1 = new Recipe(
				new ItemStack(Material.OBSIDIAN), //0
				new ItemStack(Material.IRON_BLOCK), //1
				new ItemStack(Material.OBSIDIAN), //2
				new ItemStack(Material.DIAMOND),  //3
				new ItemStack(Material.PLAYER_HEAD, 1), //4
				new ItemStack(Material.DIAMOND), //5
				new ItemStack(Material.OBSIDIAN), //6
				new ItemStack(Material.BLAZE_ROD), //7
				new ItemStack(Material.OBSIDIAN), //8
				1, //Amount
				customItems.HEART_CANISTER_TIER_1(1)); //Item to be crafted
		recipes.add(heartCannister1);
		
		Recipe heartCannister2 = new Recipe(
				new ItemStack(Material.END_STONE), //0
				new ItemStack(Material.END_CRYSTAL), //1
				new ItemStack(Material.END_STONE), //2
				new ItemStack(Material.DIAMOND),  //3
				customItems.HEART_CANISTER_TIER_1(1), //4
				new ItemStack(Material.DIAMOND), //5
				new ItemStack(Material.END_STONE), //6
				new ItemStack(Material.NETHER_STAR), //7
				new ItemStack(Material.END_STONE), //8
				1, //Amount
				customItems.HEART_CANISTER_TIER_2(1)); //Item to be crafted
		recipes.add(heartCannister2);
		
	}

}


