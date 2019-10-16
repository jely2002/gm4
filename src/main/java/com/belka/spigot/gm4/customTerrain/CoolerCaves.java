package com.belka.spigot.gm4.customTerrain;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;

import java.util.*;

public class CoolerCaves {

	private MainClass mc;
	public CoolerCaves(MainClass mc) {
		this.mc = mc;
	}

	private List<BiomeGroup> updatable = Arrays.asList(BiomeGroup.SNOWY, BiomeGroup.OCEAN, BiomeGroup.DESERT, BiomeGroup.BADLANDS);
	private BiomeGroup[] updatableArray = updatable.toArray(new BiomeGroup[0]);
	private List<Material> updatableBlocks = Arrays.asList(Material.GRASS, Material.DANDELION, Material.POPPY, Material.ALLIUM, Material.AZURE_BLUET, Material.OXEYE_DAISY, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP,
			Material.STONE, Material.GRASS_BLOCK, Material.DIRT, Material.GRANITE, Material.DIORITE, Material.ANDESITE, Material.SAND, Material.LAVA, Material.OBSIDIAN, Material.OAK_PLANKS, Material.OAK_FENCE, Material.GRAVEL);

	private int loadRadius = 3;
	
	private List<UpdatableBlock> replacements = new ArrayList<>();
	private boolean isReplacing = false;
	private int replacingSpeed = 24576;

	private List<Pair<Integer, Integer>> loadedChunks = new ArrayList<>();

	public void init(MainClass mc) {
		if (!mc.storage().config().getBoolean("CustomTerrain.enabled") || !mc.storage().config().getBoolean("CustomTerrain.CoolerCaves.enabled")) return;

		replacingSpeed = mc.storage().config().getInt("CustomTerrain.CoolerCaves.replacingSpeed");
		loadRadius = mc.storage().config().getInt("CustomTerrain.CoolerCaves.loadRadius");
		if (mc.storage().data().getConfigurationSection("CustomTerrain.CoolerCaves.chunks") == null) return;
		for (String chunk: mc.storage().data().getStringList("CustomTerrain.CoolerCaves.chunks")) {
			loadedChunks.add(new Pair<>(Helper.toInteger(chunk.split(" ")[0]), Helper.toInteger(chunk.split(" ")[1])));
		}
	}

	public void disable() {
		List<String> tmp = new ArrayList<>();
		for (Pair<Integer, Integer> pair: loadedChunks) {
			tmp.add(pair.getKey() + " " + pair.getValue());
		}
		mc.storage().data().set("CustomTerrain.CoolerCaves.chunks", tmp);
		mc.storage().saveData();
	}

