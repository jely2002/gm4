package com.belka.spigot.gm4.modules;

import api.Setting;
import api.lootTables.LootTable;
import api.lootTables.WeightedRandom;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SunkenTreasure implements Module, Listener {

	private MainClass mc;
	private boolean enabled = true;
	private boolean scubaTanks = true;
	private WeightedRandom<String> drops;
	private Map<Integer, Integer> chance = new HashMap<>();
	private LootTable treasureChest;
	private List<String> override;

	private List<List<String>> messages = Arrays.asList(
			Arrays.asList("Dearest Audrey, I mean Hannah...", "wait, no.. is it Denise? ...Anyway,", "I love you."),
			Arrays.asList("Dearest Audrey, when I listen", "to the ocean, all I hear is your",
					"snoring and I can't sleep. Please", "silence your snoring... please, I", "need sleep."),
			Arrays.asList("Dearest Audrey, I write this on a paper", "made from native plants and ink",
					"from a squid I wrangled. I like it here. ", "I found a dungeon. I'm setting up a mob",
					"grinder and never coming home."),
			Arrays.asList("Dearest Audrey, I drank all my beer"),
			Arrays.asList("Dearest Audrey, I know exactly who", "you are - play along. I don't have any money.",
					"I burned it all trying to make a", "fire. But I can tell you I've developed",
					"a very particular set of skills. Skills", "that'll make Bear Grylls quake in his boots."),
			Arrays.asList("Dearest Audrey, I'm in the sink sand.", "If you find my head, hang it on the", "wall of fame."),
			Arrays.asList("Dearest Audrey, My boat was", "sunk by a lily-pad and now I'm", "stuck on this island."),
			Arrays.asList("Dearest Audrey, I don't know how to spell", "your name, nor do I know you, but I",
					"do know that I love you, I feel like I", "have a mutual connection. After", "you ordered the pizza with cheezy",
					"crust and pinapple I just lost it. Oh", "no Clair you got shot... I mean Audrey.", "I don't mind that you're chunky"),
			Arrays.asList("Dearest Audrey, Each day that passes", "is immensely terrible, painful and sad.",
					"Getting away from you just didn't", "improve anything."),
			Arrays.asList("Dearest Audrey, The compass you sent me", "seems to only point in the direction of our love,",
					"but that seems to have vanished long ago.", "Please, get help, even if it's the last thing you do."),
			Arrays.asList("Dearest Audrey, I'm stranded", "in the middle of nowhere,", "surrounded by things that", "aren't cubic."),
			Arrays.asList("Dearest Audrey, I have no", "internet so I am sending you", "a bottle."),
			Arrays.asList("Dearest Audrey, I seem to have", "run out of clean water, if you could",
					"return this bottle with something to", "drink that would be wonderful."),
			Arrays.asList("Dearest Audrey, I find myself stranded", "on a barren island.",
					"An enderdragon took me here,", "please rescue me."),
			Collections.singletonList("Dearest Audrey, Put the money in the bag."),
			Arrays.asList("Dearest Audrey, punching wood", "for logs doesn't work in real life."),
			Arrays.asList("Dearest Audrey, I fear this may", "be my last attempt at finding help.",
					"I just want you to know... I always... loved...", "your sister."),
			Arrays.asList("Dearest Audrey, What's your", "phone number; bottled messages", "are hard to make!"),
			Arrays.asList("Dearest Audrey, How's your", "day going? I'm good, BUT I'VE",
					"BEEN STRANDED FOR 20 YEARS!", "Thanks for your response."),
			Arrays.asList("Dearest Audrey, I've been here for 3 months", "and after several attempts to find food,",
					"shelter, or even enough water... I'm giving up.",
					"Tell my family I love them, but most importantly", "Audrey, I love you.")
	);

	public SunkenTreasure(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public Setting getSetting() { return new Setting("Sunken Treasure", Material.GLASS_BOTTLE); }

	@Override
	public void init(MainClass mc) {
		loadDrops();
//		reload();
	}

	@Override
	public void reload() {
		loadDrops();
	}

	private void loadDrops() {
		enabled = mc.getStorage().config().getBoolean("SunkenTreasure.enabled");
		scubaTanks = mc.getStorage().config().getBoolean("ScubaTanks.enabled");
		drops = new WeightedRandom<>();
		override = new ArrayList<>(mc.getStorage().config().getConfigurationSection("SunkenTreasure.scubaOverride").getKeys(false));
		
		drops.addEntry("bone", get("bone", 15));
		drops.addEntry("seagrass", get("seagrass", 15));
		drops.addEntry("squid_spawn", get("squid_spawn", 10));
		drops.addEntry("message_in_a_bottle", get("message_in_a_bottle", 10));
		drops.addEntry("mossy_cobblestone", get("mossy_cobblestone", 10));
		drops.addEntry("kelp", get("kelp", 7.5));
		drops.addEntry("prismarine_crystals", get("prismarine_crystals", 5));
		drops.addEntry("prismarine_shard", get("prismarine_shard", 5));
		drops.addEntry("sea_pickle", get("sea_pickle", 5));
		//waders
		drops.addEntry("waders", get("waders", 3));
		drops.addEntry("bottle_o_enchanting", get("bottle_o_enchanting", 2.5));
		drops.addEntry("scute", get("scute", 2.5));
		drops.addEntry("gold_ingot", get("gold_ingot", 2.5));
		drops.addEntry("guardian_spawn", get("guardian_spawn", 2.5));
		//leather_boots
		drops.addEntry("leather_boots", get("leather_boots", 2.5));
		drops.addEntry("wet_sponge", get("wet_sponge", 1.5));
		drops.addEntry("treasure_chest", get("treasure_chest", .5));

		drops.addEntry("scuba_flippers", get("scuba_flippers", 0));
		drops.addEntry("scuba_tank", get("scuba_tank", 0));

		chance.put(0, mc.getStorage().config().getInt("SunkenTreasure.treasure.0", 0));
		chance.put(1, mc.getStorage().config().getInt("SunkenTreasure.treasure.1", 11));
		chance.put(2, mc.getStorage().config().getInt("SunkenTreasure.treasure.2", 27));
		chance.put(3, mc.getStorage().config().getInt("SunkenTreasure.treasure.3", 49));

		File file = new File(mc.getDataFolder() + "/sunken_treasure", "treasure_chest.yml");
		if (file.exists()) {
			treasureChest = LootTable.load(file, "treasure_chest");
		}
		else {
			try {
				LootTable.saveJsonAsYaml(mc.getResourceAsFile("sunken_treasure/treasure_chest.json"), mc.getDataFolder() + "/sunken_treasure", "treasure_chest");
				treasureChest = LootTable.load(mc.getDataFolder() + "/sunken_treasure", "treasure_chest");
			} catch (IOException err) {
				Bukkit.getLogger().log(Level.WARNING, "SunkenTreasure Error: " + err.getMessage());
			}
		}
	}
	private int get(String item, double defaultVal) {
		if (scubaTanks && override.contains(item)) {
			return (int) Math.round(mc.getStorage().config().getDouble("SunkenTreasure.scubaOverride." + item, defaultVal) * 10);
		}
		return (int) Math.round(mc.getStorage().config().getDouble("SunkenTreasure.treasure." + item, defaultVal) * 10);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!enabled) return;
		if (e.getBlock().getType() != Material.SAND) return;
		Player p = e.getPlayer();
		if (p.getGameMode() != GameMode.SURVIVAL) return;
		ItemStack main = p.getInventory().getItemInMainHand();
		if (main.getType() == Material.WOODEN_SHOVEL || main.getType() == Material.STONE_SHOVEL || main.getType() == Material.GOLDEN_SHOVEL || main.getType() == Material.IRON_SHOVEL || main.getType() == Material.DIAMOND_SHOVEL) {
			if (isUnderwater(p)) {
				ItemMeta meta = main.getItemMeta();
				int level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
				if (level > 3) level = 3;
				double d = Math.random();
				if (d * 100 < chance.get(level)) {
					String drop = drops.getRandom();
					Location loc = e.getBlock().getLocation().clone().add(.5, .5, .5);
					World world = e.getBlock().getLocation().getWorld();
					if (world != null)
						switch (drop) {
							case "bone":
								world.dropItemNaturally(loc, new ItemStack(Material.BONE));
								break;
							case "seagrass":
								world.dropItemNaturally(loc, new ItemStack(Material.SEAGRASS));
								break;
							case "squid_spawn":
								world.spawnEntity(loc, EntityType.SQUID);
								break;
							case "message_in_a_bottle":
								ItemStack bottle = new ItemStack(Material.GLASS_BOTTLE);
								ItemMeta bottleMeta = bottle.getItemMeta();
								bottleMeta.setDisplayName(ChatColor.WHITE + "Message in a Bottle");
								int index = new Random().nextInt(21);
								List<String> lore = messages.get(index).stream().map(o -> ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + o).collect(Collectors.toList());
								bottleMeta.setLore(lore);
								bottleMeta.setCustomModelData(index);
								bottle.setItemMeta(bottleMeta);
								world.dropItemNaturally(loc, bottle);
								break;
							case "mossy_cobblestone":
								world.dropItemNaturally(loc, new ItemStack(Material.MOSSY_COBBLESTONE));
								break;
							case "kelp":
								world.dropItemNaturally(loc, new ItemStack(Material.KELP));
								break;
							case "prismarine_crystals":
								world.dropItemNaturally(loc, new ItemStack(Material.PRISMARINE_CRYSTALS));
								break;
							case "prismarine_shard":
								world.dropItemNaturally(loc, new ItemStack(Material.PRISMARINE_SHARD));
								break;
							case "sea_pickle":
								world.dropItemNaturally(loc, new ItemStack(Material.SEA_PICKLE));
								break;
							case "waders":
								ItemStack waders = new ItemStack(Material.GOLDEN_BOOTS);
								waders.addEnchantment(Enchantment.DEPTH_STRIDER, 1);
								ItemMeta wadersMeta = waders.getItemMeta();
								wadersMeta.setDisplayName(ChatColor.AQUA + "Waders");
								waders.setItemMeta(wadersMeta);
								world.dropItemNaturally(loc, waders);
								break;
							case "bottle_o_enchanting":
								world.dropItemNaturally(loc, new ItemStack(Material.EXPERIENCE_BOTTLE));
								break;
							case "scute":
								world.dropItemNaturally(loc, new ItemStack(Material.SCUTE));
								break;
							case "gold_ingot":
								world.dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT));
								break;
							case "guardian_spawn":
								world.spawnEntity(loc, EntityType.GUARDIAN);
								break;
							case "leather_boots":
								world.dropItemNaturally(loc, new ItemStack(Material.LEATHER_BOOTS));
								break;
							case "wet_sponge":
								world.dropItemNaturally(loc, new ItemStack(Material.WET_SPONGE));
								break;
							case "treasure_chest":
								ItemStack chest = new ItemStack(Material.CHEST);
								if (treasureChest != null) {
									chest = treasureChest.asItem();
								}
								ItemMeta chestMeta = chest.getItemMeta();
								chestMeta.setDisplayName(ChatColor.WHITE + "Treasure Chest");
								chest.setItemMeta(chestMeta);
								world.dropItemNaturally(loc, chest);
								break;
							case "scuba_flippers":
								world.dropItemNaturally(loc, new ItemStack(Material.LEATHER_BOOTS));
								//TODO
								break;
							case "scuba_tank":
								world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_CHESTPLATE));
								//TODO
								break;
						}
				}
			}
		}
	}

	@EventHandler
	public void onPickUp(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getItem().getItemStack().getType() == Material.GLASS_BOTTLE) {
				ItemStack item = e.getItem().getItemStack();
				ItemMeta meta = item.getItemMeta();
				if (ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Message in a Bottle") && meta.hasCustomModelData()) {
					List<Integer> collected = new ArrayList<>();
					if (mc.getStorage().data().contains("AllMyAudreys." + p.getUniqueId().toString()))
						collected = mc.getStorage().data().getIntegerList("AllMyAudreys." + p.getUniqueId().toString());
					if (!collected.contains(meta.getCustomModelData())) {
						collected.add(meta.getCustomModelData());
						Advancements.grantCriteria("all_my_audreys", 1, p);
					}
					Collections.sort(collected);
					mc.getStorage().data().set("AllMyAudreys." + p.getUniqueId().toString(), collected);
					mc.getStorage().saveData();
//					if (collected.size() == 20) {
//						Advancements.grantAdvancement("all_my_audreys", p);
//					}
				}
			}
		}
	}

	private boolean isUnderwater(Player p) {
		Location loc = p.isSwimming() ? p.getEyeLocation() : p.getLocation();
		return loc.getBlock().getType() == Material.WATER &&
				loc.getBlock().getRelative(BlockFace.UP, 1).getType() == Material.WATER &&
				loc.getBlock().getRelative(BlockFace.UP, 2).getType() == Material.WATER &&
				loc.getBlock().getRelative(BlockFace.UP, 3).getType() == Material.WATER;
	}
}
