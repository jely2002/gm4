package com.belka.spigot.gm4.customTerrain;

import api.Helper;
import api.lootTables.LootTable;
import api.Structure;
import com.belka.spigot.gm4.ConsoleColor;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.*;

public class CustomTerrain implements Listener, Initializable {

	private MainClass mc;
	private LootTable lt;

	private boolean customTerrain;
	private boolean coolerCaves;
	private boolean dangerousDungeons;

	private List<Pair<Integer, Integer>> loadedChunks = new ArrayList<>();

	private int loadRadius = 3;
	List<UpdatableBlock> replacements = new ArrayList<>();
	private boolean isReplacing = false;
	private int replacingSpeed = 24576;

	public CustomTerrain(MainClass mc, LootTable lt) {
		this.mc = mc;
		this.lt = lt;
	}

	public void init(MainClass mc) {
		customTerrain = mc.getConfig().getBoolean("CustomTerrain.enabled");
		coolerCaves = mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled");
		dangerousDungeons = mc.getConfig().getBoolean("CustomTerrain.DangerousDungeons.enabled");

		loadRadius = mc.storage().config().getInt("CustomTerrain.loadRadius");
		replacingSpeed = mc.storage().config().getInt("CustomTerrain.replacingSpeed");

		if (mc.storage().data().getConfigurationSection("CustomTerrain.chunks") != null) {
			for (String chunk : mc.storage().data().getStringList("CustomTerrain.chunks")) {
				loadedChunks.add(new Pair<>(Helper.toInteger(chunk.split(" ")[0]), Helper.toInteger(chunk.split(" ")[1])));
			}
		}
		System.out.println("loadedChunks: " + loadedChunks.size());
	}

