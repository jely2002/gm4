package com.belka.spigot.gm4.customTerrain;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import javafx.util.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;

import java.util.*;

public class CoolerCaves {
	/*
	update per chunk, updated chunk changes bottom bedrock to barrier

	go below player > check chunk by chunk corner
	go ± X,Y,Z chunk > check chunk
	for ^ chunks go ± X,Y,Z chunk > check chunk
	max 10 chunks per direction (render distance)
	 */
	private static MainClass mc;
	public CoolerCaves(MainClass mc) {
		this.mc = mc;
	}

	private static List<BiomeGroup> updatable = Arrays.asList(BiomeGroup.SNOWY, BiomeGroup.OCEAN, BiomeGroup.DESERT, BiomeGroup.BADLANDS);
	private static BiomeGroup[] updatableArray = updatable.toArray(new BiomeGroup[0]);
	private static List<Material> updatableBlocks = Arrays.asList(Material.GRASS, Material.DANDELION, Material.POPPY, Material.ALLIUM, Material.AZURE_BLUET, Material.OXEYE_DAISY, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP,
			Material.STONE, Material.GRASS_BLOCK, Material.DIRT, Material.DIORITE, Material.ANDESITE, Material.SAND, Material.LAVA, Material.OBSIDIAN, Material.OAK_PLANKS, Material.OAK_FENCE, Material.GRAVEL);

//	private static RateLimiter limiter = RateLimiter.create(1.0);
	private static int blocks = 0;
	private static List<Pair<Map.Entry<Location, Material>, BiomeGroup>> replacements = new ArrayList<>();
	private static boolean isReplacing = false;

//	private static EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(Bukkit.getWorlds().get(0)), -1);

	static void loadChunk(Chunk c) {
		Location corner = new Location(c.getWorld(), c.getX() * 16, 0, c.getZ() * 16);
		Block nw = corner.getBlock();
		if (nw.getType() == Material.BEDROCK) {//maybe change to config
			ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
			List<BiomeGroup> nwB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 0), updatableArray);//Get BiomeGroups that biome is in
			List<BiomeGroup> neB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 0), updatableArray);
			List<BiomeGroup> seB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 15), updatableArray);
			List<BiomeGroup> swB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 15), updatableArray);
			Bukkit.broadcastMessage(nwB.size() + " " + neB.size() + " " + seB.size() + " " + swB.size());
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
							Material mat = cs.getBlockType(x, y, z);
							if (updatableBlocks.contains(mat))
								blockMap.put(new Location(c.getWorld(), cs.getX()*16+x, y, cs.getZ()*16+z), mat);
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
					if (updatableBlocks.contains(mat))
						blockMap.put(new Location(w, cs.getX()*16+x, y, cs.getZ()*16+z), mat);
				}
				updateBlocks(blockMap, group);
			}
		}
	}
	private static void updateBlocks(Map<Location, Material> blockMap, BiomeGroup group) {
		Bukkit.broadcastMessage("updateBlocks " + group.name());
		for (Map.Entry<Location, Material> block : blockMap.entrySet()) {
			replacements.add(new Pair<>(block, group));
//			setReplacement(block, group);
		}
		updateBlocks();
	}

