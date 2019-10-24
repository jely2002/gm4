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
	private CustomTerrain ct;
	public CoolerCaves(MainClass mc, CustomTerrain ct) {
		this.mc = mc;
		this.ct = ct;
	}

	private List<BiomeGroup> updatable = Arrays.asList(BiomeGroup.SNOWY, BiomeGroup.OCEAN, BiomeGroup.DESERT, BiomeGroup.BADLANDS);
	private BiomeGroup[] updatableArray = updatable.toArray(new BiomeGroup[0]);
	private List<Material> updatableBlocks = Arrays.asList(Material.GRASS, Material.DANDELION, Material.POPPY, Material.ALLIUM, Material.AZURE_BLUET, Material.OXEYE_DAISY, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP,
			Material.STONE, Material.GRASS_BLOCK, Material.DIRT, Material.GRANITE, Material.DIORITE, Material.ANDESITE, Material.SAND, Material.LAVA, Material.OBSIDIAN, Material.OAK_PLANKS, Material.OAK_FENCE, Material.GRAVEL);


	void loadChunk(Chunk c) {
		ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
		List<BiomeGroup> nwB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 0), updatableArray);
		List<BiomeGroup> neB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 0), updatableArray);
		List<BiomeGroup> seB = BiomeGroup.getBiomeGroups(cs.getBiome(15, 15), updatableArray);
		List<BiomeGroup> swB = BiomeGroup.getBiomeGroups(cs.getBiome(0, 15), updatableArray);

		if (nwB.size() == 0 && neB.size() == 0 && seB.size() == 0 && swB.size() == 0) return;

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
			UpdatableBlock ub = getReplacement(block.getKey(), block.getValue(), group);
			ct.replacements.add(ub);
		}
		ct.updateBlocks();
	}

	private UpdatableBlock getReplacement(Location loc, Material mat, BiomeGroup group) {
		UpdatableBlock ub = new UpdatableBlock(loc, null, false);
		switch (group) {
			case SNOWY:
				switch (mat) {
					case GRASS:
						ub.setMaterial(Material.SNOW);
						ub.setConsumer(block -> {
							Snow s = (Snow) block.getBlockData();
							s.setLayers(2);
							block.setBlockData(s);
						});
						break;
					case DANDELION: case POPPY: case BLUE_ORCHID: case ALLIUM: case AZURE_BLUET: case OXEYE_DAISY: case ORANGE_TULIP: case PINK_TULIP: case RED_TULIP: case WHITE_TULIP:
						ub.setMaterial(Material.SNOW);
						ub.setConsumer(block -> {
							Snow s = (Snow) block.getBlockData();
							s.setLayers(3);
							block.setBlockData(s);
						});
						break;
					case STONE: case GRASS_BLOCK: ub.setMaterial(Material.SNOW_BLOCK); break;
					case DIRT: case GRANITE: ub.setMaterial(Material.PACKED_ICE); break;
					case DIORITE: ub.setMaterial(Material.BLUE_ICE); break;
					case ANDESITE: ub.setMaterial(Material.ICE); break;
					case SAND: ub.setMaterial(Material.WHITE_CONCRETE_POWDER); break;
					case LAVA: case OBSIDIAN: ub.setMaterial(Material.WATER); break;// ub.setUpdate(true);
				}
				break;
			case OCEAN:
				switch (mat) {
					case OAK_PLANKS: ub.setMaterial(Material.DARK_PRISMARINE); break;
					case OAK_FENCE: ub.setMaterial(Material.PRISMARINE_BRICKS); break;
				}
				break;
			case DESERT:
				switch (mat) {
					case STONE:
					case GRANITE:
					case DIORITE:
					case ANDESITE:
						ub.setMaterial(Material.SANDSTONE); break;
					case GRAVEL: ub.setMaterial(Material.SAND); break;
					case OAK_PLANKS: ub.setMaterial(Material.DARK_OAK_PLANKS); break;
					case OAK_FENCE: ub.setMaterial(Material.BIRCH_FENCE); ub.setUpdate(true); break;
				}
				break;
			case BADLANDS:
				switch (mat) {
					case STONE: case GRANITE: case DIORITE: case ANDESITE:
						int y = loc.getBlockY();
						if (y < 12) ub.setMaterial(Material.BROWN_TERRACOTTA);
						else if (y < 27) ub.setMaterial(Material.TERRACOTTA);
						else if (y < 29) ub.setMaterial(Material.RED_TERRACOTTA);
						else if (y < 37) ub.setMaterial(Material.ORANGE_TERRACOTTA);
						else if (y < 52) ub.setMaterial(Material.TERRACOTTA);
						else if (y < 58) ub.setMaterial(Material.LIGHT_GRAY_TERRACOTTA);
						else if (y < 62) ub.setMaterial(Material.BROWN_TERRACOTTA);
						else ub.setMaterial(Material.TERRACOTTA);
						break;
					case GRAVEL: ub.setMaterial(Material.RED_SAND); break;
				}
				break;
		}
		return ub;
	}
}