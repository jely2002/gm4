package com.belka.spigot.gm4.customTerrain;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Level;

class CoolerCaves {
	/*
	update per chunk, updated chunk changes bottom bedrock to barrier

	go below player > check chunk by chunk corner
	go ± X,Y,Z chunk > check chunk
	for ^ chunks go ± X,Y,Z chunk > check chunk
	max 10 chunks per direction (render distance)
	 */
	private static List<BiomeGroup> updatable = Arrays.asList(BiomeGroup.SNOWY, BiomeGroup.OCEAN, BiomeGroup.DESERT, BiomeGroup.BADLANDS);
	private static BiomeGroup[] updatableArray = updatable.toArray(new BiomeGroup[0]);

	static void loadChunk(Chunk c) {
		Location corner = new Location(c.getWorld(), c.getX() * 16, 0, c.getZ() * 16);
		Block nw = corner.getBlock();
		if (nw.getType() == Material.BEDROCK) {//maybe change to config
			ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
			List<BiomeGroup> nwB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 0), updatableArray);//Get BiomeGroups that biome is in
			List<BiomeGroup> neB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 0), updatableArray);
			List<BiomeGroup> seB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 15), updatableArray);
			List<BiomeGroup> swB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 15), updatableArray);

			if (nwB.size() == 0 && neB.size() == 0 && seB.size() == 0 && swB.size() == 0) return;

			if (Helper.allEqual(nwB, neB, seB, swB)) { //If chunk corners have same biome
				updateFullChunk(c.getWorld(), cs, nwB.get(0));
			}
			else { //Chunk corners are unique
				for (int x = 0 ; x < 16 ; x++ ) {
					for (int z = 0; z < 16; z++) {
						List<BiomeGroup> bg = BiomeGroup.getBiomeGroups(cs.getBiome(x, z), updatableArray);
						if (bg.size() == 0) return;
						Map<Location, Material> blockMap = new HashMap<>();
						for(int y = 0; y < cs.getHighestBlockYAt(x, z); y++) {
							blockMap.put(new Location(c.getWorld(), cs.getX()*16+x, y, cs.getZ()*16+z), cs.getBlockType(x, y, z));
						}
						updateBlocks(blockMap, bg.get(0));
					}
				}
			}
		}
	}

	private static void updateFullChunk(World w, ChunkSnapshot cs, BiomeGroup group) {
		for (int x = 0 ; x < 16 ; x++ ) {
			for (int z = 0; z < 16; z++) {
				Map<Location, Material> blockMap = new HashMap<>();
				for(int y = 0; y < cs.getHighestBlockYAt(x, z); y++) {
					Material mat = cs.getBlockType(x, y, z);
					if (mat == Material.GRASS || mat == Material.DANDELION || mat == Material.POPPY || mat == Material.ALLIUM || mat == Material.AZURE_BLUET || mat == Material.OXEYE_DAISY || mat == Material.ORANGE_TULIP || mat == Material.PINK_TULIP || mat == Material.RED_TULIP || mat == Material.WHITE_TULIP ||
							mat == Material.STONE || mat == Material.GRASS_BLOCK || mat == Material.DIRT || mat == Material.DIORITE || mat == Material.ANDESITE || mat == Material.SAND || mat == Material.LAVA || mat == Material.OBSIDIAN || mat == Material.OAK_PLANKS || mat == Material.OAK_FENCE || mat == Material.GRAVEL)
						blockMap.put(new Location(w, cs.getX()*16+x, y, cs.getZ()*16+z), mat);
				}
				updateBlocks(blockMap, group);
			}
		}
//		final List<String> blocksRaw = mc.getConfig().getStringList("");
//		int count = 1;
//		for(int counter = 0; counter < blocksRaw.size(); counter++) {
//			final int finalCounter = counter;
//			Bukkit.getScheduler().scheduleSyncDelayedTask(mc, () -> {
//				String[] rawArray = blocksRaw.get(finalCounter).split(", ");
//				Location location = new Location(Bukkit.getWorlds().get(0), Integer.parseInt(rawArray[1]), Integer.parseInt(rawArray[2]), Integer.parseInt(rawArray[3]));
//				Block block = location.add(0,0,0).getBlock();
//				block.setType(Objects.requireNonNull(Material.getMaterial(rawArray[4])));
//			}, count);
//			if(counter % 3 == 0) {
//				count++;
//			}
//		}
	}
	private static void updateBlocks(Map<Location, Material> blockMap, BiomeGroup group) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "update " + blockMap.size());
//		for (Map.Entry<Location, Material> block : blockMap.entrySet()) {
//			setReplacement(block, group);
//		}
	}

	private static void setReplacement(Map.Entry<Location, Material> map, BiomeGroup group) {
		Block b = map.getKey().getBlock();
		Material mat = map.getValue();
		switch (group) {
			case SNOWY:
				switch (mat) {
					case GRASS:
						b.setType(Material.SNOW);
						((Snow) b.getBlockData()).setLayers(2);
					case DANDELION: case POPPY: case BLUE_ORCHID: case ALLIUM: case AZURE_BLUET: case OXEYE_DAISY: case ORANGE_TULIP: case PINK_TULIP: case RED_TULIP: case WHITE_TULIP:
						b.setType(Material.SNOW);
						((Snow) b.getBlockData()).setLayers(3);
					case STONE: case GRASS_BLOCK: b.setType(Material.SNOW_BLOCK);
					case DIRT: case GRANITE: b.setType(Material.PACKED_ICE);
					case DIORITE: b.setType(Material.BLUE_ICE);
					case ANDESITE: b.setType(Material.ICE);
					case SAND: b.setType(Material.WHITE_CONCRETE_POWDER);
					case LAVA: case OBSIDIAN: b.setType(Material.WATER);
				}
			case OCEAN:
				Bukkit.broadcastMessage("ocean");
				switch (mat) {
					case OAK_PLANKS: b.setType(Material.DARK_PRISMARINE);
					case OAK_FENCE: b.setType(Material.PRISMARINE_BRICKS);
				}
			case DESERT:
				Bukkit.broadcastMessage("desert");
				switch (mat) {
					case STONE: case GRANITE: case DIORITE: case ANDESITE: b.setType(Material.SANDSTONE);
					case GRAVEL: b.setType(Material.SAND);
					case OAK_PLANKS: b.setType(Material.DARK_OAK_PLANKS);
					case OAK_FENCE: b.setType(Material.DARK_OAK_FENCE);
				}
			case BADLANDS:
				switch (mat) {
					case STONE: case GRANITE: case DIORITE: case ANDESITE:
						int y = map.getKey().getBlockY();
						if (y < 12) b.setType(Material.BROWN_TERRACOTTA);
						else if (y < 27) b.setType(Material.TERRACOTTA);
						else if (y < 29) b.setType(Material.RED_TERRACOTTA);
						else if (y < 37) b.setType(Material.ORANGE_TERRACOTTA);
						else if (y < 52) b.setType(Material.TERRACOTTA);
						else if (y < 58) b.setType(Material.LIGHT_GRAY_TERRACOTTA);
						else if (y < 62) b.setType(Material.BROWN_TERRACOTTA);
						else b.setType(Material.TERRACOTTA);
					case GRAVEL: b.setType(Material.RED_SAND);
				}
		}
	}
}