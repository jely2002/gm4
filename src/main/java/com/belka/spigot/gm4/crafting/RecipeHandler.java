package com.belka.spigot.gm4.crafting;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.block.Hopper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeHandler {

	private static MainClass mc;
	private ArrayList<String> cases = new ArrayList<>();

	public RecipeHandler(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		if (mc.getConfig().getBoolean("CustomCrafter.MasterCrafting")) cases.add("master_crafter");
		if (mc.getConfig().getBoolean("CustomCrafter.BlastFurnace")) cases.add("blast_furnace");
		if (mc.getConfig().getBoolean("CustomCrafter.Disassembler")) cases.add("disassembler");
		if (mc.getConfig().getBoolean("CustomCrafter.EquivalentExchange")) cases.add("alchemical_crafter");
	}

	public void craft(Dropper dr, Player p) {
		for (ShapedRecipe recipe : CustomRecipes.shapedRecipes)
			if (equalsRecipe(dr, recipe)) {
				if (dr.getCustomName().equalsIgnoreCase("Custom Crafter")) { // If it's a Custom Crafter
					if (cases.contains(recipe.getKey().getKey())) {
						convert(dr, recipe.getKey().getKey(), p);
					}
				}
				int amount = dr.getInventory().getItem(0).getAmount();
				dr.getInventory().clear();
				ArrayList<ItemStack> results = new ArrayList<>();
				ItemStack result = recipe.getResult();
				int max = result.getMaxStackSize();
				if (max == 0) max = 1;
				int totAmount = result.getAmount() * amount;
				if (totAmount <= max) {
					result.setAmount(totAmount);
					results.add(result);
				}
				else {
					Bukkit.broadcastMessage(">");
					int loop = (int) Math.ceil(totAmount / (max + 0.0));
					for (int i = 0; loop < i; i++) {
						Bukkit.broadcastMessage(loop + " i: " + i + " tot: " + totAmount + " max: " + max);
						if (totAmount > max) result.setAmount(max);
						else result.setAmount(totAmount);
						results.add(result);
						totAmount = totAmount - max;
					}
				}
				if (results.size() == 1) dr.getInventory().setItem(4, results.get(0));
				else dr.getInventory().addItem(results.toArray(new ItemStack[]{}));
				dr.getWorld().playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
			}
	}

	public void convert(Dropper dr, String convert, Player p) {
		for (Entity e : Helper.getNearbyEntities(dr.getLocation(), 1)) {
			if (e instanceof ArmorStand && e.getCustomName().equalsIgnoreCase("CustomCrafter")) {
				Bukkit.broadcastMessage("Convert to " + convert);
				ArmorStand as = (ArmorStand) e;
				ItemStack helmet = new ItemStack(Material.AIR);
				ItemMeta helmetMeta = helmet.getItemMeta();
				EulerAngle pose = new EulerAngle(0f, 0f, 0f);
				Vector loc = new Vector(0f, 0f, 0f);
				switch (convert) {
					case "master_crafter":
						dr.setCustomName("Master Crafter");
						as.setCustomName("MasterCrafter");
						helmet.setType(Material.PISTON);
						pose = new EulerAngle(Helper.degToRad(180f), 0f, 0f);
						loc.setY(0.595f);
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
						break;
					case "disassembler":
						dr.setCustomName("Disassembler");
						dr.update();
						as.setCustomName("Disassembler");
						helmet.setType(Material.TNT);
						dr.getWorld().spawnParticle(Particle.LAVA, dr.getBlock().getLocation().add(0.5f, 0.75f, 0.5f), 10);
						dr.getWorld().playSound(dr.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
						break;
					case "alchemical_crafter":
						dr.setCustomName("Alchemical Crafter");
						dr.update();
						as.setCustomName("AlchemicalCrafter");
						helmet.setType(Material.REDSTONE_BLOCK);
						helmetMeta.addEnchant(Enchantment.DURABILITY, 1, true);
						dr.getWorld().strikeLightningEffect(dr.getBlock().getLocation().add(0.5f, 1f, 0.5f));
						break;
				}
				p.closeInventory();
				helmet.setItemMeta(helmetMeta);
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
					else if (item.getType() == Material.ROSE_RED && (recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE")) || recipe.getKey().equals(new NamespacedKey(mc, "alchemical_crafter")))) item = CustomItems.MINIUM_DUST(1);
					else if (item.getType() == Material.FIREWORK_STAR && recipe.getKey().equals(new NamespacedKey(mc, "PHILOSOPHERS_STONE"))) item = CustomItems.INERT_STONE(1);
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


