package com.belka.spigot.gm4.customTerrain;

import org.bukkit.block.Biome;

import java.util.*;

public enum BiomeGroup {
	SNOWY,
	COLD,
	LUSH,
	WARM,
	OCEAN,
	DESERT,
	BADLANDS,
	FOREST,
	PLAINS,
	RIVER,
	BEACH,
	BORDER;

//	public static boolean isBiome(BiomeGroup group, Biome biome) {
//		return getBiomes(group).contains(biome);
//	}

	public static HashMap<BiomeGroup, List> getBiomeGroups() {
		HashMap<BiomeGroup, List> biomes = new HashMap<>();
		for (BiomeGroup b: BiomeGroup.values()) biomes.put(b, getBiomes(b));
		return biomes;
	}

	public static List<BiomeGroup> getBiomeGroups(Biome b) {
		List<BiomeGroup> groups = new ArrayList<>();
		for (Map.Entry<BiomeGroup, List> entry : getBiomeGroups().entrySet()) {
			if (entry.getValue().contains(b)) groups.add(entry.getKey());
		}
		return groups;
	}
	public static List<BiomeGroup>  getBiomeGroups(Biome b, BiomeGroup... scope) {
		List<BiomeGroup> groups = new ArrayList<>();
		for (BiomeGroup group: scope) {
			if (getBiomes(group).contains(b)) groups.add(group);
		}
		return groups;
	}
	public static List<Biome> getBiomes(BiomeGroup group) {
		switch (group) {
			default: return Collections.singletonList(Biome.THE_VOID);
			case SNOWY: return Arrays.asList(Biome.SNOWY_TAIGA, Biome.SNOWY_BEACH, Biome.SNOWY_TUNDRA, Biome.SNOWY_MOUNTAINS, Biome.SNOWY_TAIGA_HILLS, Biome.SNOWY_TAIGA_MOUNTAINS, Biome.FROZEN_RIVER, Biome.ICE_SPIKES);
			case COLD: return Arrays.asList(Biome.MOUNTAINS, Biome.GRAVELLY_MOUNTAINS, Biome.WOODED_MOUNTAINS, Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.TAIGA, Biome.TAIGA_HILLS, Biome.TAIGA_MOUNTAINS, Biome.GIANT_TREE_TAIGA, Biome.GIANT_SPRUCE_TAIGA, Biome.GIANT_TREE_TAIGA_HILLS, Biome.GIANT_SPRUCE_TAIGA_HILLS);
			case LUSH: return Arrays.asList(Biome.PLAINS, Biome.SUNFLOWER_PLAINS, Biome.FOREST, Biome.FLOWER_FOREST, Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.TALL_BIRCH_FOREST, Biome.TALL_BIRCH_HILLS, Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS, Biome.SWAMP, Biome.SWAMP_HILLS, Biome.JUNGLE, Biome.MODIFIED_JUNGLE, Biome.JUNGLE_EDGE, Biome.MODIFIED_JUNGLE_EDGE, Biome.BAMBOO_JUNGLE, Biome.BAMBOO_JUNGLE_HILLS, Biome.RIVER, Biome.BEACH, Biome.MUSHROOM_FIELDS, Biome.MUSHROOM_FIELD_SHORE, Biome.THE_END, Biome.SMALL_END_ISLANDS, Biome.END_MIDLANDS, Biome.END_HIGHLANDS, Biome.END_BARRENS);
			case WARM: return Arrays.asList(Biome.DESERT, Biome.DESERT_HILLS, Biome.DESERT_LAKES, Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.SHATTERED_SAVANNA, Biome.SHATTERED_SAVANNA_PLATEAU, Biome.BADLANDS, Biome.BADLANDS_PLATEAU, Biome.MODIFIED_BADLANDS_PLATEAU, Biome.WOODED_BADLANDS_PLATEAU, Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, Biome.ERODED_BADLANDS, Biome.NETHER);
			case OCEAN: return Arrays.asList(Biome.DEEP_OCEAN, Biome.FROZEN_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.LUKEWARM_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.WARM_OCEAN, Biome.DEEP_WARM_OCEAN);
			case DESERT: return Arrays.asList(Biome.DESERT, Biome.DESERT_HILLS, Biome.DESERT_LAKES);
			case BADLANDS: return Arrays.asList(Biome.BADLANDS, Biome.BADLANDS_PLATEAU, Biome.MODIFIED_BADLANDS_PLATEAU, Biome.WOODED_BADLANDS_PLATEAU, Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, Biome.ERODED_BADLANDS);
			case FOREST: return Arrays.asList(Biome.FOREST, Biome.DARK_FOREST, Biome.DARK_FOREST_HILLS, Biome.BIRCH_FOREST, Biome.BIRCH_FOREST_HILLS, Biome.TALL_BIRCH_FOREST, Biome.FLOWER_FOREST, Biome.TAIGA, Biome.TAIGA_HILLS, Biome.TAIGA_MOUNTAINS, Biome.SNOWY_TAIGA, Biome.SNOWY_TAIGA_HILLS, Biome.SNOWY_TAIGA_MOUNTAINS, Biome.GIANT_TREE_TAIGA, Biome.GIANT_SPRUCE_TAIGA, Biome.GIANT_TREE_TAIGA_HILLS, Biome.GIANT_SPRUCE_TAIGA_HILLS);
			case PLAINS: return Arrays.asList(Biome.PLAINS, Biome.SNOWY_TUNDRA);
			case RIVER: return Arrays.asList(Biome.RIVER, Biome.FROZEN_RIVER);
			case BEACH: return Arrays.asList(Biome.BEACH, Biome.STONE_SHORE, Biome.SNOWY_BEACH);
			case BORDER: return Arrays.asList(Biome.RIVER);
		}
	}
}
