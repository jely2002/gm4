package com.belka.spigot.gm4.crafting;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomRecipes implements Initializable {

	private static MainClass mc;
	public static ArrayList<ShapedRecipe> shapedRecipes = new ArrayList<>();
	public static ArrayList<ShapelessRecipe> shapelessRecipes = new ArrayList<>();

	public CustomRecipes(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		if (mc.getConfig().getBoolean("CustomCrafter.StandardCrafting")) {
			add(shapedRecipes, master_crafter(), blast_furnace(), disassembler(), GRAVEL(), RED_SAND(), COBWEB(), IRON_HORSE_ARMOR(), GOLDEN_HORSE_ARMOR(), DIAMOND_HORSE_ARMOR(),
					OAK_PLANKS(), SPRUCE_PLANKS(), BIRCH_PLANKS(), JUNGLE_PLANKS(), ACACIA_PLANKS(), DARK_OAK_PLANKS(),
					ENCHANTED_GOLDEN_APPLE(), SMOOTH_STONE(), SMOOTH_SANDSTONE(), SMOOTH_RED_SANDSTONE(), SMOOTH_QUARTZ());
			add(shapelessRecipes, SAND(), SAND_2());
		}
		if (mc.getConfig().getBoolean("CustomCrafter.RecordCrafting"))
			add(shapedRecipes, MUSIC_DISC_11(), MUSIC_DISC_13(), MUSIC_DISC_BLOCKS(), MUSIC_DISC_CAT(), MUSIC_DISC_CHIRP(), MUSIC_DISC_FAR(), MUSIC_DISC_MALL(), MUSIC_DISC_MELLOHI(), MUSIC_DISC_STAL(), MUSIC_DISC_STRAD(), MUSIC_DISC_WAIT(), MUSIC_DISC_WARD());
		if (mc.getConfig().getBoolean("HeartCanisters.enabled"))
			add(shapedRecipes, HEART_CANISTER_TIER_1(), HEART_CANISTER_TIER_2());
	}

//	CREATION
	public static ShapedRecipe create() {
		ItemStack returnItem = new ItemStack(Material.AIR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "create"), returnItem);
		recipe.shape("CCC","C C","CRC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}
	//	Mastercraftsman's Workbench
	public static ShapedRecipe master_crafter() {
		ItemStack returnItem = new ItemStack(Material.AIR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "master_crafter"), returnItem);
		recipe.shape("CPC","P P","RFR");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('R', Material.COMPARATOR);
		recipe.setIngredient('F', Material.FURNACE);
		return recipe;
	}
	//	Blast Furnace
	public static ShapedRecipe blast_furnace() {
		ItemStack returnItem = new ItemStack(Material.AIR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "blast_furnace"), returnItem);
		recipe.shape("IBI"," P ","CTC");
		recipe.setIngredient('I', Material.IRON_BARS);
		recipe.setIngredient('B', Material.IRON_BLOCK);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('C', Material.COMPARATOR);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		return recipe;
	}
	//	Disassembler
	public static ShapedRecipe disassembler() {
		ItemStack returnItem = new ItemStack(Material.AIR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "disassembler"), returnItem);
		recipe.shape("CCC","CTC","CRC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('T', Material.TNT);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}

//	StandardCrafting
	public static ShapedRecipe GRAVEL() {
		ItemStack returnItem = new ItemStack(Material.GRAVEL, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "GRAVEL"), returnItem);
		recipe.shape("FFF","FFF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		return recipe;
	}
	public static ShapedRecipe RED_SAND() {
		ItemStack returnItem = new ItemStack(Material.RED_SAND, 8);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "RED_SAND"), returnItem);
		recipe.shape("SSS","SRS","SSS");
		recipe.setIngredient('S', Material.SAND);
		recipe.setIngredient('R', Material.ROSE_RED);
		return recipe;
	}
	public static ShapedRecipe COBWEB() {
		ItemStack returnItem = new ItemStack(Material.COBWEB, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COBWEB"), returnItem);
		recipe.shape("SSS","SBS","SSS");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('B', Material.SLIME_BALL);
		return recipe;
	}
	public static ShapedRecipe IRON_HORSE_ARMOR() {
		ItemStack returnItem = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "IRON_HORSE_ARMOR"), returnItem);
		recipe.shape("  I","ILI","I I");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('L', Material.LEATHER);
		return recipe;
	}
	public static ShapedRecipe GOLDEN_HORSE_ARMOR() {
		ItemStack returnItem = new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "GOLDEN_HORSE_ARMOR"), returnItem);
		recipe.shape("  I","ILI","I I");
		recipe.setIngredient('I', Material.GOLD_INGOT);
		recipe.setIngredient('L', Material.LEATHER);
		return recipe;
	}
	public static ShapedRecipe DIAMOND_HORSE_ARMOR() {
		ItemStack returnItem = new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "DIAMOND_HORSE_ARMOR"), returnItem);
		recipe.shape("  I","ILI","I I");
		recipe.setIngredient('I', Material.DIAMOND);
		recipe.setIngredient('L', Material.LEATHER);
		return recipe;
	}
	public static ShapedRecipe OAK_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.OAK_PLANKS, 6);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "OAK_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.OAK_STAIRS);
		return recipe;
	}
	public static ShapedRecipe SPRUCE_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.SPRUCE_PLANKS, 6);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SPRUCE_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.SPRUCE_STAIRS);
		return recipe;
	}
	public static ShapedRecipe BIRCH_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.BIRCH_PLANKS, 6);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "BIRCH_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.BIRCH_STAIRS);
		return recipe;
	}
	public static ShapedRecipe JUNGLE_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.JUNGLE_PLANKS, 6);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "JUNGLE_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.JUNGLE_STAIRS);
		return recipe;
	}
	public static ShapedRecipe ACACIA_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.ACACIA_PLANKS, 6);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "ACACIA_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.ACACIA_STAIRS);
		return recipe;
	}
	public static ShapedRecipe DARK_OAK_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.DARK_OAK_PLANKS, 6);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "DARK_OAK_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.DARK_OAK_STAIRS);
		return recipe;
	}
	public static ShapedRecipe ENCHANTED_GOLDEN_APPLE() {
		ItemStack returnItem = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "ENCHANTED_GOLDEN_APPLE"), returnItem);
		recipe.shape("GGG","GAG","GGG");
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('A', Material.APPLE);
		return recipe;
	}
	public static ShapedRecipe SMOOTH_STONE() {
		ItemStack returnItem = new ItemStack(Material.SMOOTH_STONE, 2);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SMOOTH_STONE"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.STONE_SLAB);
		return recipe;
	}
	public static ShapedRecipe SMOOTH_SANDSTONE() {
		ItemStack returnItem = new ItemStack(Material.SMOOTH_SANDSTONE, 2);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SMOOTH_SANDSTONE"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.SANDSTONE_SLAB);
		return recipe;
	}
	public static ShapedRecipe SMOOTH_RED_SANDSTONE() {
		ItemStack returnItem = new ItemStack(Material.SMOOTH_RED_SANDSTONE, 2);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SMOOTH_RED_SANDSTONE"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.RED_SANDSTONE_SLAB);
		return recipe;
	}
	public static ShapedRecipe SMOOTH_QUARTZ() {
		ItemStack returnItem = new ItemStack(Material.SMOOTH_QUARTZ, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SMOOTH_QUARTZ"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.QUARTZ_SLAB);
		return recipe;
	}
	public static ShapelessRecipe SAND() {
		ItemStack returnItem = new ItemStack(Material.SAND, 4);
		ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(mc, "SAND"), returnItem);
		recipe.addIngredient(Material.SANDSTONE);
		return recipe;
	}
	public static ShapelessRecipe SAND_2() {
		ItemStack returnItem = new ItemStack(Material.SAND, 2);
		ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(mc, "SAND_2"), returnItem);
		recipe.addIngredient(Material.SANDSTONE_SLAB);
		return recipe;
	}