//	private static void startReplacing() {
//		if (isReplacing) return;
//
//		int count = 1;
//		for(int counter = 0; counter < replacements.size(); counter++) {
//			final int finalCounter = counter;
//			Bukkit.getScheduler().scheduleSyncDelayedTask(mc, () -> {
//				Pair<Map.Entry<Location, Material>, BiomeGroup> pair = replacements.get(finalCounter);
//				setReplacement(pair.getKey(), pair.getValue());
//			}, count);
//
//			if(counter % 3 == 0) {
//				count++;
//			}
//		}
//		isReplacing = true;
//	}

	private static void setReplacement(Map.Entry<Location, Material> map, BiomeGroup group) {
//		limiter.acquire();
		Block b = map.getKey().getBlock();
		Material mat = map.getValue();
		blocks++;
//		switch (group) {
//			case SNOWY:
//				switch (mat) {
//					case GRASS:
//						b.setType(Material.SNOW);
//						((Snow) b.getBlockData()).setLayers(2);
//					case DANDELION: case POPPY: case BLUE_ORCHID: case ALLIUM: case AZURE_BLUET: case OXEYE_DAISY: case ORANGE_TULIP: case PINK_TULIP: case RED_TULIP: case WHITE_TULIP:
//						b.setType(Material.SNOW);
//						((Snow) b.getBlockData()).setLayers(3);
//					case STONE: case GRASS_BLOCK: b.setType(Material.SNOW_BLOCK);
//					case DIRT: case GRANITE: b.setType(Material.PACKED_ICE);
//					case DIORITE: b.setType(Material.BLUE_ICE);
//					case ANDESITE: b.setType(Material.ICE);
//					case SAND: b.setType(Material.WHITE_CONCRETE_POWDER);
//					case LAVA: case OBSIDIAN: b.setType(Material.WATER);
//				}
//			case OCEAN:
//				switch (mat) {
//					case OAK_PLANKS: b.setType(Material.DARK_PRISMARINE);
//					case OAK_FENCE: b.setType(Material.PRISMARINE_BRICKS);
//				}
//			case DESERT:
//				switch (mat) {
//					case STONE: case GRANITE: case DIORITE: case ANDESITE: b.setType(Material.SANDSTONE);
//					case GRAVEL: b.setType(Material.SAND);
//					case OAK_PLANKS: b.setType(Material.DARK_OAK_PLANKS);
//					case OAK_FENCE: b.setType(Material.DARK_OAK_FENCE);
//				}
//			case BADLANDS:
//				switch (mat) {
//					case STONE: case GRANITE: case DIORITE: case ANDESITE:
//						int y = map.getKey().getBlockY();
//						if (y < 12) b.setType(Material.BROWN_TERRACOTTA);
//						else if (y < 27) b.setType(Material.TERRACOTTA);
//						else if (y < 29) b.setType(Material.RED_TERRACOTTA);
//						else if (y < 37) b.setType(Material.ORANGE_TERRACOTTA);
//						else if (y < 52) b.setType(Material.TERRACOTTA);
//						else if (y < 58) b.setType(Material.LIGHT_GRAY_TERRACOTTA);
//						else if (y < 62) b.setType(Material.BROWN_TERRACOTTA);
//						else b.setType(Material.TERRACOTTA);
//					case GRAVEL: b.setType(Material.RED_SAND);
//				}
//		}
		switch (group) {
			case SNOWY:
				Bukkit.broadcastMessage(blocks + " SNOWY " + mat.name() + " " + group.name());
				switch (mat) {
					case GRASS:
						b.setType(Material.SNOW);
						((Snow) b.getBlockData()).setLayers(2);
						break;
					case DANDELION: case POPPY: case BLUE_ORCHID: case ALLIUM: case AZURE_BLUET: case OXEYE_DAISY: case ORANGE_TULIP: case PINK_TULIP: case RED_TULIP: case WHITE_TULIP:
						b.setType(Material.SNOW);
						((Snow) b.getBlockData()).setLayers(3);
						break;
					case STONE: case GRASS_BLOCK: changeBlock(b, Material.SNOW_BLOCK); break;
					case DIRT: case GRANITE: changeBlock(b, Material.PACKED_ICE); break;
					case DIORITE: changeBlock(b, Material.BLUE_ICE); break;
					case ANDESITE: changeBlock(b, Material.ICE); break;
					case SAND: changeBlock(b, Material.WHITE_CONCRETE_POWDER); break;
					case LAVA: case OBSIDIAN: changeBlock(b, Material.WATER); break;
				}
				break;
			case OCEAN:
				Bukkit.broadcastMessage(blocks + " OCEAN " + mat.name() + " " + group.name());
				switch (mat) {
					case OAK_PLANKS: changeBlock(b, Material.DARK_PRISMARINE); break;
					case OAK_FENCE: changeBlock(b, Material.PRISMARINE_BRICKS); break;
				}
				break;
			case DESERT:
				Bukkit.broadcastMessage(blocks + " DESERT " + mat.name() + " " + group.name());
				switch (mat) {
					case STONE: case GRANITE: case DIORITE: case ANDESITE: changeBlock(b, Material.SANDSTONE); break;
					case GRAVEL: changeBlock(b, Material.SAND); break;
					case OAK_PLANKS: changeBlock(b, Material.DARK_OAK_PLANKS); break;
					case OAK_FENCE: changeBlock(b, Material.DARK_OAK_FENCE); break;
				}
				break;
			case BADLANDS:
				Bukkit.broadcastMessage(blocks + " BADLANDS " + mat.name() + " " + group.name());
				switch (mat) {
					case STONE: case GRANITE: case DIORITE: case ANDESITE:
						int y = map.getKey().getBlockY();
						if (y < 12) changeBlock(b, Material.BROWN_TERRACOTTA);
						else if (y < 27) changeBlock(b, Material.TERRACOTTA);
						else if (y < 29) changeBlock(b, Material.RED_TERRACOTTA);
						else if (y < 37) changeBlock(b, Material.ORANGE_TERRACOTTA);
						else if (y < 52) changeBlock(b, Material.TERRACOTTA);
						else if (y < 58) changeBlock(b, Material.LIGHT_GRAY_TERRACOTTA);
						else if (y < 62) changeBlock(b, Material.BROWN_TERRACOTTA);
						else changeBlock(b, Material.TERRACOTTA);
						break;
					case GRAVEL: changeBlock(b, Material.RED_SAND); break;
				}
				break;
		}
	}
	private static void changeBlock(Block b, Material mat) {
//		try {
//			editSession.setBlock(BlockVector3.at(b.getX(),b.getY(),b.getZ()), Objects.requireNonNull(BlockTypes.get("minecraft:" + mat.name().toLowerCase())).getDefaultState().toBaseBlock());
//		} catch (MaxChangedBlocksException ignored) {
//
//		}

//		limiter.acquire();
//		new BukkitRunnable() {
//			@Override
//			public void run() {
				b.setType(mat,false);
//				Bukkit.broadcastMessage(ChatColor.GOLD + "update i:" + blocks + " mat:" + mat.name() + " x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ());
//			}
//		}.runTask(mc);
	}
	private static void updateBlocks() {
		if (isReplacing) return;
		isReplacing = true;
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			int n = 1024;//16384
			List<Pair<Map.Entry<Location, Material>, BiomeGroup>> tmp = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				if (replacements.size() == 0) break;
				tmp.add(replacements.get(0));
				replacements.remove(0);
			}
			for (int i = 0; i < tmp.size(); i++) {
				Pair<Map.Entry<Location, Material>, BiomeGroup> pair = tmp.get(i);
				setReplacement(pair.getKey(), pair.getValue());
				if (tmp.size() < n && i == tmp.size() - 1) {
					Bukkit.getScheduler().cancelTask(task[0]);
					isReplacing = false;
				}
			}
		}, 0L, 20L);
	}
}