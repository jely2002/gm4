package com.belka.spigot.gm4.crafting;

import api.Recipe;
import api.ShapedRecipe;
import api.ShapelessRecipe;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomRecipes implements Module {

	public static MainClass mc;
	public static List<Recipe> allRecipes = new ArrayList<>();
	public static List<Recipe> ccRecipes = new ArrayList<>();
	public static List<Recipe> mcRecipes = new ArrayList<>();
	public static List<Recipe> bfRecipes = new ArrayList<>();
	public static List<Recipe> daRecipes = new ArrayList<>();
	public static List<Recipe> acRecipes = new ArrayList<>();

	public static List<Recipe> standardCrafting = new ArrayList<>();

	public static List<Recipe> recordCrafting = new ArrayList<>();

	public static List<Recipe> heartCanisters = new ArrayList<>();
	public static List<Recipe> trappedSigns = new ArrayList<>();
	public static List<Recipe> lightningRods = new ArrayList<>();
	public static List<Recipe> soulProbes = new ArrayList<>();
	public static List<Recipe> masterCrafting = new ArrayList<>();
	public static List<Recipe> blastFurnace = new ArrayList<>();
	public static List<Recipe> disassembler = new ArrayList<>();
	public static List<Recipe> equivalentExchange = new ArrayList<>();

	public CustomRecipes(MainClass mc) {
		CustomRecipes.mc = mc;
	}

	@Override
	public void init(MainClass mc) {
//		Custom Crafter
		if (mc.getStorage().config().getBoolean("CustomCrafter.StandardCrafting")) {
			add(standardCrafting, GRAVEL(), RED_SAND(), COBWEB(), IRON_HORSE_ARMOR(), GOLDEN_HORSE_ARMOR(), DIAMOND_HORSE_ARMOR(),
					OAK_PLANKS(), SPRUCE_PLANKS(), BIRCH_PLANKS(), JUNGLE_PLANKS(), ACACIA_PLANKS(), DARK_OAK_PLANKS(),
					ENCHANTED_GOLDEN_APPLE(), SMOOTH_STONE(), SMOOTH_SANDSTONE(), SMOOTH_RED_SANDSTONE(), SMOOTH_QUARTZ(),
					SAND(), SAND_2());
			ccRecipes.addAll(standardCrafting);
		}
		if (mc.getStorage().config().getBoolean("CustomCrafter.RecordCrafting")) {
			add(recordCrafting, MUSIC_DISC_11(), MUSIC_DISC_13(), MUSIC_DISC_BLOCKS(), MUSIC_DISC_CAT(), MUSIC_DISC_CHIRP(), MUSIC_DISC_FAR(),
					MUSIC_DISC_MALL(), MUSIC_DISC_MELLOHI(), MUSIC_DISC_STAL(), MUSIC_DISC_STRAD(), MUSIC_DISC_WAIT(), MUSIC_DISC_WARD());
			ccRecipes.addAll(recordCrafting);
		}

		if (mc.getStorage().config().getBoolean("HeartCanisters.enabled")) {
			add(heartCanisters, HEART_CANISTER_TIER_1(), HEART_CANISTER_TIER_2());
			ccRecipes.addAll(heartCanisters);
		}

		if (mc.getStorage().config().getBoolean("TrappedSigns.enabled")) {
			add(trappedSigns, TRAPPED_SIGN_OAK(), TRAPPED_SIGN_SPRUCE(), TRAPPED_SIGN_BIRCH(), TRAPPED_SIGN_JUNGLE(), TRAPPED_SIGN_ACACIA(), TRAPPED_SIGN_DARK_OAK());
			ccRecipes.addAll(trappedSigns);
		}

		if (mc.getStorage().config().getBoolean("LightningRods.enabled")) {
			add(lightningRods, LIGHTNING_ROD());
			ccRecipes.addAll(lightningRods);
		}

		if (mc.getStorage().config().getBoolean("SoulProbes.enabled")) {
			add(soulProbes, SOUL_PROBES_BOOK(), EMPTY_SPAWN_EGG());
			ccRecipes.addAll(soulProbes);
		}

//		Other Crafters
		if (mc.getStorage().config().getBoolean("CustomCrafter.MasterCrafting")) {
			add(masterCrafting, STONE_BRICKS(), COAL_BLOCK_OAK(), COAL_BLOCK_SPRUCE(), COAL_BLOCK_BIRCH(), COAL_BLOCK_JUNGLE(), COAL_BLOCK_ACACIA(), COAL_BLOCK_DARK_OAK(), BRICKS(), LAVA_BUCKET());
			mcRecipes.addAll(masterCrafting);
			add(masterCrafting, master_crafter());
			ccRecipes.add(master_crafter());
		}
		if (mc.getStorage().config().getBoolean("CustomCrafter.BlastFurnace")) {
			add(blastFurnace, blast_furnace());
			ccRecipes.addAll(blastFurnace);
			add(bfRecipes);
		}
		if (mc.getStorage().config().getBoolean("CustomCrafter.Disassembler")) {
			add(disassembler, disassembler());
			ccRecipes.addAll(disassembler);
			add(daRecipes);
		}
		if (mc.getStorage().config().getBoolean("CustomCrafter.EquivalentExchange")) {
			add(equivalentExchange, PHILOSOPHERS_STONE_MKII(), PHILOSOPHERS_STONE_MKIII(), PHILOSOPHERS_STONE_MKIV(), AC_ERROR(), AC_INFINITY_TOOL(),
					EE_TO_COAL(), EE_TO_IRON(), EE_TO_GOLD(), EE_TO_DIAMOND(), EE_TO_EMERALD(),
					EE_FROM_EMERALD(), EE_FROM_DIAMOND(), EE_FROM_GOLD(), EE_FROM_IRON(), EE_FROM_COAL());
			acRecipes.addAll(equivalentExchange);
			add(equivalentExchange, alchemical_crafter(), MINIUM_DUST(), INERT_STONE(), PHILOSOPHERS_STONE());
			ccRecipes.addAll(Arrays.asList(alchemical_crafter(), MINIUM_DUST(), INERT_STONE(), PHILOSOPHERS_STONE()));
		}
	}

//	CREATION
	public static ShapedRecipe create() {
		ItemStack returnItem = new ItemStack(Material.STRUCTURE_VOID, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "create"), returnItem);
		recipe.shape("CCC","C C","CRC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}
	//	Mastercraftsman's Workbench
	public static ShapedRecipe master_crafter() {
		ItemStack returnItem = new ItemStack(Material.STRUCTURE_VOID, 1);
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
		ItemStack returnItem = new ItemStack(Material.STRUCTURE_VOID, 1);
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
		ItemStack returnItem = new ItemStack(Material.STRUCTURE_VOID, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "disassembler"), returnItem);
		recipe.shape("CCC","CTC","CRC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('T', Material.TNT);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}
	//	Alchemical Crafter
	public static ShapedRecipe alchemical_crafter() {
		ItemStack returnItem = new ItemStack(Material.STRUCTURE_VOID, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "alchemical_crafter"), returnItem);
		recipe.shape("RRR","RCR","RRR");
		recipe.setIngredient('R', CustomItems.MINIUM_DUST(1));
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		return recipe;
	}

	//<editor-fold desc="Standard Crafting">
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
		recipe.setIngredient('R', Material.RED_DYE);
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
		ItemStack returnItem = new ItemStack(Material.OAK_PLANKS, 3);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "OAK_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.OAK_STAIRS);
		return recipe;
	}
	public static ShapedRecipe SPRUCE_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.SPRUCE_PLANKS, 3);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SPRUCE_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.SPRUCE_STAIRS);
		return recipe;
	}
	public static ShapedRecipe BIRCH_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.BIRCH_PLANKS, 3);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "BIRCH_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.BIRCH_STAIRS);
		return recipe;
	}
	public static ShapedRecipe JUNGLE_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.JUNGLE_PLANKS, 3);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "JUNGLE_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.JUNGLE_STAIRS);
		return recipe;
	}
	public static ShapedRecipe ACACIA_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.ACACIA_PLANKS, 3);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "ACACIA_PLANKS"), returnItem);
		recipe.shape("SS ","SS ","   ");
		recipe.setIngredient('S', Material.ACACIA_STAIRS);
		return recipe;
	}
	public static ShapedRecipe DARK_OAK_PLANKS() {
		ItemStack returnItem = new ItemStack(Material.DARK_OAK_PLANKS, 3);
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
	//</editor-fold>

	//<editor-fold desc="Record Crafting">
	public static ShapedRecipe MUSIC_DISC_13() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_13, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_13"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.YELLOW_DYE);
		return recipe;
	}
	public static ShapedRecipe MUSIC_DISC_CAT() {
		ItemStack returnItem = new ItemStack(Material.MUSIC_DISC_CAT, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MUSIC_DISC_CAT"), returnItem);
		recipe.shape("FFF","FDF","FFF");
		recipe.setIngredient('F', Material.FLINT);
		recipe.setIngredient('D', Material.GREEN_DYE);
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
		recipe.setIngredient('D', Material.RED_DYE);
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
	//</editor-fold>

	//<editor-fold desc="Master Crafter">
	public static ShapedRecipe STONE_BRICKS() {
		ItemStack returnItem = new ItemStack(Material.STONE_BRICKS, 16);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "STONE_BRICKS"), returnItem);
		recipe.shape("SSS","SCS","SSS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('C', Material.CLAY_BALL);
		return recipe;
	}
	public static ShapedRecipe COAL_BLOCK_OAK() {
		ItemStack returnItem = new ItemStack(Material.COAL_BLOCK, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COAL_BLOCK_OAK"), returnItem);
		recipe.shape("LLL","LLL","LLL");
		recipe.setIngredient('L', Material.OAK_LOG);
		return recipe;
	}
	public static ShapedRecipe COAL_BLOCK_SPRUCE() {
		ItemStack returnItem = new ItemStack(Material.COAL_BLOCK, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COAL_BLOCK_SPRUCE"), returnItem);
		recipe.shape("LLL","LLL","LLL");
		recipe.setIngredient('L', Material.SPRUCE_LOG);
		return recipe;
	}
	public static ShapedRecipe COAL_BLOCK_BIRCH() {
		ItemStack returnItem = new ItemStack(Material.COAL_BLOCK, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COAL_BLOCK_BIRCH"), returnItem);
		recipe.shape("LLL","LLL","LLL");
		recipe.setIngredient('L', Material.BIRCH_LOG);
		return recipe;
	}
	public static ShapedRecipe COAL_BLOCK_JUNGLE() {
		ItemStack returnItem = new ItemStack(Material.COAL_BLOCK, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COAL_BLOCK_JUNGLE"), returnItem);
		recipe.shape("LLL","LLL","LLL");
		recipe.setIngredient('L', Material.JUNGLE_LOG);
		return recipe;
	}
	public static ShapedRecipe COAL_BLOCK_ACACIA() {
		ItemStack returnItem = new ItemStack(Material.COAL_BLOCK, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COAL_BLOCK_ACACIA"), returnItem);
		recipe.shape("LLL","LLL","LLL");
		recipe.setIngredient('L', Material.ACACIA_LOG);
		return recipe;
	}
	public static ShapedRecipe COAL_BLOCK_DARK_OAK() {
		ItemStack returnItem = new ItemStack(Material.COAL_BLOCK, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "COAL_BLOCK_DARK_OAK"), returnItem);
		recipe.shape("LLL","LLL","LLL");
		recipe.setIngredient('L', Material.DARK_OAK_LOG);
		return recipe;
	}
	public static ShapedRecipe BRICKS() {
		ItemStack returnItem = new ItemStack(Material.BRICKS, 12);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "BRICKS"), returnItem);
		recipe.shape("CCC","CLC","CCC");
		recipe.setIngredient('C', Material.CLAY);
		recipe.setIngredient('L', Material.LADDER);
		return recipe;
	}
	public static ShapedRecipe LAVA_BUCKET() {
		ItemStack returnItem = new ItemStack(Material.LAVA_BUCKET, 1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "LAVA_BUCKET"), returnItem);
		recipe.shape("NNN","NBN","NNN");
		recipe.setIngredient('N', Material.NETHERRACK);
		recipe.setIngredient('B', Material.BUCKET);
		return recipe;
	}
	//</editor-fold>

	//<editor-fold desc="Equivalent Exchange (Custom Crafter)">
	public static ShapedRecipe MINIUM_DUST() {
		ItemStack returnItem = CustomItems.MINIUM_DUST(4);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "MINIUM_DUST"), returnItem);
		recipe.shape("RRR","RDR","RRR");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('D', Material.DIAMOND);
		return recipe;
	}
	public static ShapedRecipe INERT_STONE() {
		ItemStack returnItem = CustomItems.INERT_STONE(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "INERT_STONE"), returnItem);
		recipe.shape("GSG","SDS","GSG");
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('D', Material.DIAMOND);
		return recipe;
	}
	public static ShapedRecipe PHILOSOPHERS_STONE() {
		ItemStack returnItem = CustomItems.PHILOSOPHERS_STONE(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "PHILOSOPHERS_STONE"), returnItem);
		recipe.shape("RRR","RFR","RRR");
		recipe.setIngredient('R', CustomItems.MINIUM_DUST(1));
		recipe.setIngredient('F', CustomItems.INERT_STONE(1));
		return recipe;
	}