	public void disable() {
		List<String> tmp = new ArrayList<>();
		for (Pair<Integer, Integer> pair: loadedChunks) {
			tmp.add(pair.getKey() + " " + pair.getValue());
		}
		mc.storage().data().set("CustomTerrain.chunks", tmp);
		mc.storage().saveData();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!customTerrain) return;
		if (coolerCaves || dangerousDungeons)
			if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)))
				loadChunks(e.getPlayer().getLocation());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!customTerrain) return;
		if (coolerCaves || dangerousDungeons)
			if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)))
				if (!e.getFrom().getChunk().equals(e.getTo().getChunk()))
					loadChunks(e.getTo());
	}
	
	private void loadChunks(Location loc) {
		Chunk c = loc.getChunk();

		for (int x = c.getX() - loadRadius; x <= c.getX() + loadRadius; x++) {
			for (int z = c.getZ() - loadRadius; z <= c.getZ() + loadRadius; z++) {
				Chunk chunk = c.getWorld().getChunkAt(x, z);
				if (!loadedChunks.contains(new Pair<>(chunk.getX(), chunk.getZ()))) {
					loadChunk(chunk);
				}
			}
		}
	}

	private void loadChunk(Chunk c) {
		Block nw = c.getBlock(0,0,0);
		if (nw.getType() == Material.BARRIER) return;

		ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
		//TODO get biomes

		Structure structure = null;
		Location loc = null;
		if (c.getBlock(10,1,10).getType() == Material.DIORITE) {//TODO add config bool
			Bukkit.broadcastMessage("Tower");
			List<Material> triggers = Arrays.asList(Material.AIR, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.DEAD_BUSH, Material.GRASS, Material.FERN, Material.VINE, Material.SNOW,
					Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.OXEYE_DAISY, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP,
					Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.TALL_GRASS, Material.LARGE_FERN,
					Material.ACACIA_LEAVES, Material.BIRCH_LEAVES, Material.JUNGLE_LEAVES, Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.DARK_OAK_LEAVES,
					Material.ACACIA_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG);

			for (int y = cs.getHighestBlockYAt(8, 8) + 1; y >= 60; y--) {//IDK
				if (!triggers.contains(c.getBlock(8, y, 8).getType())) {
					structure = mc.towerStructures().getStructure(c, y);
					loc = c.getBlock(8, y+1, 8).getLocation();//y+1
					Bukkit.broadcastMessage("TS");
					if (structure == null) Bukkit.broadcastMessage("null");
					break;
				}
			}
		}
		else if (dangerousDungeons && c.getBlock(10,1,10).getType() == Material.GRANITE) {
			List<Material> triggers = Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.WATER);
			for (int y = 15; y <= 50; y += 5) {
				if (triggers.contains(c.getBlock(8, y, 8).getType())) {
					structure = mc.dangerousDungeons().getStructure(c);
					loc = c.getBlock(8, y, 8).getLocation();
					Bukkit.broadcastMessage("DD");
					if (structure == null) Bukkit.broadcastMessage("null");
					break;
				}
			}
		}

		if (structure != null) {
			structure.setIgnoredEntityTypes(Collections.singletonList(EntityType.AREA_EFFECT_CLOUD));
			structure.place(loc, true);

			for (Pair<Vector, HashMap<String, Object>> entityPair: structure.getEntities()) {
				HashMap<String, Object> map = entityPair.getValue();
				if (!map.containsKey("id")) continue;
				EntityType entityType = Helper.getEntityByName(map.get("id").toString().replace("minecraft:", ""));
				if (entityType == EntityType.AREA_EFFECT_CLOUD) {
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						if (entry.getKey().equalsIgnoreCase("Tags")) {
							if (entry.getValue() instanceof List) {
								List<String> tags = (List<String>) entry.getValue();
								if (tags.contains("gm4_orbis_populate")) {
									Block entityBlock = loc.clone().add(entityPair.getKey()).getBlock();
									if (tags.contains("gm4_chest")) {
										if (tags.contains("gm4_dungeon")) placeChest(entityBlock, "dungeon");
										else if (tags.contains("gm4_tower")) placeChest(entityBlock, "tower");
									}
									if (tags.contains("gm4_spawner")) {
										if (tags.contains("gm4_default_spawner")) placeSpawner(entityBlock, "default");
										else if (tags.contains("gm4_water_spawner")) placeSpawner(entityBlock, "water");
									}
								}
							}
						}
					}
				}
			}

			for (Player p: Bukkit.getOnlinePlayers()) {
				if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
					p.sendMessage(mc.chatPrefix + ChatColor.GREEN + "Spawned " + structure.getName() + " at " + ChatColor.GOLD + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
				}
			}
			System.out.println(mc.consolePrefix + ConsoleColor.GREEN + "Spawned " + structure.getName() + " at " + ConsoleColor.YELLOW + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + ConsoleColor.RESET);
		}

		if (coolerCaves) mc.coolerCaves().loadChunk(c);

		for (int x = 0 ; x < 16 ; x++ ) {
			for (int z = 0; z < 16; z++) {
				replacements.add(new UpdatableBlock(c.getBlock(x, 0, z).getLocation(), Material.BARRIER, false));
			}
		}
		updateBlocks();
		loadedChunks.add(new Pair<>(c.getX(), c.getZ()));
	}

	private void placeChest(Block b, String type) {
		if (!(b.getState() instanceof Chest)) b.setType(Material.CHEST);
		LootTable lootTable = null;
		try {
			if (type.equalsIgnoreCase("dungeon")) {
				if (b.getX() <= 29) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/dungeon_20.yml"), "dungeon_20");
				}
				else if (b.getX() >= 30 && b.getX() <= 39) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/dungeon_30.yml"), "dungeon_30");
				}
				else if (b.getX() >= 40) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/dungeon_40.yml"), "dungeon_40");
				}
			}
			else if (type.equalsIgnoreCase("tower")) {
				if (b.getX() <= 89) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/tower_70.yml"), "tower_70");
				}
				else if (b.getX() >= 90 && b.getX() <= 109) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/tower_90.yml"), "tower_90");
				}
				else if (b.getX() >= 110 && b.getX() <= 129) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/tower_110.yml"), "tower_110");
				}
				else if (b.getX() >= 130 && b.getX() <= 149) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/tower_130.yml"), "tower_130");
				}
				else if (b.getX() >= 150) {
					lootTable = lt.load(mc.getResourceAsFile("custom_terrain/loot_tables/tower_150.yml"), "tower_150");
				}
			}
			if (lootTable != null) {
				Chest chest = (Chest) b.getState();
				lootTable.place(chest, true);
			}
			else System.out.println("loot null");
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	private void placeSpawner(Block b, String type) {//TODO change to bedrock pattern io random to be seed specific?
		if (b.getType() != Material.SPAWNER) b.setType(Material.SPAWNER, true);
		List<EntityType> defaultEntities = Arrays.asList(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER);
		List<EntityType> waterEntities = Arrays.asList(EntityType.DROWNED, EntityType.GUARDIAN, EntityType.GUARDIAN, EntityType.PUFFERFISH);

		EntityType entityType = defaultEntities.get(new Random().nextInt(defaultEntities.size()));
		if (type.equalsIgnoreCase("water")) entityType = waterEntities.get(new Random().nextInt(waterEntities.size()));
		BlockState blockState = b.getState();
		((CreatureSpawner) blockState).setSpawnedType(entityType);
		blockState.update();
		//TODO add block to config
	}

	private List<Entity> spawned = new ArrayList<>();
	@EventHandler
	public void onEntitySpawn(SpawnerSpawnEvent e) {
		CreatureSpawner spawner = e.getSpawner();
		spawner.getBlock();
		//TODO check block with config
		spawned.add(e.getEntity());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		if (spawned.contains(entity)) {
			spawned.remove(entity);
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
	}

	void updateBlocks() {
		if (isReplacing) return;
		isReplacing = true;
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			List<UpdatableBlock> tmp = new ArrayList<>();
			for (int i = 0; i < replacingSpeed; i++) {// Create temp block cache for replacing
				if (replacements.size() == 0) break;
				tmp.add(replacements.get(0));
				replacements.remove(0);
			}
			for (int i = 0; i < tmp.size(); i++) {// Update blocks in cache
				UpdatableBlock pair = tmp.get(i);
				if (pair.getMaterial() != null) {
					Block b = pair.getLocation().getBlock();
					b.setType(pair.getMaterial(), pair.isUpdate());
					if (pair.getConsumer() != null) pair.getConsumer().accept(b);
				}

				if (tmp.size() < replacingSpeed && i == tmp.size() - 1) {// Done
//					for (Player p: Bukkit.getOnlinePlayers()) {
//						if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
//							p.sendMessage(mc.chatPrefix + ChatColor.GREEN + "Updated queued CustomTerrain blocks!");
//						}
//					}
//					System.out.println(mc.consolePrefix + ConsoleColor.GREEN + "Updated queued CustomTerrain blocks!" + ConsoleColor.RESET);
					Bukkit.getScheduler().cancelTask(task[0]);
					isReplacing = false;
				}
			}
		}, 0L, 20L);
	}
}