//	RecordCrafting
	public static ShapedRecipe MUSIC_DISC_13() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_13, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_13"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.DANDELION_YELLOW);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_CAT() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_CAT, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_CAT"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.CACTUS_GREEN);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_BLOCKS() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_BLOCKS, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_BLOCKS"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.ORANGE_DYE);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_CHIRP() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_CHIRP, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_CHIRP"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.ROSE_RED);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_FAR() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_FAR, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_FAR"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.LIME_DYE);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_MALL() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_MALL, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_MALL"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.PURPLE_DYE);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_MELLOHI() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_MELLOHI, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_MELLOHI"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.MAGENTA_DYE);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_STAL() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_STAL, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_STAL"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.INK_SAC);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_STRAD() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_STRAD, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_STRAD"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.BONE_MEAL);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_WARD() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_WARD, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_WARD"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.ENDER_EYE);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_11() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_11, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_11"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.COAL);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_WAIT() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_WAIT, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_WAIT"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.LIGHT_BLUE_DYE);
		return recipe;
	}

//	HeartCanisters
	public static ShapedRecipe HEART_CANISTER_TIER_1() {
		ItemStack returnItem = CustomItems.HEART_CANISTER_TIER_1(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "HEART_CANISTER_TIER_1"), returnItem);
		recipe.shape("OIO","DSD","ORO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('I', Material.IRON_BLOCK);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('S', Material.WITHER_SKELETON_SKULL);
		recipe.setIngredient('R', Material.BLAZE_ROD);
		return recipe;
	}
	public static ShapedRecipe HEART_CANISTER_TIER_2() {
		ItemStack returnItem = CustomItems.HEART_CANISTER_TIER_2(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "HEART_CANISTER_TIER_2"), returnItem);
		recipe.shape("ECE","DHD","ENE");
		recipe.setIngredient('E', Material.END_STONE);
		recipe.setIngredient('C', Material.END_CRYSTAL);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('H', Material.PLAYER_HEAD);
		recipe.setIngredient('N', Material.NETHER_STAR);
		return recipe;
	}

	private void add(ArrayList<ShapedRecipe> arr, ShapedRecipe... recipes) {
		arr.addAll(new ArrayList<>(Arrays.asList(recipes)));
	}
	private void add(ArrayList<ShapelessRecipe> arr, ShapelessRecipe... recipes) {
		arr.addAll(new ArrayList<>(Arrays.asList(recipes)));
	}