//	Equivalent Exchange (Alchemical Crafter)
	public static ShapedRecipe PHILOSOPHERS_STONE_MKII() {
		ItemStack returnItem = CustomItems.PHILOSOPHERS_STONE_MKII(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "PHILOSOPHERS_STONE_MKII"), returnItem);
		recipe.shape("R R"," I ","R R");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('R', CustomItems.PHILOSOPHERS_STONE(1));
		return recipe;
	}
	public static ShapedRecipe PHILOSOPHERS_STONE_MKIII() {
		ItemStack returnItem = CustomItems.PHILOSOPHERS_STONE_MKIII(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "PHILOSOPHERS_STONE_MKIII"), returnItem);
		recipe.shape("R R"," G ","R R");
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('R', CustomItems.PHILOSOPHERS_STONE_MKII(1));
		return recipe;
	}
	public static ShapedRecipe PHILOSOPHERS_STONE_MKIV() {
		ItemStack returnItem = CustomItems.PHILOSOPHERS_STONE_MKIV(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "PHILOSOPHERS_STONE_MKIV"), returnItem);
		recipe.shape("R R"," D ","R R");
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('R', CustomItems.PHILOSOPHERS_STONE_MKIII(1));
		return recipe;
	}
	public static ShapedRecipe AC_ERROR() {
		ItemStack returnItem = CustomItems.AC_ERROR(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "AC_ERROR"), returnItem);
		recipe.shape("R R"," O ","R R");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('R', CustomItems.PHILOSOPHERS_STONE_MKIV(1));
		return recipe;
	}
	public static ShapedRecipe AC_INFINITY_TOOL() {
		ItemStack returnItem = CustomItems.AC_INFINITY_TOOL(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "AC_INFINITY_TOOL"), returnItem);
		recipe.shape("EEE"," D "," D ");
		recipe.setIngredient('E', CustomItems.AC_ERROR(1));
		recipe.setIngredient('D', Material.DIAMOND_BLOCK);
		return recipe;
	}
	//</editor-fold>
	//<editor-fold desc="Equivalent Exchange (Alchemical Crafter)">
	public static ShapedRecipe EE_TO_COAL() {
		ItemStack returnItem = new ItemStack(Material.COAL);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_TO_COAL"), returnItem);
		recipe.shape("PR ","R  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}
	public static ShapedRecipe EE_TO_IRON() {
		ItemStack returnItem = new ItemStack(Material.IRON_INGOT);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_TO_IRON"), returnItem);
		recipe.shape("PC ","CC ","C  ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('C', Material.COAL);
		return recipe;
	}
	public static ShapedRecipe EE_TO_GOLD() {
		ItemStack returnItem = new ItemStack(Material.GOLD_INGOT);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_TO_GOLD"), returnItem);
		recipe.shape("PI ","II ","I  ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('I', Material.IRON_INGOT);
		return recipe;
	}
	public static ShapedRecipe EE_TO_DIAMOND() {
		ItemStack returnItem = new ItemStack(Material.DIAMOND);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_TO_DIAMOND"), returnItem);
		recipe.shape("PG ","GG ","G  ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('G', Material.GOLD_INGOT);
		return recipe;
	}
	public static ShapedRecipe EE_TO_EMERALD() {
		ItemStack returnItem = new ItemStack(Material.EMERALD);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_TO_EMERALD"), returnItem);
		recipe.shape("PD ","D  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('D', Material.DIAMOND);
		return recipe;
	}
	public static ShapedRecipe EE_FROM_EMERALD() {
		ItemStack returnItem = new ItemStack(Material.DIAMOND, 2);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_FROM_EMERALD"), returnItem);
		recipe.shape("P  ","E  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('E', Material.EMERALD);
		return recipe;
	}
	public static ShapedRecipe EE_FROM_DIAMOND() {
		ItemStack returnItem = new ItemStack(Material.GOLD_INGOT, 4);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_FROM_DIAMOND"), returnItem);
		recipe.shape("P  ","E  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('E', Material.DIAMOND);
		return recipe;
	}
	public static ShapedRecipe EE_FROM_GOLD() {
		ItemStack returnItem = new ItemStack(Material.IRON_INGOT, 4);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_FROM_GOLD"), returnItem);
		recipe.shape("P  ","E  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('E', Material.GOLD_INGOT);
		return recipe;
	}
	public static ShapedRecipe EE_FROM_IRON() {
		ItemStack returnItem = new ItemStack(Material.COAL, 4);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_FROM_IRON"), returnItem);
		recipe.shape("P  ","E  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('E', Material.IRON_INGOT);
		return recipe;
	}
	public static ShapedRecipe EE_FROM_COAL() {
		ItemStack returnItem = new ItemStack(Material.REDSTONE, 2);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EE_FROM_COAL"), returnItem);
		recipe.shape("P  ","E  ","   ");
		recipe.setIngredient('P', CustomItems.PHILOSOPHERS_STONE(1));
		recipe.setIngredient('E', Material.COAL);
		return recipe;
	}
	//</editor-fold>

//	OTHER MODULES
	//<editor-fold desc="Heart Canisters">
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
		recipe.setIngredient('H', CustomItems.HEART_CANISTER_TIER_1(1));
		recipe.setIngredient('N', Material.NETHER_STAR);
		return recipe;
	}
	//</editor-fold>

//	Trapped Signs
	private static ShapedRecipe TRAPPED_SIGN() {
		ItemStack returnItem = CustomItems.TRAPPED_SIGN(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "TRAPPED_SIGN"), returnItem);
		recipe.shape("   ","TST","RRR");
		recipe.setIngredient('T', Material.TRIPWIRE_HOOK);
		recipe.setIngredient('S', Material.OAK_SIGN);
		recipe.setIngredient('R', Material.REDSTONE);
		return recipe;
	}

//	Lightning Rods
	public static ShapedRecipe LIGHTNING_ROD() {
		ItemStack returnItem = CustomItems.LIGHTNING_ROD(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "LIGHTNING_ROD"), returnItem);
		recipe.shape("BBB","BSB","BBB");
		recipe.setIngredient('B', Material.BLAZE_POWDER);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}

//	Soul Probes
	public static ShapedRecipe SOUL_PROBES_BOOK() {
		ItemStack returnItem = CustomItems.SOUL_PROBES_BOOK(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "SOUL_PROBES_BOOK"), returnItem);
		recipe.shape(" E "," B "," N ");
		recipe.setIngredient('E', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('N', Material.NETHER_STAR);
		return recipe;
	}
	public static ShapedRecipe EMPTY_SPAWN_EGG() {
		ItemStack returnItem = CustomItems.EMPTY_SPAWN_EGG(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(mc, "EMPTY_SPAWN_EGG"), returnItem);
		recipe.shape("BIB","LEL","BDB");
		recipe.setIngredient('B', Material.BONE_MEAL);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('L', Material.LIGHT_GRAY_DYE);
		recipe.setIngredient('E', Material.EGG);
		recipe.setIngredient('D', Material.DIAMOND);
		return recipe;
	}

	public void add(List<Recipe> arr, Recipe... recipes) {
		allRecipes.addAll(new ArrayList<>(Arrays.asList(recipes)));
		arr.addAll(new ArrayList<>(Arrays.asList(recipes)));
	}
}
