package api;

import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.crafting.CustomRecipes;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public enum CustomBlockType {// implements Consumer<Block>
	CUSTOM_CRAFTER("CustomCrafter", "Custom Crafter", Material.DROPPER, new Vector(0.5, 0.075, 0.5), new ItemStack(Material.CRAFTING_TABLE)),
	MASTER_CRAFTER("MasterCrafter", "Master Crafter", Material.DROPPER, new Vector(0.5, 0.67, 0.5), new ItemStack(Material.PISTON), new EulerAngle(Helper.degToRad(180f), 0f, 0f)),
	BLAST_FURNACE("BlastFurnace", "Blast Furnace Output", Material.HOPPER, new Vector(0.5, 0.075, 0.5)),
	DISASSEMBLER("Disassembler", "Disassembler", Material.DROPPER, new Vector(0.5, 0.075, 0.5), new ItemStack(Material.TNT), (Block b) -> {
		b.getWorld().spawnParticle(Particle.LAVA, b.getLocation().add(0.5, 0.75, 0.5), 10);
		b.getWorld().playSound(b.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
	}),
	ALCHEMICAL_CRAFTER("AlchemicalCrafter", "Alchemical Crafter", Material.DROPPER, new Vector(0.5, 0.075, 0.5), CustomItems.PHILOSOPHERS_STONE(1), (Block b) -> {
		b.getWorld().strikeLightningEffect(b.getLocation().add(0.5, 1, 0.5));
		b.getWorld().playSound(b.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
	}),
	ZAUBER_CAULDRON("ZauberCauldron", "Zauber Cauldron", Material.CAULDRON, new Vector(0.5, 0.075, 0.5)),
	LIQUID_TANK("LiquidTank", "Liquid Tank", Material.HOPPER, new Vector(0.5, 0.075, 0.5)),
	ENDER_HOPPER("EnderHopper", "Ender Hopper", Material.HOPPER, new Vector(0.5, 0, 0.5), CustomItems.ENDER_HOPPER_SKULL(1), (Block b) -> {
		b.getWorld().spawnParticle(Particle.SMOKE_LARGE, b.getLocation().add(0.5, 0.75, 0.5), 10);
		b.getWorld().playSound(b.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1, 1);
	}),
	SOUL_GLASS("SoulGlass", "Soul Glass", Material.BROWN_STAINED_GLASS);

	private String id;
	private String name;
	private Material type;
	private Vector offset;
	private ItemStack helmet;
	private EulerAngle headPose;
	private Consumer<Block> consumer;

	CustomBlockType(@NotNull String id, @NotNull String name, @NotNull Material type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	CustomBlockType(@NotNull String id, @NotNull String name, @NotNull Material type, @Nullable Vector offset) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.offset = offset;
	}
	CustomBlockType(@NotNull String id, @NotNull String name, @NotNull Material type, @Nullable Vector offset, @Nullable ItemStack helmet) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.offset = offset;
		this.helmet = helmet;
	}
	CustomBlockType(@NotNull String id, @NotNull String name, @NotNull Material type, @Nullable Vector offset, @Nullable ItemStack helmet, @Nullable EulerAngle headPose) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.offset = offset;
		this.helmet = helmet;
		this.headPose = headPose;
	}
	CustomBlockType(@NotNull String id, @NotNull String name, @NotNull Material type, @Nullable Vector offset, @Nullable ItemStack helmet, @Nullable Consumer<Block> consumer) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.offset = offset;
		this.helmet = helmet;
		this.consumer = consumer;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Material getType() {
		return type;
	}

	public boolean hasArmorStand() {
		return offset != null;
	}
	public Vector getOffset() {
		return offset;
	}

	public boolean hasHelmet() {
		return helmet != null;
	}
	public ItemStack getHelmet() {
		return helmet;
	}

	public boolean hasHeadPose() {
		return headPose != null;
	}
	public EulerAngle getHeadPose() {
		return headPose;
	}

	public boolean hasConsumer() {
		return consumer != null;
	}
	public Consumer<Block> getConsumer() {
		return consumer;
	}

	public List<ItemStack> getDrops() {
		List<ItemStack> items = recipeToList(CustomRecipes.create());
		items.add(new ItemStack(Material.CRAFTING_TABLE));
		switch (this) {
			case CUSTOM_CRAFTER:
				return items;
			case MASTER_CRAFTER:
				items.addAll(recipeToList(CustomRecipes.master_crafter()));
				return items;
			case BLAST_FURNACE:
				items.addAll(recipeToList(CustomRecipes.blast_furnace()));
				return items;
			case DISASSEMBLER:
				items.addAll(recipeToList(CustomRecipes.disassembler()));
				return items;
			case ALCHEMICAL_CRAFTER:
				items.addAll(Arrays.asList(new ItemStack(Material.CRAFTING_TABLE), CustomItems.MINIUM_DUST(8)));
				return items;
			case ENDER_HOPPER:
				return new ArrayList<>(Arrays.asList(new ItemStack(Material.ENDER_PEARL, 4), new ItemStack(Material.IRON_BLOCK, 4), new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.ENDER_EYE)));
			case SOUL_GLASS:
				return Collections.singletonList(CustomItems.SOUL_GLASS(1));
			default: return new ArrayList<>();
		}
	}
	private List<ItemStack> recipeToList(ShapedRecipe recipe) {
		Map<Character, ItemStack> map = new HashMap<>();
		for (String chars : recipe.getShape()) {
			for (String character : chars.split("(?!^)")) {
				char c = character.charAt(0);
				if (c != ' ') {
					if (map.containsKey(c)) {
						ItemStack item = map.get(c);
						item.setAmount(item.getAmount() + 1);
						map.put(c, item);
					}
					else {
						map.put(c, recipe.getIngredientMap().get(c));
					}
				}
			}
		}
		return new ArrayList<>(map.values());
	}

	public static CustomBlockType getById(String id) {
		for (CustomBlockType cbt: CustomBlockType.values()) {
			if (cbt.getId().equalsIgnoreCase(id)) return cbt;
		}
		return null;
	}

	public static boolean isType(String id) {
		for (CustomBlockType cbt: CustomBlockType.values()) {
			if (cbt.getId().equalsIgnoreCase(id)) return true;
		}
		return false;
	}

//	@Override
//	public void accept(Block o) {
//		consumer.accept(o);
//	}
}
