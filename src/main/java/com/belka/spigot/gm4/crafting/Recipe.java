package com.belka.spigot.gm4.crafting;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Recipe {
	
	ItemStack blockToDrop;

	ArrayList<ItemStack> recipe;
	int amount;
	
	public Recipe(ItemStack item1, ItemStack item2, ItemStack item3, ItemStack item4, ItemStack item5, ItemStack item6, ItemStack item7, ItemStack item8, ItemStack item9, int amount, ItemStack itemToDrop) {
		ArrayList<ItemStack> recipe = new ArrayList<>();
		recipe.add(item1);
		recipe.add(item2);
		recipe.add(item3);
		recipe.add(item4);
		recipe.add(item5);
		recipe.add(item6);
		recipe.add(item7);
		recipe.add(item8);
		recipe.add(item9);
		this.recipe = recipe;
		this.amount = amount;
		this.blockToDrop = itemToDrop;
	}
	
	public Recipe(Material item1, Material item2, Material item3, Material item4, Material item5, Material item6, Material item7, Material item8, Material item9, int amount, ItemStack itemToDrop) {
		ArrayList<Material> recipe = new ArrayList<>();
		ArrayList<ItemStack> stacks = new ArrayList<>();
		recipe.add(item1);
		recipe.add(item2);
		recipe.add(item3);
		recipe.add(item4);
		recipe.add(item5);
		recipe.add(item6);
		recipe.add(item7);
		recipe.add(item8);
		recipe.add(item9);
		for(Material mat : recipe) {
			stacks.add(new ItemStack(mat));
		}
		this.recipe = stacks;
		this.amount = amount;
		this.blockToDrop = itemToDrop;
	}
	
	public ItemStack getBlockToDrop() {
		return blockToDrop;
	}
	public ArrayList<ItemStack> getRecipe() {
		return recipe;
	}
	public int getAmount() {
		return amount;
	}

}
