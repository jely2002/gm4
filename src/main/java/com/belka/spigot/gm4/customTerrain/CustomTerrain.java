package com.belka.spigot.gm4.customTerrain;

import api.Helper;
import api.Structure;
import com.belka.spigot.gm4.ConsoleColor;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class CustomTerrain implements Listener, Initializable {

	private MainClass mc;

	private boolean customTerrain;
	private boolean coolerCaves;
	private boolean dangerousDungeons;

	private List<Pair<Integer, Integer>> loadedChunks = new ArrayList<>();

	private int loadRadius = 3;
	List<UpdatableBlock> replacements = new ArrayList<>();
	private boolean isReplacing = false;
	private int replacingSpeed = 24576;

	public CustomTerrain(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		customTerrain = mc.getConfig().getBoolean("CustomTerrain.enabled");
		coolerCaves = mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled");
		dangerousDungeons = mc.getConfig().getBoolean("CustomTerrain.DangerousDungeons.enabled");

		loadRadius = mc.storage().config().getInt("CustomTerrain.loadRadius");
		replacingSpeed = mc.storage().config().getInt("CustomTerrain.replacingSpeed");

		if (mc.storage().data().getConfigurationSection("CustomTerrain.chunks") != null)
			for (String chunk: mc.storage().data().getStringList("CustomTerrain.chunks")) {
				loadedChunks.add(new Pair<>(Helper.toInteger(chunk.split(" ")[0]), Helper.toInteger(chunk.split(" ")[1])));
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

					loadedChunks.add(new Pair<>(chunk.getX(), chunk.getZ()));
				}
			}
		}
	}
	private void loadChunk(Chunk c) {
		Block nw = c.getBlock(0,0,0);
		if (nw.getType() == Material.BARRIER) return;

//		ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
		//TODO get biomes

		Structure structure = null;
		Location loc = null;
		if (c.getBlock(10,1,10).getType() == Material.DIORITE) {
			List<Material> triggers = Arrays.asList(Material.AIR);//add remaining
			Bukkit.broadcastMessage("Diorite");
			//TODO Tower Structures
		}
		else if (dangerousDungeons && c.getBlock(10,1,10).getType() == Material.GRANITE) {
			List<Material> triggers = Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.WATER);
			for (int y = 15; y <= 60; y += 5) {
				if (triggers.contains(c.getBlock(8, y, 8).getType())) {
					structure = mc.dangerousDungeons().getStructure(c);
					loc = c.getBlock(8, y, 8).getLocation();
					Bukkit.broadcastMessage("DD");
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
				EntityType entityType = structure.getEntityByName(map.get("id").toString().replace("minecraft:", ""));
				if (entityType == EntityType.AREA_EFFECT_CLOUD) {
					Location entityLoc = loc.clone().add(structure.getOffset()).add(entityPair.getKey());
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						if (entry.getKey().equalsIgnoreCase("Tags")) {
							if (entry.getValue() instanceof List) {
								List<String> tags = (List<String>) entry.getValue();
								if (tags.contains("gm4_orbis_populate")) {
									if (tags.contains("gm4_chest")) {
										if (tags.contains("gm4_dungeon")) placeChest(entityLoc, "dungeon");
										else if (tags.contains("gm4_tower")) placeChest(entityLoc, "tower");
									}
									if (tags.contains("gm4_spawner")) {
										if (tags.contains("gm4_default_spawner")) placeSpawner(entityLoc, "default");
										else if (tags.contains("gm4_water_spawner")) placeSpawner(entityLoc, "water");
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
		}

//		if (coolerCaves) mc.coolerCaves().loadChunk(c);
	}

	private void placeChest(Location loc, String type) {
		Block b = loc.getBlock();
		if (b.getType() != Material.CHEST) b.setType(Material.CHEST, true);
		if (type.equalsIgnoreCase("dungeon")) {
			if (b.getX() <= 29) {

			}
			else if (b.getX() >= 30 && b.getX() <= 39) {

			}
			else if (b.getX() >= 40) {

			}
		}
		else if (type.equalsIgnoreCase("tower")) {
			if (b.getX() <= 89) {

			}
			else if (b.getX() >= 90 && b.getX() <= 109) {

			}
			else if (b.getX() >= 110 && b.getX() <= 129) {

			}
			else if (b.getX() >= 130 && b.getX() <= 149) {

			}
			else if (b.getX() >= 150) {

			}
		}
	}
	private void placeSpawner(Location loc, String type) {

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
					for (Player p: Bukkit.getOnlinePlayers()) {
						if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
							p.sendMessage(mc.chatPrefix + ChatColor.GREEN + "Updated queued CustomTerrain blocks!");
						}
					}
					System.out.println(ConsoleColor.GREEN + "Updated queued CustomTerrain blocks!");
					Bukkit.getScheduler().cancelTask(task[0]);
					isReplacing = false;
				}
			}
		}, 0L, 20L);
	}
}