//	//Blast Furnace
//	public static boolean BLAST_FURNACE(Dropper dr) {
//		ItemStack one = dr.getInventory().getItem(0);
//		ItemStack two = dr.getInventory().getItem(1);
//		ItemStack three = dr.getInventory().getItem(2);
//		ItemStack four = dr.getInventory().getItem(3);
//		ItemStack five = dr.getInventory().getItem(4);
//		ItemStack six = dr.getInventory().getItem(5);
//		ItemStack seven = dr.getInventory().getItem(6);
//		ItemStack eight = dr.getInventory().getItem(7);
//		ItemStack nine = dr.getInventory().getItem(8);
//		if(one != null && two != null && three != null && four == null && five != null && six == null && seven != null && eight != null && nine != null) {
//			if(one.equals(new ItemStack(Material.IRON_BARS, 1)) && two.equals(new ItemStack(Material.IRON_BLOCK, 1)) && three.equals(new ItemStack(Material.IRON_BARS, 1)) &&
//				five.equals(new ItemStack(Material.PISTON, 1)) &&
//				seven.equals(new ItemStack(Material.COMPARATOR, 1)) && eight.equals(new ItemStack(Material.REDSTONE_TORCH, 1)) && nine.equals(new ItemStack(Material.COMPARATOR, 1))) {
//				return true;
//			}
//			else {
//				return false;
//			}
//		}
//		else {
//			return false;
//		}
//	}
//
//	public static void playSound(Dropper dr) {
//		for(Player p : Helper.getNearbyPlayers(dr.getLocation(), 7)) {
//			p.playSound(dr.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);
//		}
//	}
}