	void loadChunks(Location loc) {
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
		Location corner = new Location(c.getWorld(), c.getX() * 16, 0, c.getZ() * 16);
		Block nw = corner.getBlock();
		if (nw.getType() == Material.BARRIER) return;//todo maybe change to config

		ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
		List<BiomeGroup> nwB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 0), updatableArray);
		List<BiomeGroup> neB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 0), updatableArray);
		List<BiomeGroup> seB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 15), updatableArray);
		List<BiomeGroup> swB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 15), updatableArray);

		if (nwB.size() == 0 && neB.size() == 0 && seB.size() == 0 && swB.size() == 0) {
			for (int x = 0 ; x < 16 ; x++ ) {
				for (int z = 0; z < 16; z++) {
					Block b = c.getBlock(x, 0, z);
					b.setType(Material.BARRIER);
				}
			}
			return;
		}

		if (Helper.allEqual(nwB, neB, seB, swB)) { //If chunk corners have same biome
			updateFullChunk(c.getWorld(), c, cs, nwB.get(0));
		}
		else { //Chunk corners are unique
			for (int x = 0 ; x < 16 ; x++ ) {
				for (int z = 0; z < 16; z++) {
					Block b = c.getBlock(x, 0, z);
					b.setType(Material.BARRIER);

					List<BiomeGroup> bg = BiomeGroup.getBiomeGroups(cs.getBiome(x, z), updatableArray);
					if (bg.size() == 0) continue;
					Map<Location, Material> blockMap = new HashMap<>();
					for(int y = 0; y < cs.getHighestBlockYAt(x, z) + 1; y++) {
						Material mat = cs.getBlockType(x, y, z);
						if (updatableBlocks.contains(mat))
							blockMap.put(new Location(c.getWorld(), cs.getX()*16+x, y, cs.getZ()*16+z), mat);
					}
					updateColumn(blockMap, bg.get(0));
				}
			}
		}
	}

	private void updateFullChunk(World w, Chunk c, ChunkSnapshot cs, BiomeGroup group) {
		for (int x = 0 ; x < 16 ; x++ ) {
			for (int z = 0; z < 16; z++) {
				Block b = c.getBlock(x, 0, z);
				b.setType(Material.BARRIER);

				Map<Location, Material> blockMap = new HashMap<>();
				for(int y = 0; y < cs.getHighestBlockYAt(x, z) + 1; y++) {
					Material mat = cs.getBlockType(x, y, z);
					if (updatableBlocks.contains(mat))
						blockMap.put(new Location(w, cs.getX()*16+x, y, cs.getZ()*16+z), mat);
				}
				updateColumn(blockMap, group);
			}
		}
	}

	private void updateColumn(Map<Location, Material> blockMap, BiomeGroup group) {
		for (Map.Entry<Location, Material> block : blockMap.entrySet()) {
			UpdatableBlock ub = new UpdatableBlock(block.getKey(), block.getValue());
			ub.setBiomeGroup(group);
			replacements.add(ub);
		}
		updateBlocks();
	}

	private void updateBlocks() {
		if (isReplacing) return;
		isReplacing = true;
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			List<UpdatableBlock> tmp = new ArrayList<>();
			for (int i = 0; i < replacingSpeed; i++) {
				if (replacements.size() == 0) break;
				tmp.add(replacements.get(0));
				replacements.remove(0);
			}
			for (int i = 0; i < tmp.size(); i++) {
				UpdatableBlock pair = tmp.get(i);
				setReplacement(pair);
				if (tmp.size() < replacingSpeed && i == tmp.size() - 1) {
					Bukkit.broadcastMessage("Updated queued chunks");
					Bukkit.getScheduler().cancelTask(task[0]);
					isReplacing = false;
				}
			}
		}, 0L, 20L);
	}

	private void setReplacement(UpdatableBlock ub) {
		Block b = ub.getLocation().getBlock();
		Material mat = ub.getMaterial();
		switch (ub.getBiomeGroup()) {
			case SNOWY:
				switch (mat) {
					case GRASS:
						b.setType(Material.SNOW, false);
						Snow s = (Snow) b.getBlockData();
						s.setLayers(2);
						b.setBlockData(s);
						break;
					case DANDELION: case POPPY: case BLUE_ORCHID: case ALLIUM: case AZURE_BLUET: case OXEYE_DAISY: case ORANGE_TULIP: case PINK_TULIP: case RED_TULIP: case WHITE_TULIP:
						b.setType(Material.SNOW, false);
						Snow s2 = (Snow) b.getBlockData();
						s2.setLayers(3);
						b.setBlockData(s2);
						break;
					case STONE: case GRASS_BLOCK: b.setType(Material.SNOW_BLOCK, false); break;
					case DIRT: case GRANITE: b.setType(Material.PACKED_ICE, false); break;
					case DIORITE: b.setType(Material.BLUE_ICE, false); break;
					case ANDESITE: b.setType(Material.ICE, false); break;
					case SAND: b.setType(Material.WHITE_CONCRETE_POWDER, false); break;
					case LAVA: case OBSIDIAN: b.setType(Material.WATER); break;
				}
				break;
			case OCEAN:
				switch (mat) {
					case OAK_PLANKS: b.setType(Material.DARK_PRISMARINE, false); break;
					case OAK_FENCE: b.setType(Material.PRISMARINE_BRICKS, false); break;
				}
				break;
			case DESERT:
				switch (mat) {
					case STONE:
					case GRANITE:
					case DIORITE:
					case ANDESITE:
						b.setType(Material.SANDSTONE, false); break;
					case GRAVEL: b.setType(Material.SAND, false); break;
					case OAK_PLANKS: b.setType(Material.DARK_OAK_PLANKS, false); break;
					case OAK_FENCE: b.setType(Material.BIRCH_FENCE); break;
				}
				break;
			case BADLANDS:
				switch (mat) {
					case STONE: case GRANITE: case DIORITE: case ANDESITE:
						int y = ub.getLocation().getBlockY();
						if (y < 12) b.setType(Material.BROWN_TERRACOTTA, false);
						else if (y < 27) b.setType(Material.TERRACOTTA, false);
						else if (y < 29) b.setType(Material.RED_TERRACOTTA, false);
						else if (y < 37) b.setType(Material.ORANGE_TERRACOTTA, false);
						else if (y < 52) b.setType(Material.TERRACOTTA, false);
						else if (y < 58) b.setType(Material.LIGHT_GRAY_TERRACOTTA, false);
						else if (y < 62) b.setType(Material.BROWN_TERRACOTTA, false);
						else b.setType(Material.TERRACOTTA, false);
						break;
					case GRAVEL: b.setType(Material.RED_SAND, false); break;
				}
				break;
		}
	}
}